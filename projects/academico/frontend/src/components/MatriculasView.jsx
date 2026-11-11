import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function MatriculasView({ filtroAlunoRa, onLimparFiltroAluno, setFeedback }) {
  const [matriculas, setMatriculas] = useState([]);
  const [alunos, setAlunos] = useState([]);
  const [ofertas, setOfertas] = useState([]);
  const [carregando, setCarregando] = useState(false);

  // Form de nova matrícula
  const [novoAlunoRa, setNovoAlunoRa] = useState('');
  const [novaOfertaId, setNovaOfertaId] = useState('');

  // Modal de conclusão
  const [modalConcluir, setModalConcluir] = useState({
    aberto: false,
    matricula: null,
    resultado: 'APROVADO',
  });

  const carregarDados = async () => {
    try {
      setCarregando(true);
      const [listaMatriculas, listaAlunos, listaOfertas] = await Promise.all([
        api.listarMatriculas(filtroAlunoRa),
        api.listarAlunos(),
        api.listarOfertas(),
      ]);

      setMatriculas(listaMatriculas);
      setAlunos(listaAlunos);
      setOfertas(listaOfertas);

      if (listaAlunos.length > 0 && !novoAlunoRa) {
        setNovoAlunoRa(listaAlunos[0].registroAcademico);
      }
      if (listaOfertas.length > 0 && !novaOfertaId) {
        setNovaOfertaId(listaOfertas[0].codigo || listaOfertas[0].id);
      }
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    } finally {
      setCarregando(false);
    }
  };

  useEffect(() => {
    carregarDados();
  }, [filtroAlunoRa]);

  const handleMatricular = async (e) => {
    e.preventDefault();
    if (!novoAlunoRa || !novaOfertaId) {
      setFeedback({ tipo: 'erro', mensagem: 'Selecione um aluno e uma oferta válida.' });
      return;
    }

    try {
      const nova = await api.matricular({
        registroAcademico: novoAlunoRa,
        ofertaId: novaOfertaId,
      });
      setFeedback({
        tipo: 'sucesso',
        mensagem: `Matrícula ${nova.codigo || nova.id} realizada com sucesso para o aluno ${nova.alunoNome}.`
      });
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const handleConcluir = async () => {
    if (!modalConcluir.matricula) return;

    try {
      await api.concluirMatricula(modalConcluir.matricula.id, modalConcluir.resultado);
      setFeedback({
        tipo: 'sucesso',
        mensagem: `Matrícula concluída com resultado: ${modalConcluir.resultado}.`
      });
      setModalConcluir({ aberto: false, matricula: null, resultado: 'APROVADO' });
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const handleTrancar = async (matricula) => {
    try {
      await api.trancarMatricula(matricula.id);
      setFeedback({
        tipo: 'sucesso',
        mensagem: `Matrícula ${matricula.codigo || matricula.id} trancada com sucesso.`
      });
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const handleCancelar = async (matricula) => {
    try {
      await api.cancelarMatricula(matricula.id);
      setFeedback({
        tipo: 'sucesso',
        mensagem: `Matrícula ${matricula.codigo || matricula.id} cancelada com sucesso.`
      });
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const handleExcluir = async (matricula) => {
    if (!window.confirm(`Deseja realmente excluir o registro da matrícula ${matricula.codigo || matricula.id}?`)) {
      return;
    }

    try {
      await api.excluirMatricula(matricula.id);
      setFeedback({
        tipo: 'sucesso',
        mensagem: `Registro de matrícula ${matricula.codigo || matricula.id} excluído com sucesso.`
      });
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const getBadgeClassSituacao = (situacao) => {
    switch (situacao) {
      case 'ATIVA': return 'badge-ativa';
      case 'CONCLUIDA': return 'badge-concluida';
      case 'TRANCADA': return 'badge-trancada';
      case 'CANCELADA': return 'badge-cancelada';
      default: return 'badge-pendente';
    }
  };

  const getBadgeClassResultado = (resultado) => {
    switch (resultado) {
      case 'APROVADO': return 'badge-aprovado';
      case 'REPROVADO': return 'badge-reprovado';
      default: return 'badge-pendente';
    }
  };

  return (
    <div>
      <div className="card">
        <h2 className="card-title">Realizar Nova Matrícula</h2>
        <form onSubmit={handleMatricular}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label" htmlFor="selectAluno">Aluno</label>
              <select
                id="selectAluno"
                value={novoAlunoRa}
                onChange={(e) => setNovoAlunoRa(e.target.value)}
              >
                {alunos.map((a) => (
                  <option key={a.registroAcademico} value={a.registroAcademico}>
                    {a.registroAcademico} - {a.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="selectOferta">Oferta de Disciplina</label>
              <select
                id="selectOferta"
                value={novaOfertaId}
                onChange={(e) => setNovaOfertaId(e.target.value)}
              >
                {ofertas.map((o) => (
                  <option key={o.id || o.codigo} value={o.codigo || o.id}>
                    {o.codigo} - {o.disciplinaNome}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <button type="submit" className="btn-primary">
            Efetuar Matrícula
          </button>
        </form>
      </div>

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <div>
            <h2 className="card-title" style={{ marginBottom: 0, borderBottom: 'none', paddingBottom: 0 }}>
              Matrículas Registradas ({matriculas.length})
            </h2>
            {filtroAlunoRa && (
              <span style={{ fontSize: 12, color: 'var(--text-secondary)' }}>
                Filtrando por RA: {filtroAlunoRa}
              </span>
            )}
          </div>
          {filtroAlunoRa && (
            <button
              type="button"
              className="btn-secondary btn-action"
              onClick={onLimparFiltroAluno}
            >
              Limpar Filtro de RA
            </button>
          )}
        </div>

        {carregando ? (
          <div className="empty-state">Carregando matrículas...</div>
        ) : matriculas.length === 0 ? (
          <div className="empty-state">Nenhuma matrícula registrada para este critério.</div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Aluno</th>
                  <th>Oferta / Disciplina</th>
                  <th>Situação</th>
                  <th>Resultado</th>
                  <th>Ações de Ciclo de Vida</th>
                </tr>
              </thead>
              <tbody>
                {matriculas.map((m) => {
                  const isAtiva = m.situacao === 'ATIVA';
                  return (
                    <tr key={m.id || m.codigo}>
                      <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{m.codigo}</td>
                      <td>
                        <div style={{ fontWeight: 500 }}>{m.alunoNome}</div>
                        <div style={{ fontSize: 11, color: 'var(--text-secondary)', fontFamily: 'monospace' }}>
                          {m.alunoRa}
                        </div>
                      </td>
                      <td>
                        <div style={{ fontWeight: 500 }}>{m.disciplinaNome}</div>
                        <div style={{ fontSize: 11, color: 'var(--text-secondary)', fontFamily: 'monospace' }}>
                          Oferta: {m.ofertaCodigo}
                        </div>
                      </td>
                      <td>
                        <span className={`badge ${getBadgeClassSituacao(m.situacao)}`}>
                          {m.situacao}
                        </span>
                      </td>
                      <td>
                        <span className={`badge ${getBadgeClassResultado(m.resultado)}`}>
                          {m.resultado || 'PENDENTE'}
                        </span>
                      </td>
                      <td>
                        <div className="actions-cell">
                          {isAtiva && (
                            <>
                              <button
                                type="button"
                                className="btn-primary btn-action"
                                onClick={() => setModalConcluir({ aberto: true, matricula: m, resultado: 'APROVADO' })}
                              >
                                Concluir
                              </button>
                              <button
                                type="button"
                                className="btn-secondary btn-action"
                                onClick={() => handleTrancar(m)}
                              >
                                Trancar
                              </button>
                              <button
                                type="button"
                                className="btn-secondary btn-action"
                                onClick={() => handleCancelar(m)}
                              >
                                Cancelar
                              </button>
                            </>
                          )}
                          <button
                            type="button"
                            className="btn-danger btn-action"
                            onClick={() => handleExcluir(m)}
                          >
                            Excluir
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {modalConcluir.aberto && (
        <div className="modal-backdrop">
          <div className="modal-content">
            <h3 className="modal-title">Concluir Matrícula</h3>
            <p style={{ marginBottom: 16, color: 'var(--text-secondary)', fontSize: 13 }}>
              Aluno: <strong style={{ color: 'var(--text-main)' }}>{modalConcluir.matricula?.alunoNome}</strong><br />
              Oferta: <strong style={{ color: 'var(--text-main)' }}>{modalConcluir.matricula?.disciplinaNome}</strong> ({modalConcluir.matricula?.ofertaCodigo})
            </p>

            <div className="form-group" style={{ marginBottom: 20 }}>
              <label className="form-label">Resultado Final:</label>
              <select
                value={modalConcluir.resultado}
                onChange={(e) => setModalConcluir({ ...modalConcluir, resultado: e.target.value })}
              >
                <option value="APROVADO">Aprovado</option>
                <option value="REPROVADO">Reprovado</option>
              </select>
            </div>

            <div className="modal-actions">
              <button
                type="button"
                className="btn-secondary"
                onClick={() => setModalConcluir({ aberto: false, matricula: null, resultado: 'APROVADO' })}
              >
                Cancelar Operação
              </button>
              <button
                type="button"
                className="btn-primary"
                onClick={handleConcluir}
              >
                Confirmar Conclusão
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
