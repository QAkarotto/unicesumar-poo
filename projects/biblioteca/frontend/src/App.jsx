import { useEffect, useState } from "react";

const API = "http://localhost:8080/api";
const JSON_HEADERS = { "Content-Type": "application/json" };

export default function App() {
  const [emprestimos, setEmprestimos] = useState([]);
  const [mensagem, setMensagem] = useState(null);
  const [erro, setErro] = useState(null);

  const [novoUsuario, setNovoUsuario] = useState({ nome: "", email: "" });
  const [novoLivro, setNovoLivro] = useState({ titulo: "", autor: "", isbn: "" });
  const [novoEmprestimo, setNovoEmprestimo] = useState({ usuarioId: "", livroId: "" });

  async function requisitar(url, opcoes = {}) {
    setErro(null);
    setMensagem(null);
    try {
      const resposta = await fetch(url, opcoes);
      const dados = await resposta.json().catch(() => null);
      if (!resposta.ok) {
        setErro(`${resposta.status}: ${dados?.mensagem ?? "Erro na operação"}`);
        return null;
      }
      return dados;
    } catch {
      setErro("Não foi possível conectar à API.");
      return null;
    }
  }

  async function carregarEmprestimos() {
    const dados = await requisitar(`${API}/emprestimos`);
    if (dados) setEmprestimos(dados);
  }

  useEffect(() => {
    carregarEmprestimos();
  }, []);

  async function cadastrarUsuario(e) {
    e.preventDefault();
    const dados = await requisitar(`${API}/usuarios`, {
      method: "POST",
      headers: JSON_HEADERS,
      body: JSON.stringify(novoUsuario),
    });
    if (dados) {
      setNovoUsuario({ nome: "", email: "" });
      setMensagem(`Usuário cadastrado com id ${dados.id}.`);
    }
  }

  async function cadastrarLivro(e) {
    e.preventDefault();
    const dados = await requisitar(`${API}/livros`, {
      method: "POST",
      headers: JSON_HEADERS,
      body: JSON.stringify(novoLivro),
    });
    if (dados) {
      setNovoLivro({ titulo: "", autor: "", isbn: "" });
      setMensagem(`Livro cadastrado com id ${dados.id}.`);
    }
  }

  async function emprestar(e) {
    e.preventDefault();
    const dados = await requisitar(`${API}/emprestimos`, {
      method: "POST",
      headers: JSON_HEADERS,
      body: JSON.stringify({
        usuarioId: Number(novoEmprestimo.usuarioId),
        livroId: Number(novoEmprestimo.livroId),
      }),
    });
    if (dados) {
      await carregarEmprestimos();
      setMensagem(`Empréstimo ${dados.id} criado.`);
    }
  }

  async function devolver(id) {
    const dados = await requisitar(`${API}/emprestimos/${id}/devolucao`, {
      method: "PATCH",
    });
    if (dados) {
      await carregarEmprestimos();
      setMensagem(`Empréstimo ${id} devolvido.`);
    }
  }

  return (
      <div style={{ fontFamily: "sans-serif", maxWidth: 900, margin: "40px auto" }}>
        <h1>Biblioteca</h1>

        {erro && <p style={{ color: "red" }}>Erro {erro}</p>}
        {mensagem && <p style={{ color: "green" }}>{mensagem}</p>}

        <section>
          <h2>Cadastrar usuário</h2>
          <form onSubmit={cadastrarUsuario}>
            <input
                placeholder="Nome"
                value={novoUsuario.nome}
                onChange={(e) => setNovoUsuario({ ...novoUsuario, nome: e.target.value })}
            />{" "}
            <input
                placeholder="E-mail"
                value={novoUsuario.email}
                onChange={(e) => setNovoUsuario({ ...novoUsuario, email: e.target.value })}
            />{" "}
            <button type="submit">Cadastrar</button>
          </form>
        </section>

        <section style={{ marginTop: 24 }}>
          <h2>Cadastrar livro</h2>
          <form onSubmit={cadastrarLivro}>
            <input
                placeholder="Título"
                value={novoLivro.titulo}
                onChange={(e) => setNovoLivro({ ...novoLivro, titulo: e.target.value })}
            />{" "}
            <input
                placeholder="Autor"
                value={novoLivro.autor}
                onChange={(e) => setNovoLivro({ ...novoLivro, autor: e.target.value })}
            />{" "}
            <input
                placeholder="ISBN"
                value={novoLivro.isbn}
                onChange={(e) => setNovoLivro({ ...novoLivro, isbn: e.target.value })}
            />{" "}
            <button type="submit">Cadastrar</button>
          </form>
        </section>

        <section style={{ marginTop: 24 }}>
          <h2>Novo empréstimo</h2>
          <form onSubmit={emprestar}>
            <input
                placeholder="ID do usuário"
                value={novoEmprestimo.usuarioId}
                onChange={(e) => setNovoEmprestimo({ ...novoEmprestimo, usuarioId: e.target.value })}
            />{" "}
            <input
                placeholder="ID do livro"
                value={novoEmprestimo.livroId}
                onChange={(e) => setNovoEmprestimo({ ...novoEmprestimo, livroId: e.target.value })}
            />{" "}
            <button type="submit">Emprestar</button>
          </form>
        </section>

        <section style={{ marginTop: 24 }}>
          <h2>Empréstimos</h2>
          {emprestimos.length === 0 ? (
              <p>Nenhum empréstimo.</p>
          ) : (
              <table border="1" cellPadding="6" style={{ width: "100%" }}>
                <thead>
                <tr>
                  <th>ID</th>
                  <th>Usuário</th>
                  <th>Livro</th>
                  <th>Situação</th>
                  <th>Data</th>
                  <th>Devolução</th>
                  <th>Ação</th>
                </tr>
                </thead>
                <tbody>
                {emprestimos.map((e) => (
                    <tr key={e.id}>
                      <td>{e.id}</td>
                      <td>{e.usuarioId}</td>
                      <td>{e.livroId}</td>
                      <td>{e.situacao}</td>
                      <td>{e.dataEmprestimo}</td>
                      <td>{e.dataDevolucao ?? "-"}</td>
                      <td>
                        {e.situacao === "ATIVO" && (
                            <button onClick={() => devolver(e.id)}>Devolver</button>
                        )}
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
          )}
        </section>
      </div>
  );
}