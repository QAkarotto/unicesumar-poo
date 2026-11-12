const API_URL = "http://localhost:8080/api";

// se der erro (servidor fora do ar ou resposta ruim) joga um Error com a mensagem pra tela mostrar
async function requisitar(caminho, opcoes) {
  let resposta;
  try {
    resposta = await fetch(API_URL + "/" + caminho, {
      headers: { "Content-Type": "application/json" },
      ...opcoes,
    });
  } catch {
    throw new Error("Não foi possível conectar ao servidor.");
  }

  if (!resposta.ok) {
    const erro = await resposta.json().catch(() => null);
    throw new Error((erro && erro.mensagem) || "Não foi possível concluir a operação.");
  }

  return resposta.status === 204 ? null : resposta.json();
}

export const api = {
  listar: (recurso) => requisitar(recurso),

  criar: (recurso, dados) =>
    requisitar(recurso, { method: "POST", body: JSON.stringify(dados) }),

  atualizar: (recurso, id, dados) =>
    requisitar(recurso + "/" + id, { method: "PUT", body: JSON.stringify(dados) }),

  alterarSituacao: (recurso, id, situacao) =>
    requisitar(recurso + "/" + id + "/" + situacao, { method: "PATCH" }),

  remover: (recurso, id) => requisitar(recurso + "/" + id, { method: "DELETE" }),
};
