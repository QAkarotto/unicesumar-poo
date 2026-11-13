// Cores das capas: fundo e texto, todas da paleta (cream / navy / flame)
const CORES = [
  ['#142850', '#fbf7ec'],
  ['#c9481a', '#ffffff'],
  ['#f3ecda', '#142850'],
  ['#0e1b38', '#e25822'],
  ['#1d3566', '#fbf7ec'],
  ['#e25822', '#0e1b38'],
];

function estilo(id) {
  const [fundo, texto] = CORES[id % CORES.length];
  return { '--capa': fundo, '--capa-texto': texto };
}

export function Capa({ livro }) {
  return (
    <div className="capa" style={estilo(livro.id)} aria-hidden="true">
      <span className="capa-titulo">{livro.titulo}</span>
      <span className="capa-autor">{livro.autor}</span>
    </div>
  );
}
