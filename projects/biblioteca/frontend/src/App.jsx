import { useState } from "react";

const API = "http://localhost:8080/api/emprestimos";

export default function App() {
  const [usuarioId, setUsuarioId] = useState("1");
  const [livroId, setLivroId] = useState("1");
  const [emprestimoId, setEmprestimoId] = useState("1");
  const [resultado, setResultado] = useState(null);
  const [erro, setErro] = useState(null);

  async function criarEmprestimo(e) {
    e.preventDefault();
    setErro(null);
    setResultado(null);
    try {
      const resposta = await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          usuarioId: Number(usuarioId),
          livroId: Number(livroId),
        }),
      });
      const dados = await resposta.json();
      if (!resposta.ok) {
        setErro(`${resposta.status}: ${dados.mensagem ?? "Erro"}`);
      } else {
        setResultado(dados);
      }
    } catch (err) {
      setErro("Não foi possível conectar à API.");
    }
  }

  async function devolverEmprestimo() {
    setErro(null);
    setResultado(null);
    try {
      const resposta = await fetch(`${API}/${emprestimoId}/devolucao`, {
        method: "PATCH",
      });
      const dados = await resposta.json();
      if (!resposta.ok) {
        setErro(`${resposta.status}: ${dados.mensagem ?? "Erro"}`);
      } else {
        setResultado(dados);
      }
    } catch (err) {
      setErro("Não foi possível conectar à API.");
    }
  }

  return (
      <div style={{ fontFamily: "sans-serif", maxWidth: 600, margin: "40px auto" }}>
        <h1>Biblioteca - Empréstimos</h1>

        <form onSubmit={criarEmprestimo}>
          <h2>Novo empréstimo</h2>
          <label>
            ID do usuário:{" "}
            <input value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)} />
          </label>{" "}
          <label>
            ID do livro:{" "}
            <input value={livroId} onChange={(e) => setLivroId(e.target.value)} />
          </label>{" "}
          <button type="submit">Emprestar</button>
        </form>

        <div style={{ marginTop: 24 }}>
          <h2>Devolução</h2>
          <label>
            ID do empréstimo:{" "}
            <input value={emprestimoId} onChange={(e) => setEmprestimoId(e.target.value)} />
          </label>{" "}
          <button onClick={devolverEmprestimo}>Devolver</button>
        </div>

        {erro && (
            <p style={{ color: "red", marginTop: 24 }}>Erro {erro}</p>
        )}

        {resultado && (
            <pre style={{ background: "#f4f4f4", padding: 12, marginTop: 24 }}>
          {JSON.stringify(resultado, null, 2)}
        </pre>
        )}
      </div>
  );
}