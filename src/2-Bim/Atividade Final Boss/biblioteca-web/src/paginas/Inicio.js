import { useEffect } from 'react';
import { Capa } from '../componentes/Livro';
import Logo from '../componentes/Logo';
import HeroLivros from '../componentes/HeroLivros';

const PASSOS = [
  ['Escolha na estante', 'Navegue pelo acervo e encontre o livro pelo título ou pelo autor.'],
  ['Coloque na sacola', 'Junte até 3 livros. A sacola fica salva enquanto você navega.'],
  ['Finalize e retire', 'Confirme o empréstimo com seu nome e retire os livros no balcão.'],
];

function Inicio({ livros, naSacola }) {
  // ao rolar, as seções abaixo do hero descem de cima com blur até ficarem nítidas
  useEffect(() => {
    if (!('IntersectionObserver' in window)) return undefined;
    const alvos = document.querySelectorAll('.secao-topo, .secao > h2, .passo, .estante-destaque .livro');
    const observador = new IntersectionObserver(
      (itens) =>
        itens.forEach((item) => {
          if (!item.isIntersecting) return;
          item.target.classList.add('visivel');
          observador.unobserve(item.target);
        }),
      { threshold: 0.15 },
    );
    alvos.forEach((el) => {
      el.classList.add('revelar');
      // atraso de 70ms por irmão: os 3 passos entram em cascata curta
      el.style.setProperty('--d', Array.from(el.parentElement.children).indexOf(el) * 70 + 'ms');
      observador.observe(el);
    });
    return () => observador.disconnect();
  }, [livros.length]);

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
        <h1>Biblioteca</h1>
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
        <h2>Como funciona o empréstimo</h2>
        <ol className="passos">
          {PASSOS.map(([titulo, texto], i) => (
            <li key={titulo} className="passo">
              <span className="passo-numero">{i + 1}</span>
              <h3>{titulo}</h3>
              <p>{texto}</p>
            </li>
          ))}
        </ol>
      </section>

      {livros.length > 0 && (
        <section className="secao" id="na-estante">
          <div className="secao-topo">
            <h2>Na estante agora</h2>
            <a className="link" href="#/estante">
              Ver todos os livros
            </a>
          </div>
          <ul className="estante estante-destaque">
            {livros.slice(0, 4).map((livro) => (
              <li key={livro.id} className="livro">
                <a className="livro-prateleira" href="#/estante">
                  <Capa livro={livro} />
                </a>
                <h3>{livro.titulo}</h3>
                <p>{livro.autor}</p>
              </li>
            ))}
          </ul>
        </section>
      )}
    </main>
  );
}

export default Inicio;
