// Adaptado do Book3D da RareUI (MIT, (c) 2025 Swapnil Kalambe): https://github.com/Codewithswappy/RareUI
// Portado para CSS puro (o projeto não usa Tailwind nem framer-motion), abre por clique
// e cada folha tem frente e verso, para mostrar duas páginas do PDF por vez.

const JANELA = 2; // só as folhas perto da aberta ficam no DOM, para PDFs longos

function Livro3D({ largura, altura, capa, totalFolhas, folha, aberto, indice, onAlternar, onIndice, plano, escala = 1 }) {
  const inicio = Math.max(0, indice - JANELA);
  const fim = Math.min(totalFolhas - 1, indice + JANELA);
  const visiveis = [];
  for (let k = inicio; k <= fim; k++) visiveis.push(k);

  let corpo = 'none';
  if (plano) {
    // origem na lombada: fechado centraliza a capa, aberto centraliza as duas páginas
    corpo = aberto
      ? `translateX(${largura / 2}px) scale(${escala})`
      : `translateX(${(-largura * (escala - 1)) / 2}px) scale(${escala})`;
  } else if (aberto) {
    corpo = `translateX(${largura / 2}px) scale(${escala}) translateZ(40px) rotateX(12deg) rotateY(-20deg)`;
  }

  return (
    <div className="l3d" style={{ width: largura, height: altura }}>
      <div className="l3d-corpo" style={{ transform: corpo }}>
        <div className="l3d-contracapa" />

        {visiveis.map((k) => {
          const virada = k < indice;
          const z = (virada ? k - inicio + 1 : fim - k + 1) * 1.5;
          const { frente, verso } = folha(k);
          return (
            <div
              key={k}
              className="l3d-folha"
              style={{
                zIndex: virada ? k + 1 : 1000 - k,
                // -178 dá a inclinação de livro na prévia; no zoom -180 deixa a página reta e nítida
                transform: `translateZ(${z}px) rotateY(${virada ? (plano ? -180 : -178) : 0}deg)`,
                transitionDelay: virada ? '0ms' : (k - inicio) * 50 + 'ms',
              }}
              onClick={() => {
                if (!aberto) return;
                if (virada) onIndice(k);
                else if (k === indice) onIndice(k + 1);
              }}
            >
              <div className="l3d-face l3d-frente">{frente}</div>
              <div className="l3d-face l3d-verso">{verso}</div>
            </div>
          );
        })}

        <div
          className="l3d-capa"
          style={{
            transform: aberto ? 'translateZ(0) rotateY(-180deg)' : `translateZ(${(fim - inicio + 2) * 1.5 + 2}px)`,
            transitionDelay: aberto ? '0ms' : '500ms',
          }}
          onClick={onAlternar}
        >
          <div className="l3d-face l3d-capa-fora">{capa}</div>
          <div className="l3d-face l3d-capa-dentro">
            <span>Biblioteca Universitária</span>
          </div>
        </div>
      </div>
      <div className={aberto ? 'l3d-sombra aberta' : 'l3d-sombra'} />
    </div>
  );
}

export default Livro3D;
