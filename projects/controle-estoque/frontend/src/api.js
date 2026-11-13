// Camada de acesso à API REST do backend (envio e recebimento de JSON).
const BASE_URL = import.meta.env.VITE_API_URL ?? '/api'

/** Erro devolvido pela API, com status HTTP, mensagem e erros por campo (400). */
export class ApiError extends Error {
  constructor(status, corpo) {
    super(corpo?.mensagem ?? `Erro inesperado (HTTP ${status}).`)
    this.status = status
    this.campos = corpo?.campos ?? null
  }
}

async function requisicao(metodo, caminho, corpo) {
  let resposta
  try {
    resposta = await fetch(BASE_URL + caminho, {
      method: metodo,
      headers: corpo !== undefined ? { 'Content-Type': 'application/json' } : {},
      body: corpo !== undefined ? JSON.stringify(corpo) : undefined,
    })
  } catch {
    throw new ApiError(0, { mensagem: 'Não foi possível conectar à API. O backend está em execução?' })
  }

  if (resposta.status === 204) return null
  const dados = await resposta.json().catch(() => null)
  if (!resposta.ok) throw new ApiError(resposta.status, dados)
  return dados
}

export const api = {
  get: (caminho) => requisicao('GET', caminho),
  post: (caminho, corpo) => requisicao('POST', caminho, corpo),
  put: (caminho, corpo) => requisicao('PUT', caminho, corpo),
  patch: (caminho, corpo) => requisicao('PATCH', caminho, corpo),
  delete: (caminho) => requisicao('DELETE', caminho),
}

/** Monta query string ignorando valores vazios: { a: 1, b: '' } -> "?a=1" */
export function query(params) {
  const qs = new URLSearchParams(
    Object.entries(params).filter(([, v]) => v !== undefined && v !== null && v !== ''),
  ).toString()
  return qs ? `?${qs}` : ''
}

export const moeda = (valor) =>
  Number(valor ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

export const dataHora = (iso) => (iso ? new Date(iso).toLocaleString('pt-BR') : '')

export const cnpjFormatado = (c) =>
  c?.length === 14 ? c.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5') : c
