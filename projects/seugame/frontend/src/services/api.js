const baseUrl = (import.meta.env?.VITE_API_BASE_URL || '/api').replace(/\/$/, '')
const unsafe = new Set(['POST', 'PUT', 'PATCH', 'DELETE'])
const sessionListeners = new Set()
let csrfToken = null
let csrfRequest = null
let csrfGeneration = 0

const fallbackMessages = {
  400: 'Confira os dados informados e tente novamente.',
  401: 'Sua sessão terminou. Entre novamente para continuar.',
  403: 'Você não tem permissão ou a proteção da sessão expirou. Tente novamente.',
  404: 'O recurso não foi encontrado. Ele pode ter sido removido.',
  409: 'Os dados conflitam com o estado atual. Confira as informações e tente novamente.',
}
const fieldNames = {
  nome: 'Nome', usuario: 'Usuário', email: 'E-mail', senha: 'Senha', confirmarSenha: 'Confirmação da senha',
  identificador: 'Usuário ou e-mail', tipo: 'Formato', dataInicio: 'Data de início',
  participantes: 'Participantes', placar1: 'Primeiro placar', placar2: 'Segundo placar',
}

export class ApiError extends Error {
  constructor(status, message, fields = []) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.fields = fields
  }
}
export function onSessionExpired(listener) {
  sessionListeners.add(listener)
  return () => sessionListeners.delete(listener)
}
function expireSession() {
  resetCsrf()
  for (const listener of sessionListeners) listener()
}
export function resetCsrf() {
  csrfGeneration += 1
  csrfToken = null
  csrfRequest = null
}
async function transport(path, { signal, headers, body, ...options } = {}) {
  let response
  try {
    const timeout = AbortSignal.timeout(15000)
    response = await fetch(baseUrl + path, {
      ...options, credentials: 'include', headers: { Accept: 'application/json', ...headers },
      ...(body === undefined ? {} : { body: JSON.stringify(body) }),
      signal: signal ? AbortSignal.any([signal, timeout]) : timeout,
    })
  } catch (error) {
    if (signal?.aborted) throw error
    throw new ApiError(503, 'Não foi possível conectar ao servidor. Verifique sua conexão e tente novamente.')
  }
  const content = response.status === 204 ? null : await response.json().catch(() => null)
  if (!response.ok) {
    const message = response.status >= 500
      ? 'O servidor está indisponível no momento. Tente novamente em instantes.'
      : content?.detail || fallbackMessages[response.status] || 'Não foi possível concluir a operação.'
    const fields = Object.keys(content?.errors || {}).map((field) => {
      const base = field.split('.')[0].split('[')[0]
      return `Confira o campo ${fieldNames[base] || 'informado'}.`
    })
    throw new ApiError(response.status, message, [...new Set(fields)])
  }
  if (response.status !== 204 && content === null) throw new ApiError(503, 'O servidor retornou uma resposta inválida. Tente novamente.')
  return content
}
export async function getCsrf() {
  if (csrfToken) return csrfToken
  if (!csrfRequest) {
    const generation = csrfGeneration
    const pending = transport('/auth/csrf').then((token) => {
      if (typeof token?.headerName !== 'string' || typeof token?.token !== 'string')
        throw new ApiError(503, 'Não foi possível preparar a proteção da sessão. Tente novamente.')
      if (generation === csrfGeneration) csrfToken = token
      return token
    }).finally(() => {
      if (csrfRequest === pending) csrfRequest = null
    })
    csrfRequest = pending
  }
  return csrfRequest
}
export async function renewCsrf() { resetCsrf(); return getCsrf() }

export async function api(path, { method = 'GET', body, signal, ignoreSession = false } = {}) {
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (unsafe.has(method)) {
    const csrf = await getCsrf()
    headers[csrf.headerName] = csrf.token
  }
  try {
    return await transport(path, { method, body, headers, signal })
  } catch (error) {
    if (error.status === 401 && !ignoreSession) expireSession()
    if (error.status === 403 && unsafe.has(method)) {
      resetCsrf()
      try { await transport('/usuarios/me') } catch (sessionError) {
        if (sessionError.status === 401) {
          if (!ignoreSession) expireSession()
          throw new ApiError(401, fallbackMessages[401])
        }
      }
    }
    throw error
  }
}

export async function loadTournament(id, signal) {
  const path = '/torneios/' + id
  const [detail, partidas] = await Promise.all([api(path, { signal }), api(path + '/partidas', { signal })])
  const table = detail.torneio.tipo === 'TABELA'
  const competition = await api(path + (table ? '/classificacao' : '/chaveamento'), { signal })
  return { ...detail, partidas, classificacao: table ? competition : [], chaveamento: table ? [] : competition }
}
