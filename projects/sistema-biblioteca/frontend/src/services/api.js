
const API_BASE_URL = '/api'

export async function apiRequest(endpoint, options = {}) {
  let response

  try {
    response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    })
  } catch {
    throw new Error(
      'Não foi possível conectar ao servidor. Verifique se o Spring Boot está funcionando.'
    )
  }

  if (response.status === 204) {
    return null
  }

  const texto = await response.text()

  let data = null

  if (texto) {
    try {
      data = JSON.parse(texto)
    } catch {
      data = null
    }
  }

  if (!response.ok) {
    const mensagem =
      data?.message ||
      data?.mensagem ||
      data?.detail ||
      data?.error ||
      `Erro HTTP ${response.status} ao processar a solicitação.`

    throw new Error(mensagem)
  }

  return data
}
