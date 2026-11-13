import { useState } from 'react';
import { Effect } from '@animate-ui/react/primitives/effects/effect';
import { Tilt } from '@animate-ui/react/primitives/effects/tilt';
import { Capa } from '../componentes/Livro';
import LeitorLivro from '../componentes/LeitorLivro';
import { Botao } from '../componentes/ui';
import { LIMITE_SACOLA } from './Sacola';

function Estante({ livros, sacola, alternarSacola }) {
  const [busca, setBusca] = useState('');
  const [lendo, setLendo] = useState(null);
  const termo = busca.trim().toLowerCase();
  const visiveis = livros.filter(
    (livro) => livro.titulo.toLowerCase().includes(termo) || livro.autor.toLowerCase().includes(termo)
  );
  const cheia = sacola.length >= LIMITE_SACOLA;

  function botao(livro) {
    if (sacola.includes(livro.id)) {
      return (
        <Botao className="secundario pequeno" onClick={() => alternarSacola(livro.id)}>
          Tirar da sacola
        </Botao>
      );
    }
    if (livro.quantidadeDisponivel === 0) {
      return (
        <Botao className="pequeno" disabled>
          Indisponível
        </Botao>
      );
    }
    return (
      <Botao className="pequeno" disabled={cheia} onClick={() => alternarSacola(livro.id)}>
        {cheia ? 'Sacola cheia' : 'Pôr na sacola'}
      </Botao>
    );
  }

  return (
    <main className="app">
      <div className="pagina-topo">
        <div>
          <h1>Estante</h1>
          <p>Escolha até {LIMITE_SACOLA} livros e retire no balcão.</p>
        </div>
        <input
          type="search"
          className="busca"
          placeholder="Buscar por título ou autor"
          aria-label="Buscar por título ou autor"
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
        />
      </div>

      {visiveis.length === 0 ? (
        <p className="vazio">{livros.length === 0 ? 'A estante ainda está vazia.' : 'Nenhum livro encontrado para essa busca.'}</p>
      ) : (
        <ul className="estante">
          {visiveis.map((livro, i) => (
            <li key={livro.id}>
              {/* cascata só nas duas primeiras fileiras; o resto entra junto */}
              <Effect
                className={livro.quantidadeDisponivel === 0 ? 'livro esgotado' : 'livro'}
                fade
                slide={{ offset: 16 }}
                delay={Math.min(i, 9) * 40}
              >
                {/* a capa inclina levemente para o cursor, como um livro pego da prateleira */}
                <div className="livro-prateleira">
                  <Tilt maxTilt={7} perspective={700}>
                    <button type="button" className="livro-capa" aria-label={'Abrir ' + livro.titulo} onClick={() => setLendo(livro)}>
                      <Capa livro={livro} />
                    </button>
                  </Tilt>
                </div>
                <h3>{livro.titulo}</h3>
                <p>{livro.autor}</p>
                {botao(livro)}
              </Effect>
            </li>
          ))}
        </ul>
      )}

      {lendo && <LeitorLivro livro={lendo} onFechar={() => setLendo(null)} />}
    </main>
  );
}

export default Estante;
