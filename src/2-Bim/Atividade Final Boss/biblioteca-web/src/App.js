import { useCallback, useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AnimateUIProvider } from '@animate-ui/react/provider';
import { TooltipProvider } from '@animate-ui/react/primitives/tooltip';
import { Highlight } from '@animate-ui/react/primitives/effects/highlight';
import { SlidingNumber } from '@animate-ui/react/primitives/texts/sliding-number';
import { api } from './professor/api';
import Logo from './componentes/Logo';
import { Confirmacao } from './componentes/ui';
import Inicio from './paginas/Inicio';
import Estante from './paginas/Estante';
import Sacola from './paginas/Sacola';
import Admin from './paginas/Admin';
import './App.css';
import './Loja.css';

const MENU = [
  ['inicio', 'Início'],
  ['estante', 'Estante'],
  ['sacola', 'Sacola'],
  ['admin', 'Admin'],
];

// rotas por hash (#/estante): sem servidor nem dependência de roteador
function lerRota() {
  const rota = window.location.hash.replace(/^#\/?/, '');
  return MENU.some(([id]) => id === rota) ? rota : 'inicio';
}

function lerSacola() {
  try {
    return JSON.parse(localStorage.getItem('sacola')) || [];
  } catch {
    return [];
  }
}

function App() {
  const [rota, setRota] = useState(lerRota);
  const [livros, setLivros] = useState([]);
  const [leitores, setLeitores] = useState([]);
  const [sacola, setSacola] = useState(lerSacola);
  const [confirmacao, setConfirmacao] = useState(null);

  useEffect(() => {
    const aoMudar = () => setRota(lerRota());
    window.addEventListener('hashchange', aoMudar);
    return () => window.removeEventListener('hashchange', aoMudar);
  }, []);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [rota]);

  useEffect(() => {
    try {
      localStorage.setItem('sacola', JSON.stringify(sacola));
    } catch {
      // sem localStorage a sacola só não sobrevive ao recarregar
    }
  }, [sacola]);

  const carregarLoja = useCallback(async () => {
    try {
      const [listaLivros, listaLeitores] = await Promise.all([api.listarLivros(), api.listarLeitores()]);
      setLivros(listaLivros);
      setLeitores(listaLeitores);
    } catch (e) {
      toast.error(e.message);
    }
  }, []);

  // o admin carrega os próprios dados; a loja recarrega ao trocar de página
  useEffect(() => {
    if (rota !== 'admin') carregarLoja();
  }, [rota, carregarLoja]);

  // Mostra o popup de confirmação; a ação só roda se o usuário clicar em Confirmar
  function confirmar(mensagem, acao) {
    setConfirmacao({ mensagem, acao, aberta: true });
  }

  // mantém a mensagem enquanto o popup sai da tela
  const fecharConfirmacao = () => setConfirmacao((atual) => atual && { ...atual, aberta: false });

  function alternarSacola(id) {
    setSacola((atual) => (atual.includes(id) ? atual.filter((item) => item !== id) : [...atual, id]));
  }

  function tirarDaSacola(ids) {
    setSacola((atual) => atual.filter((item) => !ids.includes(item)));
  }

  return (
    <AnimateUIProvider>
      <TooltipProvider openDelay={400} closeDelay={0}>
        {/* na página inicial o hero traz o próprio menu */}
        {rota !== 'inicio' && rota !== 'admin' && (
          <header className="topo">
            <div className="topo-conteudo">
              <a className="marca" href="#/">
                <Logo className="logo-marca" />
                <span className="marca-nome">Biblioteca Universitária</span>
              </a>
              <nav aria-label="Principal">
                {/* a pílula de fundo desliza até o link sob o cursor */}
                <Highlight as="div" mode="parent" hover className="menu-pilula" containerClassName="menu">
                  {MENU.map(([id, rotulo]) => (
                    <a key={id} href={'#/' + (id === 'inicio' ? '' : id)} aria-current={rota === id ? 'page' : undefined}>
                      {rotulo}
                      {id === 'sacola' && sacola.length > 0 && (
                        <span className="menu-contagem">
                          <SlidingNumber number={sacola.length} />
                        </span>
                      )}
                    </a>
                  ))}
                </Highlight>
              </nav>
            </div>
          </header>
        )}

        <ToastContainer position="top-right" autoClose={3000} hideProgressBar closeButton={false} />

        <Confirmacao
          aberta={Boolean(confirmacao && confirmacao.aberta)}
          mensagem={confirmacao ? confirmacao.mensagem : ''}
          onConfirmar={() => {
            confirmacao.acao();
            fecharConfirmacao();
          }}
          onFechar={fecharConfirmacao}
        />

        {rota === 'inicio' && <Inicio livros={livros} naSacola={sacola.length} />}
        {rota === 'estante' && <Estante livros={livros} sacola={sacola} alternarSacola={alternarSacola} />}
        {rota === 'sacola' && (
          <Sacola
            livros={livros}
            leitores={leitores}
            sacola={sacola}
            alternarSacola={alternarSacola}
            tirarDaSacola={tirarDaSacola}
            recarregar={carregarLoja}
            confirmar={confirmar}
          />
        )}
        {rota === 'admin' && <Admin confirmar={confirmar} />}

        {rota !== 'admin' && (
          <footer className="rodape">
            <div className="rodape-conteudo">
              <span>Biblioteca Universitária</span>
              <span>Empréstimo de até 3 livros por 14 dias</span>
            </div>
          </footer>
        )}
      </TooltipProvider>
    </AnimateUIProvider>
  );
}

export default App;
