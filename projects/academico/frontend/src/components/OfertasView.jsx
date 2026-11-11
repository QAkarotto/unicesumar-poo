import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function OfertasView({ setFeedback }) {
  const [ofertas, setOfertas] = useState([]);
  const [disciplinas, setDisciplinas] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [form, setForm] = useState({
    codigo: '',
    disciplinaId: '',
    turmaId: 1, // Turma padrão criada pelo seed
  });

  const carregarDados = async () => {
    try {
      setCarregando(true);
      const [listaOfertas, listaDisciplinas] = await Promise.all([
        api.listarOfertas(),
        api.listarDisciplinas(),
      ]);
      setOfertas(listaOfertas);
      setDisciplinas(listaDisciplinas);
      if (listaDisciplinas.length > 0) {
        setForm((prev) => ({ ...prev, disciplinaId: listaDisciplinas[0].id || listaDisciplinas[0].codigo }));
      }
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    } finally {
      setCarregando(false);
    }
  };

  useEffect(() => {
    carregarDados();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.codigo || !form.disciplinaId) {
      setFeedback({ tipo: 'erro', mensagem: 'Preencha o código da oferta e selecione uma disciplina.' });
      return;
    }

    try {
      await api.cadastrarOferta({
        codigo: form.codigo,
        disciplinaId: form.disciplinaId,
        turmaId: 1,
      });
      setFeedback({ tipo: 'sucesso', mensagem: `Oferta ${form.codigo} criada com sucesso.` });
      setForm((prev) => ({ ...prev, codigo: '' }));
      carregarDados();
    } catch (err) {
      setFeedback({ tipo: 'erro', mensagem: err.message });
    }
  };

  return (
    <div>
      <div className="card">
        <h2 className="card-title">Criar Nova Oferta de Disciplina</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label" htmlFor="ofertaCodigo">Código da Oferta</label>
              <input
                id="ofertaCodigo"
                type="text"
                name="codigo"
                placeholder="Ex: ED-2026-2"
                value={form.codigo}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label className="form-label" htmlFor="disciplinaId">Disciplina</label>
              <select
                id="disciplinaId"
                name="disciplinaId"
                value={form.disciplinaId}
                onChange={handleChange}
              >
                {disciplinas.map((d) => (
                  <option key={d.id || d.codigo} value={d.id || d.codigo}>
                    {d.codigo} - {d.nome} ({d.cargaHoraria}h)
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Turma e Período</label>
              <input
                type="text"
                disabled
                value="TURMA-2026-2 (2026 / 2º Semestre)"
                style={{ opacity: 0.7 }}
              />
            </div>
          </div>
          <button type="submit" className="btn-primary">
            Salvar Oferta
          </button>
        </form>
      </div>

      <div className="card">
        <h2 className="card-title">Ofertas Disponíveis ({ofertas.length})</h2>

        {carregando ? (
          <div className="empty-state">Carregando catálogo de ofertas...</div>
        ) : ofertas.length === 0 ? (
          <div className="empty-state">Nenhuma oferta cadastrada.</div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Disciplina</th>
                  <th>Turma / Semestre</th>
                </tr>
              </thead>
              <tbody>
                {ofertas.map((of) => (
                  <tr key={of.id || of.codigo}>
                    <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>{of.codigo}</td>
                    <td>{of.disciplinaNome}</td>
                    <td style={{ color: 'var(--text-secondary)' }}>{of.turmaCodigo || 'TURMA-2026-2'}</td>
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
