import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function DisciplinasView({ setFeedback }) {
  const [disciplinas, setDisciplinas] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [form, setForm] = useState({
    codigo: '',
    nome: '',
    cargaHoraria: '',
  });

  const carregarDisciplinas = async () => {
    try {
      setCarregando(true);
      const dados = await api.listarDisciplinas();
      setDisciplinas(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    } finally {
      setCarregando(false);
    }
  };

  useEffect(() => {
    carregarDisciplinas();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.codigo || !form.nome || !form.cargaHoraria) {
      setFeedback({ tipo: 'erro', mensagem: 'Preencha todos os campos da disciplina.' });
      return;
    }

    try {
      await api.cadastrarDisciplina({
        codigo: form.codigo,
        nome: form.nome,
        cargaHoraria: parseInt(form.cargaHoraria, 10),
      });
      setFeedback({ tipo: 'sucesso', mensagem: `Disciplina ${form.nome} (${form.codigo}) cadastrada com sucesso.` });
      setForm({ codigo: '', nome: '', cargaHoraria: '' });
      carregarDisciplinas();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  return (
    <div>
      <div className="card">
        <h2 className="card-title">Cadastrar Nova Disciplina</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label" htmlFor="codigo">Código da Disciplina</label>
              <input
                id="codigo"
                type="text"
                name="codigo"
                placeholder="Ex: ED, REDES, IA"
                value={form.codigo}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="nome">Nome da Disciplina</label>
              <input
                id="nome"
                type="text"
                name="nome"
                placeholder="Ex: Estrutura de Dados"
                value={form.nome}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="cargaHoraria">Carga Horária (Horas)</label>
              <input
                id="cargaHoraria"
                type="number"
                name="cargaHoraria"
                min="1"
                placeholder="Ex: 80"
                value={form.cargaHoraria}
                onChange={handleChange}
              />
            </div>
          </div>
          <button type="submit" className="btn-primary">
            Salvar Disciplina
          </button>
        </form>
      </div>

      <div className="card">
        <h2 className="card-title">Disciplinas Cadastradas ({disciplinas.length})</h2>

        {carregando ? (
          <div className="empty-state">Carregando catálogo de disciplinas...</div>
        ) : disciplinas.length === 0 ? (
          <div className="empty-state">Nenhuma disciplina cadastrada.</div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nome da Disciplina</th>
                  <th>Carga Horária</th>
                </tr>
              </thead>
              <tbody>
                {disciplinas.map((d) => (
                  <tr key={d.id || d.codigo}>
                    <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{d.codigo}</td>
                    <td>{d.nome}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{d.cargaHoraria}h</td>
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
