const API_URL = 'http://localhost:8080/api';

async function request(path, method = 'GET', body) {
  const response = await fetch(API_URL + path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined,
  });

  if (response.status === 204) {
    return null;
  }

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.mensagem || 'Erro HTTP ' + response.status);
  }
  return data;
}

export const api = {
  listarLeitores: () => request('/leitores'),
  criarLeitor: (leitor) => request('/leitores', 'POST', leitor),
  atualizarLeitor: (id, leitor) => request('/leitores/' + id, 'PUT', leitor),
  excluirLeitor: (id) => request('/leitores/' + id, 'DELETE'),

  listarLivros: () => request('/livros'),
  criarLivro: (livro) => request('/livros', 'POST', livro),
  atualizarLivro: (id, livro) => request('/livros/' + id, 'PUT', livro),
  excluirLivro: (id) => request('/livros/' + id, 'DELETE'),

  listarEmprestimos: () => request('/emprestimos'),
  criarEmprestimo: (emprestimo) => request('/emprestimos', 'POST', emprestimo),
  devolverEmprestimo: (id) => request('/emprestimos/' + id + '/devolucao', 'POST'),
};
