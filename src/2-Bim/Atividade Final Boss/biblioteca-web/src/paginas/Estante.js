import { useState } from 'react';
import { Capa } from '../componentes/Livro';
import LeitorLivro from '../componentes/LeitorLivro';
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
        <button type="button" className="secundario pequeno" onClick={() => alternarSacola(livro.id)}>
          Tirar da sacola
        </button>
      );
    }
    if (livro.quantidadeDisponivel === 0) {
      return (
        <button type="button" className="pequeno" disabled>
          Indisponível
        </button>
      );
    }
    return (
      <button type="button" className="pequeno" disabled={cheia} onClick={() => alternarSacola(livro.id)}>
        {cheia ? 'Sacola cheia' : 'Pôr na sacola'}
      </button>
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
          {visiveis.map((livro) => (
            <li key={livro.id} className={livro.quantidadeDisponivel === 0 ? 'livro esgotado' : 'livro'}>
              <button
                type="button"
                className="livro-prateleira"
                aria-label={'Abrir ' + livro.titulo}
                onClick={() => setLendo(livro)}
              >
                <Capa livro={livro} />
              </button>
              <h3>{livro.titulo}</h3>
              <p>{livro.autor}</p>
              {botao(livro)}
            </li>
          ))}
        </ul>
      )}

      {lendo && <LeitorLivro livro={lendo} onFechar={() => setLendo(null)} />}
    </main>
  );
}

export default Estante;
