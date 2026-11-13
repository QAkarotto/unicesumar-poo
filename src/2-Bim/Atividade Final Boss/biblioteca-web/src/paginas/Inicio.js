import { Effect } from '@animate-ui/react/primitives/effects/effect';
import { SplittingText } from '@animate-ui/react/primitives/texts/splitting';
import { Capa } from '../componentes/Livro';
import Logo from '../componentes/Logo';
import HeroLivros from '../componentes/HeroLivros';

const PASSOS = [
  ['Escolha na estante', 'Navegue pelo acervo e encontre o livro pelo título ou pelo autor.'],
  ['Coloque na sacola', 'Junte até 3 livros. A sacola fica salva enquanto você navega.'],
  ['Finalize e retire', 'Confirme o empréstimo com seu nome e retire os livros no balcão.'],
];

// entrada das seções ao rolar: sobe um pouco e sai do desfoque, em cascata curta
const ENTRADA = { inView: true, inViewMargin: '-10% 0px', fade: true, slide: { offset: 18 }, blur: { initialBlur: 6 } };

function Inicio({ livros, naSacola }) {
  return (
    <main>
      <section className="hero">
        <nav className="hero-nav" aria-label="Principal">
          <a className="hero-logo" href="#/">
            <Logo className="logo-hero" />
            <span>
              <strong>Biblioteca</strong>
              <small>universitária</small>
            </span>
          </a>
          <div className="hero-menu">
            <a href="#/estante">Estante</a>
            <a href="#/sacola">Sacola{naSacola > 0 && ` (${naSacola})`}</a>
            <a href="#como-funciona">Como funciona</a>
          </div>
          <div className="hero-lado">
            <a href="#/admin">Admin</a>
            <a className="hero-pilula" href="#/estante">
              Ver a estante
            </a>
          </div>
        </nav>
        <h1>
          <SplittingText
            type="chars"
            text="Biblioteca"
            initial={{ opacity: 0, y: '0.18em', filter: 'blur(14px)' }}
            animate={{ opacity: 1, y: 0, filter: 'blur(0px)' }}
            transition={{ duration: 0.9, ease: [0.23, 1, 0.32, 1] }}
            stagger={0.035}
            delay={150}
          />
        </h1>
        <div className="hero-links">
          <a href="#/estante">Acervo completo</a>
          <a href="#como-funciona">Como funciona</a>
          <a href="#na-estante">Na estante agora</a>
          <a href="#/sacola">Minha sacola</a>
          <span>Até 3 livros por vez</span>
          <span>14 dias para devolver</span>
        </div>
        <HeroLivros />
      </section>

      <section className="secao" id="como-funciona">
        <Effect {...ENTRADA}>
          <h2>Como funciona o empréstimo</h2>
        </Effect>
        <ol className="passos">
          {PASSOS.map(([titulo, texto], i) => (
            <li key={titulo}>
              <Effect className="passo" {...ENTRADA} delay={i * 70}>
                <span className="passo-numero">{i + 1}</span>
                <h3>{titulo}</h3>
                <p>{texto}</p>
              </Effect>
            </li>
          ))}
        </ol>
      </section>

      {livros.length > 0 && (
        <section className="secao" id="na-estante">
          <Effect className="secao-topo" {...ENTRADA}>
            <h2>Na estante agora</h2>
            <a className="link" href="#/estante">
              Ver todos os livros
            </a>
          </Effect>
          <ul className="estante estante-destaque">
            {livros.slice(0, 4).map((livro, i) => (
              <li key={livro.id}>
                <Effect className="livro" {...ENTRADA} delay={i * 60}>
                  <a className="livro-prateleira" href="#/estante">
                    <Capa livro={livro} />
                  </a>
                  <h3>{livro.titulo}</h3>
                  <p>{livro.autor}</p>
                </Effect>
              </li>
            ))}
          </ul>
        </section>
      )}
    </main>
  );
}

export default Inicio;
