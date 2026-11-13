import { useState } from 'react';
import { toast } from 'react-toastify';
import { api } from '../professor/api';
import { Capa } from '../componentes/Livro';

const PRAZO_DIAS = 14; // igual a Emprestimo.PRAZO_DIAS no backend
export const LIMITE_SACOLA = 3; // igual a EmprestimoService.LIMITE_EMPRESTIMOS_ATIVOS

function Sacola({ livros, leitores, sacola, alternarSacola, tirarDaSacola, recarregar, confirmar }) {
  const [leitorId, setLeitorId] = useState('');
  const [enviando, setEnviando] = useState(false);
  const itens = sacola.map((id) => livros.find((livro) => livro.id === id)).filter(Boolean);

  const devolucao = new Date();
  devolucao.setDate(devolucao.getDate() + PRAZO_DIAS);

  // um empréstimo por livro; os que derem certo saem da sacola, os que falharem ficam
  async function finalizar() {
    setEnviando(true);
    const feitos = [];
    for (const livro of itens) {
      try {
        await api.criarEmprestimo({ leitorId: Number(leitorId), livroId: livro.id });
        feitos.push(livro.id);
      } catch (e) {
        toast.error(livro.titulo + ': ' + e.message);
      }
    }
    if (feitos.length) {
      tirarDaSacola(feitos);
      toast.success(feitos.length === 1 ? 'Empréstimo realizado.' : feitos.length + ' empréstimos realizados.');
    }
    setEnviando(false);
    recarregar();
  }

  if (itens.length === 0) {
    return (
      <main className="app">
        <div className="pagina-topo">
          <h1>Sacola</h1>
        </div>
        <div className="sacola-vazia">
          <p>Sua sacola está vazia.</p>
          <a className="botao" href="#/estante">
            Ir para a estante
          </a>
        </div>
      </main>
    );
  }

  return (
    <main className="app">
      <div className="pagina-topo">
        <h1>Sacola</h1>
      </div>

      <div className="sacola">
        <ul className="sacola-itens">
          {itens.map((livro) => (
            <li key={livro.id} className="sacola-item">
              <Capa livro={livro} />
              <div>
                <h3>{livro.titulo}</h3>
                <p>{livro.autor}</p>
              </div>
              <button type="button" className="perigo pequeno" onClick={() => alternarSacola(livro.id)}>
                Remover
              </button>
            </li>
          ))}
        </ul>

        <aside className="card card-form resumo-sacola">
          <h2>Resumo do empréstimo</h2>
          <label>
            Quem vai retirar
            <select value={leitorId} onChange={(e) => setLeitorId(e.target.value)}>
              <option value="">Selecione seu nome</option>
              {leitores.map((leitor) => (
                <option key={leitor.id} value={leitor.id}>
                  {leitor.nome}
                </option>
              ))}
            </select>
          </label>
          <dl>
            <div>
              <dt>Livros</dt>
              <dd>{itens.length}</dd>
            </div>
            <div>
              <dt>Devolver até</dt>
              <dd>{devolucao.toLocaleDateString('pt-BR')}</dd>
            </div>
          </dl>
          <button
            type="button"
            disabled={!leitorId || enviando}
            onClick={() => confirmar('Deseja mesmo realizar este empréstimo?', finalizar)}
          >
            {enviando ? 'Enviando...' : 'Finalizar empréstimo'}
          </button>
        </aside>
      </div>
    </main>
  );
}

export default Sacola;
