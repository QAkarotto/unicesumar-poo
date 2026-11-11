const BASE_URL = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

export async function requisicao<T>(
  caminho: string,
  metodo = 'GET',
  dados?: unknown,
  usuarioId?: string,
): Promise<T> {
  const controller = new AbortController()
  const timeout = window.setTimeout(() => controller.abort(), 20000)
  const headers: Record<string, string> = { Accept: 'application/json' }
  if (dados !== undefined) headers['Content-Type'] = 'application/json'
  if (usuarioId) headers['X-Usuario-Id'] = usuarioId

  let resposta: Response
  try {
    resposta = await fetch(`${BASE_URL}${caminho}`, {
      method: metodo,
      headers,
      body: dados === undefined ? undefined : JSON.stringify(dados),
      signal: controller.signal,
    })
  } catch {
    throw new Error(
      'Não foi possível conectar ao servidor. Confira se o backend está em execução e tente novamente.',
    )
  } finally {
    window.clearTimeout(timeout)
  }

  if (resposta.status === 204) return undefined as T
  const texto = await resposta.text()
  let conteudo: { mensagem?: string } | null = null
  try {
    conteudo = texto ? JSON.parse(texto) : null
  } catch {
    throw new Error('O servidor não retornou uma resposta válida. Confira a configuração da API.')
  }
  if (!resposta.ok) {
    throw new Error(conteudo?.mensagem || `Não foi possível concluir a operação (HTTP ${resposta.status}).`)
  }
  return conteudo as T
}
