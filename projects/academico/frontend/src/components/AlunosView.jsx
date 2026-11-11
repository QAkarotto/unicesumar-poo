import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function AlunosView({ onSelectAluno, setFeedback }) {
  const [alunos, setAlunos] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [filtro, setFiltro] = useState('');
  const [form, setForm] = useState({
    registroAcademico: '',
    nome: '',
    email: '',
  });

  const carregarAlunos = async () => {
    try {
      setCarregando(true);
      const dados = await api.listarAlunos();
      setAlunos(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    } finally {
      setCarregando(false);
    }
  };

  useEffect(() => {
    carregarAlunos();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.registroAcademico || !form.nome || !form.email) {
      setFeedback({ tipo: 'erro', mensagem: 'Preencha todos os campos do aluno.' });
      return;
    }

    try {
      await api.cadastrarAluno(form);
      setFeedback({ tipo: 'sucesso', mensagem: `Aluno ${form.nome} (${form.registroAcademico}) cadastrado com sucesso.` });
      setForm({ registroAcademico: '', nome: '', email: '' });
      carregarAlunos();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  const alunosFiltrados = alunos.filter((a) =>
    a.nome.toLowerCase().includes(filtro.toLowerCase()) ||
    a.registroAcademico.toLowerCase().includes(filtro.toLowerCase()) ||
    a.email.toLowerCase().includes(filtro.toLowerCase())
  );

  return (
    <div>
      <div className="card">
        <h2 className="card-title">Cadastrar Novo Aluno</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label" htmlFor="ra">Registro Acadêmico (RA)</label>
              <input
                id="ra"
                type="text"
                name="registroAcademico"
                placeholder="Ex: RA2026100"
                value={form.registroAcademico}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="nome">Nome Completo</label>
              <input
                id="nome"
                type="text"
                name="nome"
                placeholder="Ex: João Silva"
                value={form.nome}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="email">E-mail Institucional</label>
              <input
                id="email"
                type="email"
                name="email"
                placeholder="Ex: joao.silva@unicesumar.edu.br"
                value={form.email}
                onChange={handleChange}
              />
            </div>
          </div>
          <button type="submit" className="btn-primary">
            Salvar Aluno
          </button>
        </form>
      </div>

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <h2 className="card-title" style={{ marginBottom: 0, borderBottom: 'none', paddingBottom: 0 }}>
            Alunos Cadastrados ({alunosFiltrados.length})
          </h2>
          <input
            type="text"
            placeholder="Filtrar por RA, nome ou e-mail..."
            value={filtro}
            onChange={(e) => setFiltro(e.target.value)}
            style={{ width: 280 }}
          />
        </div>

        {carregando ? (
          <div className="empty-state">Carregando registros de alunos...</div>
        ) : alunosFiltrados.length === 0 ? (
          <div className="empty-state">Nenhum aluno encontrado.</div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>RA</th>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {alunosFiltrados.map((aluno) => (
                  <tr key={aluno.registroAcademico}>
                    <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{aluno.registroAcademico}</td>
                    <td>{aluno.nome}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{aluno.email}</td>
                    <td>
                      <button
                        type="button"
                        className="btn-secondary btn-action"
                        onClick={() => onSelectAluno && onSelectAluno(aluno.registroAcademico)}
                      >
                        Ver Matrículas
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
