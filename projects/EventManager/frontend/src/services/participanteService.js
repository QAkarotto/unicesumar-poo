const API_URL = "http://localhost:8080/api/participantes";


async function requisicao(url, opcoes = {}) {
  let resposta;

  try {
    resposta = await fetch(url, {
      ...opcoes,
      headers: {
        "Content-Type": "application/json",
        ...opcoes.headers
      }
    });
  } catch {
    throw new Error("Não foi possível conectar ao servidor.");
  }

  if (!resposta.ok) {
    let dados = null;

    try {
      dados = await resposta.json();
    } catch {
      // A resposta pode não conter JSON.
    }

    throw new Error(
      dados?.mensagem ||
      `Erro na requisição: ${resposta.status}`
    );
  }

  if (resposta.status === 204) {
    return null;
  }

  return resposta.json();
}

export function listarParticipantes() {
  return requisicao(API_URL);
}

export function cadastrarParticipante(participante) {
  return requisicao(API_URL, {
    method: "POST",
    body: JSON.stringify(participante)
  });
}

export function atualizarParticipante(id, participante) {
  return requisicao(`${API_URL}/${id}`, {
    method: "PUT",
    body: JSON.stringify(participante)
  });
}

export function excluirParticipante(id) {
  return requisicao(`${API_URL}/${id}`, {
    method: "DELETE"
  });
}