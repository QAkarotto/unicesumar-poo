async function req(method, url, body) {
  let res;
  try {
    res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new Error("Não foi possível conectar ao servidor. Confira se o backend está rodando.");
  }
  if (res.status === 204) return null;
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    // campo / campos dizem em qual campo do formulário mostrar o erro
    throw Object.assign(new Error(data.erro || `Erro ${res.status}`), { campo: data.campo, campos: data.campos });
  }
  return data;
}

export const api = {
  categorias: () => req("GET", "/api/categorias"),
  criarCategoria: (nome) => req("POST", "/api/categorias", { nome }),
  excluirCategoria: (id) => req("DELETE", `/api/categorias/${id}`),
  produtos: () => req("GET", "/api/produtos"),
  criarProduto: (p) => req("POST", "/api/produtos", p),
  atualizarProduto: (id, p) => req("PUT", `/api/produtos/${id}`, p),
  desativarProduto: (id) => req("PATCH", `/api/produtos/${id}/desativacao`),
  movimentacoes: () => req("GET", "/api/movimentacoes"),
  movimentar: (m) => req("POST", "/api/movimentacoes", m),
};
