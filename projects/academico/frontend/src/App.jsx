import React, { useState } from 'react';
import { AlunosView } from './components/AlunosView';
import { DisciplinasView } from './components/DisciplinasView';
import { OfertasView } from './components/OfertasView';
import { MatriculasView } from './components/MatriculasView';

export function App() {
  const [abaAtiva, setAbaAtiva] = useState('matriculas');
  const [filtroAlunoRa, setFiltroAlunoRa] = useState('');
  const [feedback, setFeedback] = useState(null);

  const handleSelectAluno = (ra) => {
    setFiltroAlunoRa(ra);
    setAbaAtiva('matriculas');
  };

  const handleLimparFiltroAluno = () => {
    setFiltroAlunoRa('');
  };

  return (
    <div>
      <header className="header">
        <div className="header-inner">
          <div>
            <div className="brand-title">Sistema Acadêmico</div>
            <div className="brand-subtitle">Gestão Integrada Full Stack - Spring Boot & ReactJS</div>
          </div>
          <div style={{ fontSize: 12, color: 'var(--text-secondary)' }}>
            API REST: <span style={{ color: 'var(--status-active-text)' }}>Conectada (Porta 8080)</span>
          </div>
        </div>
      </header>

      <main className="container">
        {feedback && (
          <div className={`banner ${feedback.tipo === 'erro' ? 'banner-error' : 'banner-success'}`}>
            <span>{feedback.mensagem}</span>
            <button
              type="button"
              className="banner-close"
              onClick={() => setFeedback(null)}
              aria-label="Fechar aviso"
            >
              Fechar
            </button>
          </div>
        )}

        <nav className="nav-tabs">
          <button
            type="button"
            className={`nav-tab ${abaAtiva === 'matriculas' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('matriculas')}
          >
            Matrículas
          </button>
          <button
            type="button"
            className={`nav-tab ${abaAtiva === 'alunos' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('alunos')}
          >
            Alunos
          </button>
          <button
            type="button"
            className={`nav-tab ${abaAtiva === 'disciplinas' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('disciplinas')}
          >
            Disciplinas
          </button>
          <button
            type="button"
            className={`nav-tab ${abaAtiva === 'ofertas' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('ofertas')}
          >
            Ofertas de Disciplinas
          </button>
        </nav>

        {abaAtiva === 'matriculas' && (
          <MatriculasView
            filtroAlunoRa={filtroAlunoRa}
            onLimparFiltroAluno={handleLimparFiltroAluno}
            setFeedback={setFeedback}
          />
        )}

        {abaAtiva === 'alunos' && (
          <AlunosView
            onSelectAluno={handleSelectAluno}
            setFeedback={setFeedback}
          />
        )}

        {abaAtiva === 'disciplinas' && (
          <DisciplinasView
            setFeedback={setFeedback}
          />
        )}

        {abaAtiva === 'ofertas' && (
          <OfertasView
            setFeedback={setFeedback}
          />
        )}
      </main>
    </div>
  );
}

export default App;
