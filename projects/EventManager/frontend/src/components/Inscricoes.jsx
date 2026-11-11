import { useEffect, useState } from "react";

import { listarEventos } from "../services/eventoService";
import { listarParticipantes } from "../services/participanteService";

import {
  listarInscricoes,
  realizarInscricao,
  cancelarInscricao
} from "../services/inscricaoService";

export default function Inscricoes() {
  const [eventos, setEventos] = useState([]);
  const [participantes, setParticipantes] = useState([]);
  const [inscricoes, setInscricoes] = useState([]);

  const [eventoId, setEventoId] = useState("");
  const [participanteId, setParticipanteId] = useState("");
  const [mensagem, setMensagem] = useState("");

  async function carregar() {
    try {
      const [dadosEventos, dadosParticipantes, dadosInscricoes] =
        await Promise.all([
          listarEventos(),
          listarParticipantes(),
          listarInscricoes()
        ]);

      setEventos(dadosEventos);
      setParticipantes(dadosParticipantes);
      setInscricoes(dadosInscricoes);
    } catch {
      setMensagem("Erro ao carregar informações.");
    }
  }

  useEffect(() => {
    carregar();
  }, []);

  async function inscrever(e) {
    e.preventDefault();

    try {
      await realizarInscricao(eventoId, participanteId);

      setMensagem("Inscrição realizada com sucesso!");
      setEventoId("");
      setParticipanteId("");

      await carregar();
    } catch (erro) {
      setMensagem(erro.message);
    }
  }

  async function cancelar(id) {
    if (!window.confirm("Cancelar esta inscrição?")) return;

    try {
      await cancelarInscricao(id);
      setMensagem("Inscrição cancelada!");
      await carregar();
    } catch (erro){
      setMensagem(erro.message);
    }
  }

  function vagasDisponiveis(evento) {
    const ocupadas = inscricoes.filter(
      (i) => i.eventoId === evento.id
    ).length;

    return Math.max(0, evento.capacidade - ocupadas);
  }

  return (
    <section>
      <div className="painel">
        <h2>Inscrever participante em evento</h2>

        <form onSubmit={inscrever}>
          <label>Evento</label>

          <select
            value={eventoId}
            onChange={(e) => setEventoId(e.target.value)}
            required
          >
            <option value="">Selecione um evento</option>

            {eventos.map((evento) => (
              <option
                key={evento.id}
                value={evento.id}
                disabled={
                  vagasDisponiveis(evento) === 0 ||
                  new Date(evento.dataHora) <= new Date()
                }
              >
                {evento.nome} — {vagasDisponiveis(evento)} vagas
              </option>
            ))}
          </select>

          <label>Participante</label>

          <select
            value={participanteId}
            onChange={(e) => setParticipanteId(e.target.value)}
            required
          >
            <option value="">Selecione um participante</option>

            {participantes.map((p) => (
              <option key={p.id} value={p.id}>
                {p.nome} — {p.email}
              </option>
            ))}
          </select>

          <button type="submit">Realizar inscrição</button>
        </form>
      </div>

      {mensagem && <p role="status">{mensagem}</p>}

      <h2>Inscrições realizadas</h2>

      <div className="lista-eventos">
        {inscricoes.map((inscricao) => (
          <article className="evento" key={inscricao.id}>
            <h3>{inscricao.eventoNome}</h3>

            <p>
              <strong>Participante:</strong>{" "}
              {inscricao.participanteNome}
            </p>

            <p>
              <strong>Inscrição:</strong>{" "}
              {new Date(inscricao.dataInscricao).toLocaleString("pt-BR")}
            </p>

            <button
              className="excluir"
              onClick={() => cancelar(inscricao.id)}
            >
              Cancelar inscrição
            </button>
          </article>
        ))}
      </div>

      <h2>Vagas disponíveis</h2>

      <div className="lista-eventos">
        {eventos.map((evento) => (
          <article className="evento" key={evento.id}>
            <h3>{evento.nome}</h3>
            <p>Capacidade: {evento.capacidade}</p>
            <p>
              <strong>Vagas disponíveis:</strong>{" "}
              {vagasDisponiveis(evento)}
            </p>
          </article>
        ))}
      </div>
    </section>
  );
}