
import { useEffect, useState } from "react";

import Participantes from "./components/Participantes";
import Inscricoes from "./components/Inscricoes";

import {
  listarEventos,
  cadastrarEvento,
  atualizarEvento,
  excluirEvento
} from "./services/eventoService";

import "./App.css";

const formularioInicial = {
  nome: "",
  descricao: "",
  local: "",
  dataHora: "",
  capacidade: ""
};

function App() {
  const [pagina, setPagina] = useState("eventos");

  const [eventos, setEventos] = useState([]);
  const [formulario, setFormulario] = useState(formularioInicial);
  const [editandoId, setEditandoId] = useState(null);
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  async function carregarEventos() {
    try {
      const dados = await listarEventos();
      setEventos(dados);
      setErro("");
    } catch {
      setErro("Não foi possível carregar os eventos.");
    }
  }

  useEffect(() => {
    carregarEventos();
  }, []);

  function alterarCampo(e) {
    const { name, value } = e.target;

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value
    }));
  }

  async function salvarEvento(e) {
    e.preventDefault();

    setMensagem("");
    setErro("");

    if (Number(formulario.capacidade) <= 0) {
      setErro("A capacidade deve ser maior que zero.");
      return;
    }

    const dados = {
      ...formulario,
      capacidade: Number(formulario.capacidade)
    };

    try {
      if (editandoId !== null) {
        await atualizarEvento(editandoId, dados);
        setMensagem("Evento atualizado com sucesso!");
      } else {
        await cadastrarEvento(dados);
        setMensagem("Evento cadastrado com sucesso!");
      }

      setFormulario(formularioInicial);
      setEditandoId(null);
      await carregarEventos();
    } catch (erro){
      setErro(erro.message);
    }
  }

  function editarEvento(evento) {
    setFormulario({
      nome: evento.nome,
      descricao: evento.descricao || "",
      local: evento.local,
      dataHora: evento.dataHora.slice(0, 16),
      capacidade: String(evento.capacidade)
    });

    setEditandoId(evento.id);
    setMensagem("");
    setErro("");

    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  }

  async function removerEvento(id) {
    if (!window.confirm("Deseja excluir este evento?")) {
      return;
    }

    try {
      await excluirEvento(id);
      setMensagem("Evento excluído com sucesso!");
      setErro("");
      await carregarEventos();
    } catch (erro) {
        setErro(erro.message);
    }
  }

  function cancelarEdicao() {
    setFormulario(formularioInicial);
    setEditandoId(null);
  }

  function mudarPagina(novaPagina) {
    setPagina(novaPagina);
    setMensagem("");
    setErro("");

    if (novaPagina === "eventos") {
      carregarEventos();
    }
  }

  return (
    <div className="container">

      <header>
        <h1>Gerenciamento de Eventos</h1>
        <p>Cadastre e organize seus eventos</p>
      </header>

      {/* MENU DE NAVEGAÇÃO */}
      <nav className="menu">
        <button
          type="button"
          onClick={() => mudarPagina("eventos")}
        >
          Eventos
        </button>

        <button
          type="button"
          onClick={() => mudarPagina("participantes")}
        >
          Participantes
        </button>

        <button
          type="button"
          onClick={() => mudarPagina("inscricoes")}
        >
          Inscrições
        </button>
      </nav>

      {/* PÁGINA DE EVENTOS */}
      {pagina === "eventos" && (
        <>
          <section className="painel">
            <h2>
              {editandoId !== null
                ? "Editar evento"
                : "Cadastrar novo evento"}
            </h2>

            <form onSubmit={salvarEvento}>

              <label>Nome do evento</label>
              <input
                name="nome"
                value={formulario.nome}
                onChange={alterarCampo}
                required
                maxLength={150}
              />

              <label>Descrição</label>
              <textarea
                name="descricao"
                value={formulario.descricao}
                onChange={alterarCampo}
                maxLength={500}
              />

              <label>Local</label>
              <input
                name="local"
                value={formulario.local}
                onChange={alterarCampo}
                required
                maxLength={200}
              />

              <label>Data e horário</label>
              <input
                type="datetime-local"
                name="dataHora"
                value={formulario.dataHora}
                onChange={alterarCampo}
                required
              />

              <label>Capacidade</label>
              <input
                type="number"
                name="capacidade"
                value={formulario.capacidade}
                onChange={alterarCampo}
                min="1"
                required
              />

              <div className="acoes">
                <button type="submit">
                  {editandoId !== null
                    ? "Salvar alterações"
                    : "Cadastrar evento"}
                </button>

                {editandoId !== null && (
                  <button
                    type="button"
                    className="secundario"
                    onClick={cancelarEdicao}
                  >
                    Cancelar
                  </button>
                )}
              </div>
            </form>
          </section>

          {mensagem && (
            <p className="sucesso">{mensagem}</p>
          )}

          {erro && (
            <p className="erro">{erro}</p>
          )}

          <section>
            <h2>Eventos cadastrados</h2>

            {eventos.length === 0 ? (
              <p>Nenhum evento cadastrado.</p>
            ) : (
              <div className="lista-eventos">

                {eventos.map((evento) => (
                  <article
                    className="evento"
                    key={evento.id}
                  >
                    <h3>{evento.nome}</h3>

                    <p>{evento.descricao}</p>

                    <p>
                      <strong>Local:</strong>{" "}
                      {evento.local}
                    </p>

                    <p>
                      <strong>Data:</strong>{" "}
                      {new Date(
                        evento.dataHora
                      ).toLocaleString("pt-BR")}
                    </p>

                    <p>
                      <strong>Capacidade:</strong>{" "}
                      {evento.capacidade}
                    </p>

                    <div className="acoes">
                      <button
                        type="button"
                        onClick={() => editarEvento(evento)}
                      >
                        Editar
                      </button>

                      <button
                        type="button"
                        className="excluir"
                        onClick={() => removerEvento(evento.id)}
                      >
                        Excluir
                      </button>
                    </div>

                  </article>
                ))}

              </div>
            )}
          </section>
        </>
      )}

      {/* PÁGINA DE PARTICIPANTES */}
      {pagina === "participantes" && (
        <Participantes />
      )}

      {/* PÁGINA DE INSCRIÇÕES */}
      {pagina === "inscricoes" && (
        <Inscricoes />
      )}

    </div>
  );
}

export default App;
