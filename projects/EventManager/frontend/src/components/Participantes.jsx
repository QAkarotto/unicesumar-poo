import { useEffect, useState } from "react";

import {
  listarParticipantes,
  cadastrarParticipante,
  atualizarParticipante,
  excluirParticipante
} from "../services/participanteService";

export default function Participantes() {
  const [participantes, setParticipantes] = useState([]);
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [editandoId, setEditandoId] = useState(null);
  const [mensagem, setMensagem] = useState("");

  async function carregar() {
    try {
      setParticipantes(await listarParticipantes());
    } catch {
      setMensagem("Erro ao carregar participantes.");
    }
  }

  useEffect(() => {
    carregar();
  }, []);

  async function salvar(e) {
    e.preventDefault();

    try {
      const dados = { nome, email };

      if (editandoId !== null) {
        await atualizarParticipante(editandoId, dados);
        setMensagem("Participante atualizado!");
      } else {
        await cadastrarParticipante(dados);
        setMensagem("Participante cadastrado!");
      }

      setNome("");
      setEmail("");
      setEditandoId(null);
      await carregar();
    } catch (erro) {
    setMensagem(erro.message);
    }
  }

  function editar(participante) {
    setNome(participante.nome);
    setEmail(participante.email);
    setEditandoId(participante.id);
  }

  async function excluir(id) {
    if (!window.confirm("Excluir este participante?")) return;

    try {
      await excluirParticipante(id);
      setMensagem("Participante excluído!");
      await carregar();
    } catch (erro) {
        setMensagem(erro.message);
    }
  }

  return (
    <section>
      <div className="painel">
        <h2>
          {editandoId !== null
            ? "Editar participante"
            : "Cadastrar participante"}
        </h2>

        <form onSubmit={salvar}>
          <label>Nome</label>
          <input
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />

          <label>E-mail</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <div className="acoes">
            <button type="submit">
              {editandoId !== null ? "Salvar alterações" : "Cadastrar"}
            </button>

            {editandoId !== null && (
              <button
                type="button"
                className="secundario"
                onClick={() => {
                  setEditandoId(null);
                  setNome("");
                  setEmail("");
                }}
              >
                Cancelar
              </button>
            )}
          </div>
        </form>
      </div>

      {mensagem && <p role="status">{mensagem}</p>}

      <h2>Participantes cadastrados</h2>

      <div className="lista-eventos">
        {participantes.map((p) => (
          <article className="evento" key={p.id}>
            <h3>{p.nome}</h3>
            <p>{p.email}</p>

            <div className="acoes">
              <button onClick={() => editar(p)}>Editar</button>
              <button
                className="excluir"
                onClick={() => excluir(p.id)}
              >
                Excluir
              </button>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}