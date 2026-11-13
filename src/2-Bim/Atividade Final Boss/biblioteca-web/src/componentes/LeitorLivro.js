import { useEffect, useRef, useState } from 'react';
import { Root as Dialog, DialogPortal, DialogOverlay, DialogContent, DialogTitle } from '@animate-ui/react/primitives/radix-ui/dialog';
import Livro3D from './Livro3D';
import { Capa } from './Livro';
import { Botao } from './ui';

const ALTURA = 360;

// o PDF de cada livro fica em public/livros/<isbn>.pdf
function urlDoPdf(livro) {
  return process.env.PUBLIC_URL + '/livros/' + encodeURIComponent(livro.isbn) + '.pdf';
}

async function renderizar(pdf, numero, larguraPx) {
  const pagina = await pdf.getPage(numero);
  const viewport = pagina.getViewport({ scale: larguraPx / pagina.getViewport({ scale: 1 }).width });
  const canvas = document.createElement('canvas');
  canvas.width = Math.floor(viewport.width);
  canvas.height = Math.floor(viewport.height);
  await pagina.render({ canvasContext: canvas.getContext('2d'), viewport }).promise;
  return canvas.toDataURL('image/jpeg', 0.92);
}

function tamanhoDaTela() {
  return { w: window.innerWidth, h: window.innerHeight };
}

function LeitorLivro({ livro, onFechar }) {
  const [aberto, setAberto] = useState(false);
  const [indice, setIndice] = useState(0);
  const [zoom, setZoom] = useState(false);
  const [pdf, setPdf] = useState(null);
  const [estado, setEstado] = useState('');
  const [proporcao, setProporcao] = useState(2 / 3);
  const [imagens, setImagens] = useState({});
  const [tela, setTela] = useState(tamanhoDaTela);
  const pedidas = useRef(new Set());

  const largura = Math.round(ALTURA * proporcao);
  // escala que faz as duas páginas abertas caberem na tela (no zoom pode passar de 1)
  const cabe = Math.min((tela.w - 48) / (2 * largura), (tela.h - 190) / ALTURA);
  const escala = zoom ? Math.max(0.4, cabe) : Math.min(1, cabe);
  const totalPaginas = pdf ? pdf.numPages : 0;
  const totalFolhas = pdf ? Math.ceil((totalPaginas - 1) / 2) : 1;

  function alternarLivro() {
    if (aberto) setIndice(0);
    setAberto(!aberto);
  }

  function anterior() {
    if (indice > 0) setIndice(indice - 1);
    else if (aberto) alternarLivro();
  }

  function proxima() {
    if (!aberto) setAberto(true);
    else if (indice < totalFolhas) setIndice(indice + 1);
  }

  async function lerPdf(fonte) {
    setEstado('carregando');
    try {
      const pdfjs = await import('pdfjs-dist');
      pdfjs.GlobalWorkerOptions.workerSrc = new URL('pdfjs-dist/build/pdf.worker.min.js', import.meta.url).toString();
      const documento = await pdfjs.getDocument(fonte).promise;
      const primeira = (await documento.getPage(1)).getViewport({ scale: 1 });
      pedidas.current = new Set();
      setImagens({});
      setProporcao(primeira.width / primeira.height);
      setIndice(0);
      setAberto(false);
      setPdf(documento);
      setEstado('pronto');
    } catch {
      setEstado('sem-pdf');
    }
  }

  function ler() {
    setZoom(true);
    if (!pdf) lerPdf(urlDoPdf(livro));
  }

  async function escolherArquivo(e) {
    const arquivo = e.target.files[0];
    if (arquivo) lerPdf({ data: await arquivo.arrayBuffer() });
  }

  // libera o PDF anterior ao trocar de arquivo ou fechar o leitor
  useEffect(() => () => pdf && pdf.destroy(), [pdf]);

  // renderiza só a capa e as páginas das folhas perto da aberta
  useEffect(() => {
    if (!pdf) return;
    const precisa = [1];
    for (let k = Math.max(0, indice - 2); k <= Math.min(totalFolhas - 1, indice + 2); k++) precisa.push(2 + 2 * k, 3 + 2 * k);
    const faltam = precisa.filter((n) => n <= pdf.numPages && !pedidas.current.has(n));
    if (faltam.length === 0) return;
    faltam.forEach((n) => pedidas.current.add(n));
    // resolução do tamanho com zoom (as páginas só são renderizadas uma vez) e 1.5x extra por causa da transformação 3D
    const larguraPx = Math.min(2400, largura * Math.max(1, cabe) * Math.max(1.5, window.devicePixelRatio || 1));
    (async () => {
      for (const n of faltam) {
        try {
          const url = await renderizar(pdf, n, larguraPx);
          setImagens((atual) => ({ ...atual, [n]: url }));
        } catch {
          return; // PDF fechado no meio da renderização
        }
      }
    })();
  }, [pdf, indice, totalFolhas, largura, cabe]);

  useEffect(() => {
    const aoRedimensionar = () => setTela(tamanhoDaTela());
    window.addEventListener('resize', aoRedimensionar);
    return () => window.removeEventListener('resize', aoRedimensionar);
  }, []);

  useEffect(() => {
    // Esc fecha pelo próprio Dialog
    function teclas(e) {
      if (e.key === 'ArrowRight') proxima();
      if (e.key === 'ArrowLeft') anterior();
    }
    window.addEventListener('keydown', teclas);
    return () => window.removeEventListener('keydown', teclas);
  });

  function pagina(n) {
    if (n > totalPaginas) return <div className="l3d-papel" />;
    if (!imagens[n]) return <div className="l3d-papel l3d-carregando" />;
    return <img src={imagens[n]} alt={'Página ' + n} draggable="false" />;
  }

  function folha(k) {
    if (!pdf) {
      return {
        frente: (
          <div className="l3d-papel l3d-ficha">
            <h3>{livro.titulo}</h3>
            <p>{livro.autor}</p>
            <p className="l3d-isbn">ISBN {livro.isbn}</p>
          </div>
        ),
        verso: <div className="l3d-papel" />,
      };
    }
    return { frente: pagina(2 + 2 * k), verso: pagina(3 + 2 * k) };
  }

  let rotulo = 'Capa';
  if (aberto) {
    const esquerda = indice === 0 ? null : 2 * indice + 1;
    const direita = 2 + 2 * indice <= totalPaginas ? 2 + 2 * indice : null;
    if (esquerda && direita) rotulo = `Páginas ${esquerda} e ${direita} de ${totalPaginas}`;
    else if (esquerda || direita) rotulo = `Página ${esquerda || direita} de ${totalPaginas}`;
    else rotulo = 'Contracapa';
  }

  const capa = pdf && imagens[1] ? <img src={imagens[1]} alt={'Capa de ' + livro.titulo} draggable="false" /> : <Capa livro={livro} />;

  return (
    <Dialog open onOpenChange={(abre) => !abre && onFechar()}>
      <DialogPortal>
        <DialogOverlay className="leitor-fundo" />
        <DialogContent className="leitor" aria-describedby={undefined}>
      <div className="leitor-topo">
        <div>
          <DialogTitle asChild>
            <h2>{livro.titulo}</h2>
          </DialogTitle>
          <p>{livro.autor}</p>
        </div>
        <div className="acoes">
          {zoom ? (
            <Botao className="secundario" onClick={() => setZoom(false)}>
              Reduzir
            </Botao>
          ) : (
            <Botao onClick={ler}>Ler o livro</Botao>
          )}
          <Botao className="secundario" onClick={onFechar}>
            Fechar
          </Botao>
        </div>
      </div>

      <div className="leitor-palco">
        <Livro3D
          largura={largura}
          altura={ALTURA}
          capa={capa}
          totalFolhas={totalFolhas}
          folha={folha}
          aberto={aberto}
          indice={indice}
          onAlternar={alternarLivro}
          onIndice={setIndice}
          plano={zoom}
          escala={escala}
        />
      </div>

      <div className="leitor-rodape">
        {estado === 'carregando' && <p>Carregando o PDF...</p>}
        {estado === 'sem-pdf' && (
          <>
            <p>Este livro ainda não tem PDF na biblioteca.</p>
            <label className="botao secundario">
              Abrir um PDF do computador
              <input type="file" accept="application/pdf" hidden onChange={escolherArquivo} />
            </label>
          </>
        )}
        {estado === 'pronto' && zoom && (
          <>
            <Botao className="secundario pequeno" onClick={anterior} disabled={!aberto}>
              Anterior
            </Botao>
            <p className="leitor-paginas">{rotulo}</p>
            <Botao className="secundario pequeno" onClick={proxima} disabled={aberto && indice >= totalFolhas}>
              Próxima
            </Botao>
          </>
        )}
        {!zoom && estado !== 'carregando' && estado !== 'sem-pdf' && <p>Clique na capa para abrir e nas páginas para folhear.</p>}
      </div>
        </DialogContent>
      </DialogPortal>
    </Dialog>
  );
}

export default LeitorLivro;
