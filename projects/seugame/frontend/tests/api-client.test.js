import test from 'node:test'
import assert from 'node:assert/strict'

let sequence = 0
const client = () => import(`../src/services/api.js?test=${++sequence}`)
const response = (body, status = 200) => new Response(body === null ? null : JSON.stringify(body), { status, headers: { 'Content-Type': 'application/json' } })

test('escrita envia o cabeçalho CSRF indicado pelo servidor e cookies', async (t) => {
  const requests = []
  t.mock.method(globalThis, 'fetch', async (url, options) => {
    requests.push({ url, options })
    return response(url.endsWith('/csrf') ? { headerName: 'X-CSRF-CUSTOM', token: 'token-teste' } : { nome: 'Nome atualizado' })
  })
  const { api } = await client()
  await api('/usuarios/me', { method: 'PATCH', body: { nome: 'Nome atualizado' } })
  assert.equal(requests.length, 2)
  assert.equal(requests[1].options.credentials, 'include')
  assert.equal(requests[1].options.headers['X-CSRF-CUSTOM'], 'token-teste')
  assert.equal(requests[1].options.headers['Content-Type'], 'application/json')
  assert.deepEqual(JSON.parse(requests[1].options.body), { nome: 'Nome atualizado' })
})

test('token fica em memória, é compartilhado em leituras concorrentes e renovado', async (t) => {
  let requests = 0
  t.mock.method(globalThis, 'fetch', async () => response({ headerName: 'X-CSRF-TOKEN', token: `token-${++requests}` }))
  const { getCsrf, renewCsrf } = await client()
  const [first, second] = await Promise.all([getCsrf(), getCsrf()])
  assert.equal(requests, 1)
  assert.deepEqual(first, second)
  assert.notDeepEqual(await renewCsrf(), first)
  assert.equal(requests, 2)
})

test('sessão expirada pode chegar como 403 CSRF: detecta 401 e não repete escrita', async (t) => {
  let writes = 0
  let expired = 0
  t.mock.method(globalThis, 'fetch', async (url, options) => {
    if (url.endsWith('/csrf')) return response({ headerName: 'X-CSRF-TOKEN', token: 'antigo' })
    if (options.method === 'POST') { writes++; return response({ detail: 'CSRF inválido' }, 403) }
    return response({ detail: 'Autenticação necessária' }, 401)
  })
  const { api, onSessionExpired } = await client()
  const remove = onSessionExpired(() => expired++)
  await assert.rejects(api('/torneios', { method: 'POST', body: {} }), (error) => error.status === 401)
  assert.equal(writes, 1)
  assert.equal(expired, 1)
  remove()
})

test('403 de autoria mantém sessão válida e também não repete a escrita', async (t) => {
  let writes = 0
  let expired = 0
  t.mock.method(globalThis, 'fetch', async (url, options) => {
    if (url.endsWith('/csrf')) return response({ headerName: 'X-CSRF-TOKEN', token: 'valido' })
    if (options.method === 'DELETE') { writes++; return response({ detail: 'Você não pode gerenciar este recurso.' }, 403) }
    return response({ id: 1 })
  })
  const { api, onSessionExpired } = await client()
  onSessionExpired(() => expired++)
  await assert.rejects(api('/torneios/1', { method: 'DELETE' }), (error) => error.status === 403)
  assert.equal(writes, 1)
  assert.equal(expired, 0)
})

test('500 e 503 não expõem detalhes internos; falha de rede permite nova tentativa', async (t) => {
  let status = 500
  t.mock.method(globalThis, 'fetch', async () => {
    if (status === 0) throw new TypeError('detalhe técnico interno')
    return response({ detail: 'SQL senha segredo interno' }, status)
  })
  const { api } = await client()
  for (const code of [500, 503, 0]) {
    status = code
    await assert.rejects(api('/torneios'), (error) => [500, 503].includes(error.status) && !/SQL|senha|segredo|técnico/.test(error.message))
  }
  status = 200
  assert.deepEqual(await api('/torneios'), { detail: 'SQL senha segredo interno' })
})

test('400, 404 e 409 preservam códigos e apresentam campos sem valores internos', async (t) => {
  let status = 400
  t.mock.method(globalThis, 'fetch', async () => response({ detail: 'Confira os dados.', errors: { 'participantes[0]': 'mensagem interna', nome: 'must not be blank' } }, status))
  const { api } = await client()
  for (const code of [400, 404, 409]) {
    status = code
    await assert.rejects(api('/torneios'), (error) => error.status === code && error.fields.includes('Confira o campo Participantes.') && !error.fields.join().includes('interna'))
  }
})

test('401 no login não dispara expiração de uma sessão por falha de credenciais', async (t) => {
  let expired = 0
  t.mock.method(globalThis, 'fetch', async (url) => response(url.endsWith('/csrf') ? { headerName: 'X-CSRF-TOKEN', token: 'valido' } : { detail: 'Credenciais inválidas.' }, url.endsWith('/csrf') ? 200 : 401))
  const { api, onSessionExpired } = await client()
  onSessionExpired(() => expired++)
  await assert.rejects(api('/auth/login', { method: 'POST', body: {}, ignoreSession: true }), (error) => error.status === 401)
  assert.equal(expired, 0)
})

test('204 sem corpo é válido e GET não obtém nem envia token CSRF', async (t) => {
  const calls = []
  t.mock.method(globalThis, 'fetch', async (url, options) => {
    calls.push({ url, options })
    return url.endsWith('/csrf') ? response({ headerName: 'X-CSRF-TOKEN', token: 'valido' }) : options.method === 'DELETE' ? response(null, 204) : response([])
  })
  const { api } = await client()
  assert.deepEqual(await api('/torneios'), [])
  assert.equal(calls.length, 1)
  assert.equal(calls[0].options.headers['X-CSRF-TOKEN'], undefined)
  assert.equal(await api('/torneios/1', { method: 'DELETE' }), null)
})
