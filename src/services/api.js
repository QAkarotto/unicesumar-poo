/*
 * Comunicação com a API REST do backend (Spring Boot).
 * Em desenvolvimento o Vite encaminha tudo que começa com /api para http://localhost:8080
 * (veja vite.config.js), então o cookie de sessão fica no mesmo endereço do frontend.
 *
 * Os nomes e argumentos das funções seguem o contrato do BACKEND.md. Os argumentos
 * `user` e `userId` foram mantidos por compatibilidade, mas o servidor ignora esses valores:
 * quem está logado é descoberto pela sessão.
 */

export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

async function request(path, { method = 'GET', body } = {}) {
  let response
  try {
    response = await fetch(`/api${path}`, {
      method,
      credentials: 'same-origin',
      headers: body === undefined ? undefined : { 'Content-Type': 'application/json' },
      body: body === undefined ? undefined : JSON.stringify(body),
    })
  } catch {
    throw new ApiError('Não foi possível falar com o servidor. Ele está ligado?', 0)
  }

  if (response.status === 204) return null

  const data = await response.json().catch(() => null)
  if (!response.ok) {
    throw new ApiError(data?.mensagem || 'Algo deu errado. Tente novamente.', response.status)
  }
  return data
}

const enc = encodeURIComponent

// ---------- Catálogo ----------

export function getMediaCatalog() {
  return request('/obras')
}

export async function getMediaById(id) {
  try {
    return await request(`/obras/${enc(id)}`)
  } catch (cause) {
    if (cause.status === 404) return undefined
    throw cause
  }
}

// ---------- Conta e lista pessoal ----------

export const accountApi = {
  // null quando ninguém está logado (o servidor responde 204)
  me: () => request('/auth/me'),

  register: ({ nome, email, senha }) => request('/auth/cadastro', { method: 'POST', body: { nome, email, senha } }),

  login: ({ email, senha }) => request('/auth/login', { method: 'POST', body: { email, senha } }),

  async logout() {
    await request('/auth/logout', { method: 'POST' })
  },

  library: () => request('/biblioteca'),

  toggleFavorite: (user, mediaId) => request(`/biblioteca/favoritos/${enc(mediaId)}/alternar`, { method: 'POST' }),

  toggleWatchlist: (user, mediaId) => request(`/biblioteca/pauta/${enc(mediaId)}/alternar`, { method: 'POST' }),

  updateWatchlist: (user, mediaId, change) => request(`/biblioteca/pauta/${enc(mediaId)}`, {
    method: 'PATCH',
    body: { status: change.status, progresso: change.progresso },
  }),
}

// ---------- Avaliações e comentários ----------

export const feedbackApi = {
  ratings: (mediaId) => request(`/obras/${enc(mediaId)}/avaliacoes`),

  rate: (mediaId, userId, value) => request(`/obras/${enc(mediaId)}/avaliacoes`, { method: 'PUT', body: { nota: value } }),

  removeRating: (mediaId) => request(`/obras/${enc(mediaId)}/avaliacoes`, { method: 'DELETE' }),

  comments: (mediaId) => request(`/obras/${enc(mediaId)}/comentarios`),

  addComment: (mediaId, user, text, spoiler) => request(`/obras/${enc(mediaId)}/comentarios`, {
    method: 'POST',
    body: { texto: text, spoiler },
  }),

  toggleCommentLike: (mediaId, userId, commentId) => request(`/obras/${enc(mediaId)}/comentarios/${enc(commentId)}/curtidas`, { method: 'POST' }),

  deleteComment: (mediaId, userId, commentId) => request(`/obras/${enc(mediaId)}/comentarios/${enc(commentId)}`, { method: 'DELETE' }),
}
