export async function api(caminho, opcoes = {}) {
  let resposta
  try {
    resposta = await fetch('/api' + caminho, {
      ...opcoes,
      headers: { 'Content-Type': 'application/json' },
    })
  } catch {
    throw new Error('Não foi possível conectar ao servidor.')
  }

  if (!resposta.ok) {
    let mensagem = 'Erro ' + resposta.status
    try {
      const corpo = await resposta.json()
      if (corpo.mensagem) {
        mensagem = corpo.mensagem
      }
    } catch {
      if (resposta.status >= 500) {
        mensagem = 'Não foi possível conectar ao servidor.'
      }
    }
    throw new Error(mensagem)
  }

  return resposta.json()
}
