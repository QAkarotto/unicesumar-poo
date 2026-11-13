import { useState } from 'react';
import { toast } from 'react-toastify';
import { Effect } from '@animate-ui/react/primitives/effects/effect';
import { SlidingNumber } from '@animate-ui/react/primitives/texts/sliding-number';
import { api } from '../professor/api';
import { Capa } from '../componentes/Livro';
import { Botao, Seletor } from '../componentes/ui';

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
        <Effect className="sacola-vazia" fade slide={{ offset: 12 }}>
          <p>Sua sacola está vazia.</p>
          <Botao asChild>
            <a href="#/estante">Ir para a estante</a>
          </Botao>
        </Effect>
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
          {itens.map((livro, i) => (
            <li key={livro.id}>
              <Effect className="sacola-item" fade slide={{ offset: 12 }} delay={i * 50}>
                <Capa livro={livro} />
                <div>
                  <h3>{livro.titulo}</h3>
                  <p>{livro.autor}</p>
                </div>
                <Botao className="perigo pequeno" onClick={() => alternarSacola(livro.id)}>
                  Remover
                </Botao>
              </Effect>
            </li>
          ))}
        </ul>

        <Effect className="card card-form resumo-sacola" fade slide={{ offset: 12 }} delay={120}>
          <h2>Resumo do empréstimo</h2>
          <label>
            Quem vai retirar
            <Seletor
              value={leitorId}
              onValueChange={setLeitorId}
              placeholder="Selecione seu nome"
              opcoes={leitores.map((leitor) => [leitor.id, leitor.nome])}
              aria-label="Quem vai retirar"
            />
          </label>
          <dl>
            <div>
              <dt>Livros</dt>
              <dd>
                <SlidingNumber number={itens.length} />
              </dd>
            </div>
            <div>
              <dt>Devolver até</dt>
              <dd>{devolucao.toLocaleDateString('pt-BR')}</dd>
            </div>
          </dl>
          <Botao disabled={!leitorId || enviando} onClick={() => confirmar('Deseja mesmo realizar este empréstimo?', finalizar)}>
            {enviando ? 'Enviando...' : 'Finalizar empréstimo'}
          </Botao>
        </Effect>
      </div>
    </main>
  );
}

export default Sacola;
