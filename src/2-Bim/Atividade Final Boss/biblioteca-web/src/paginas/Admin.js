import { Fragment, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import {
  Tabs,
  TabsList,
  TabsHighlight,
  TabsHighlightItem,
  TabsTrigger,
  TabsContents,
  TabsContent,
} from '@animate-ui/react/primitives/tabs';
import * as Sidebar from '@animate-ui/react/primitives/sidebar';
import { Effects } from '@animate-ui/react/primitives/effects/effect';
import { AutoHeight } from '@animate-ui/react/primitives/effects/auto-height';
import { CountingNumber } from '@animate-ui/react/primitives/texts/counting-number';
import Logo from '../componentes/Logo';
import { Botao, Dica, ModalDemo, Seletor } from '../componentes/ui';
import { api } from '../professor/api';

const LEITOR_VAZIO = { nome: '', email: '' };
const LIVRO_VAZIO = { isbn: '', titulo: '', autor: '', quantidadeTotal: 1 };
const EMPRESTIMO_VAZIO = { leitorId: '', livroId: '' };

const NOMES = ['Ana', 'Bruno', 'Carla', 'Diego', 'Eduarda', 'Felipe', 'Gabriela', 'Hugo', 'Isabela', 'João', 'Larissa', 'Marcos'];
const SOBRENOMES = ['Silva', 'Souza', 'Lima', 'Costa', 'Pereira', 'Almeida', 'Rocha', 'Gomes', 'Ribeiro', 'Martins'];
const TITULOS = ['O Segredo do', 'A Ilha do', 'Crônicas do', 'O Último', 'Memórias de um', 'O Mistério do', 'A Viagem ao', 'Contos do'];
const TEMAS = ['Farol', 'Bosque', 'Relojoeiro', 'Dragão', 'Navegante', 'Deserto', 'Alquimista', 'Castelo', 'Cometa', 'Jardim'];
const sortear = (lista) => lista[Math.floor(Math.random() * lista.length)];
const inteiro = (min, max) => min + Math.floor(Math.random() * (max - min + 1));

const MARCA = '[DEMO] ';
const ehDemo = (texto) => !!texto && texto.startsWith(MARCA);

// Registros de demonstração ganham um selo para não se confundirem com dados reais
function Nome({ texto }) {
  return ehDemo(texto) ? (
    <>
      <span className="badge demo">DEMO</span> {texto.slice(MARCA.length)}
    </>
  ) : (
    texto
  );
}

// Demo com empréstimo não pode ser excluída pela API (FK): fica oculta aqui no front
const CHAVE_OCULTOS = 'demoOcultos';
function lerOcultos() {
  try {
    return { leitores: [], livros: [], ...JSON.parse(localStorage.getItem(CHAVE_OCULTOS)) };
  } catch {
    return { leitores: [], livros: [] };
  }
}
function salvarOcultos(oculto) {
  try {
    localStorage.setItem(CHAVE_OCULTOS, JSON.stringify(oculto));
  } catch {
    // sem storage: o registro volta a aparecer ao recarregar
  }
}

const PAGINAS = {
  dashboard: ['Visão geral', 'Resumo do acervo e atalhos para os módulos'],
  leitores: ['Leitores', 'Cadastro, edição e exclusão de leitores'],
  livros: ['Livros', 'Gestão do acervo e quantidade de exemplares'],
  emprestimos: ['Empréstimos', 'Registrar empréstimos e devoluções'],
};

// traços (stroke), sem preenchimento — estilo shadcn / Animate UI
const ICONES = {
  painel: 'M3 3h7v7H3V3zm11 0h7v7h-7V3zM3 14h7v7H3v-7zm11 0h7v7h-7v-7z',
  pessoas: 'M16 11a3 3 0 1 0-3-3 3 3 0 0 0 3 3zM8 11a3 3 0 1 0-3-3 3 3 0 0 0 3 3zM2 20v-1a4 4 0 0 1 4-4h4a4 4 0 0 1 4 4v1M14 15h3a4 4 0 0 1 4 4v1',
  livro: 'M4 19.5A2.5 2.5 0 0 1 6.5 17H20M4 4.5A2.5 2.5 0 0 1 6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15z',
  troca: 'M16 3l4 4-4 4M20 7H4M8 21l-4-4 4-4M4 17h16',
  painelLateral: 'M4 5h16a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1zM9 5v14',
  chevron: 'M9 6l6 6-6 6',
  setas: 'M7 9l5-5 5 5M7 15l5 5 5-5',
  estante: 'M4 4h16M4 20h16M4 4v16M20 4v16M10 8v8M14 8v8',
  sacola: 'M6 8V6a3 3 0 0 1 3-3h6a3 3 0 0 1 3 3v2M4 8h16l-1 12H5L4 8z',
};

function Icone({ nome }) {
  return (
    <svg className="lateral-icone" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path
        d={ICONES[nome]}
        stroke="currentColor"
        strokeWidth="1.75"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

function LinhaVazia({ colunas, texto }) {
  return (
    <tr className="vazia">
      <td colSpan={colunas}>{texto}</td>
    </tr>
  );
}

function Admin({ confirmar }) {
  const [aba, setAba] = useState('dashboard');
  const [aberta, setAberta] = useState(true);
  const [demoAberto, setDemoAberto] = useState(false);

  const [leitores, setLeitores] = useState([]);
  const [livros, setLivros] = useState([]);
  const [emprestimos, setEmprestimos] = useState([]);

  const [leitorForm, setLeitorForm] = useState(LEITOR_VAZIO);
  const [leitorEditando, setLeitorEditando] = useState(null);
  const [livroForm, setLivroForm] = useState(LIVRO_VAZIO);
  const [livroEditando, setLivroEditando] = useState(null);
  const [emprestimoForm, setEmprestimoForm] = useState(EMPRESTIMO_VAZIO);
  const [dicas, setDicas] = useState({});

  function obrigatorio(chave, mensagem) {
    return {
      onInvalid: (e) => {
        e.preventDefault();
        if (e.target.form.querySelector(':invalid') === e.target) e.target.focus();
        setDicas((atual) => ({ ...atual, [chave]: mensagem }));
      },
      'data-dica': chave,
    };
  }

  async function carregarTudo() {
    try {
      const oculto = lerOcultos();
      const L = (await api.listarLeitores()).filter((l) => !(oculto.leitores.includes(l.id) && ehDemo(l.nome)));
      const V = (await api.listarLivros()).filter((v) => !(oculto.livros.includes(v.id) && ehDemo(v.titulo)));
      const E = (await api.listarEmprestimos()).filter(
        (e) =>
          !((oculto.leitores.includes(e.leitorId) || oculto.livros.includes(e.livroId)) &&
            (ehDemo(e.leitorNome) || ehDemo(e.livroTitulo))),
      );
      setLeitores(L);
      setLivros(V);
      setEmprestimos(E);
    } catch (e) {
      toast.error(e.message);
    }
  }

  useEffect(() => {
    carregarTudo();
  }, []);

  async function executar(acao, mensagem) {
    try {
      await acao();
      toast.success(mensagem);
      await carregarTudo();
    } catch (e) {
      toast.error(e.message);
    }
  }

  // Demo: cria leitores, livros, empréstimos e devoluções aleatórios pela própria API
  async function gerarDemo() {
    const sufixo = Date.now().toString(36);
    const novosLeitores = [];
    const novosLivros = [];
    for (let i = 0; i < 5; i++) {
      const nome = sortear(NOMES) + ' ' + sortear(SOBRENOMES);
      const email = nome.toLowerCase().replace(/[^a-z]/g, '.') + '.' + sufixo + i + '@demo.com';
      novosLeitores.push(await api.criarLeitor({ nome: MARCA + nome, email }));
    }
    for (let i = 0; i < 8; i++) {
      novosLivros.push(
        await api.criarLivro({
          isbn: 'DEMO-' + sufixo + i,
          titulo: MARCA + sortear(TITULOS) + ' ' + sortear(TEMAS),
          autor: sortear(NOMES) + ' ' + sortear(SOBRENOMES),
          quantidadeTotal: inteiro(1, 4),
        }),
      );
    }
    for (let i = 0; i < 10; i++) {
      try {
        const emp = await api.criarEmprestimo({ leitorId: sortear(novosLeitores).id, livroId: sortear(novosLivros).id });
        if (Math.random() < 0.4) await api.devolverEmprestimo(emp.id);
      } catch {
        // sem exemplar disponível ou regra de negócio: pula este sorteio
      }
    }
  }

  // Exclui pela API o que der; o que o banco recusar (tem empréstimo) fica oculto no front
  async function apagarDemo() {
    const demoLeitores = leitores.filter((l) => ehDemo(l.nome));
    const demoLivros = livros.filter((l) => ehDemo(l.titulo));
    if (demoLeitores.length + demoLivros.length === 0) throw new Error('Não há dados de demonstração.');
    const oculto = lerOcultos();
    for (const l of demoLeitores) {
      try {
        await api.excluirLeitor(l.id);
      } catch {
        oculto.leitores.push(l.id);
      }
    }
    for (const l of demoLivros) {
      try {
        await api.excluirLivro(l.id);
      } catch {
        oculto.livros.push(l.id);
      }
    }
    salvarOcultos(oculto);
  }

  function limparDica(e) {
    const chave = e.target.dataset.dica;
    if (chave) setDicas((atual) => ({ ...atual, [chave]: '' }));
  }

  function trocarAba(nova) {
    setDicas({});
    setAba(nova);
  }

  function cancelarLeitor() {
    setDicas({});
    setLeitorEditando(null);
    setLeitorForm(LEITOR_VAZIO);
  }

  function cancelarLivro() {
    setDicas({});
    setLivroEditando(null);
    setLivroForm(LIVRO_VAZIO);
  }

  function salvarLeitor(event) {
    event.preventDefault();
    const verbo = leitorEditando ? 'atualizar' : 'cadastrar';
    confirmar('Deseja mesmo ' + verbo + ' este leitor?', () =>
      executar(async () => {
        if (leitorEditando) {
          await api.atualizarLeitor(leitorEditando, leitorForm);
        } else {
          await api.criarLeitor(leitorForm);
        }
        cancelarLeitor();
      }, 'Leitor salvo.'),
    );
  }

  function salvarLivro(event) {
    event.preventDefault();
    const verbo = livroEditando ? 'atualizar' : 'cadastrar';
    confirmar('Deseja mesmo ' + verbo + ' este livro?', () =>
      executar(async () => {
        if (livroEditando) {
          await api.atualizarLivro(livroEditando, { titulo: livroForm.titulo, autor: livroForm.autor });
        } else {
          await api.criarLivro({ ...livroForm, quantidadeTotal: Number(livroForm.quantidadeTotal) });
        }
        cancelarLivro();
      }, 'Livro salvo.'),
    );
  }

  function salvarEmprestimo(event) {
    event.preventDefault();
    if (!emprestimoForm.leitorId) return setDicas({ leitorId: 'Selecione um leitor.' });
    if (!emprestimoForm.livroId) return setDicas({ livroId: 'Selecione um livro.' });
    confirmar('Deseja mesmo realizar este empréstimo?', () =>
      executar(async () => {
        await api.criarEmprestimo({
          leitorId: Number(emprestimoForm.leitorId),
          livroId: Number(emprestimoForm.livroId),
        });
        setEmprestimoForm(EMPRESTIMO_VAZIO);
      }, 'Empréstimo realizado.'),
    );
  }

  function escolherEmprestimo(campo, valor) {
    setDicas({});
    setEmprestimoForm({ ...emprestimoForm, [campo]: valor });
  }

  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);

  function estaAtrasado(item) {
    if (item.situacao !== 'ATIVO' || !item.dataPrevistaDevolucao) return false;
    return new Date(item.dataPrevistaDevolucao + 'T00:00:00') < hoje;
  }

  const listaAtivos = emprestimos.filter((item) => item.situacao === 'ATIVO');
  const listaAtrasados = listaAtivos.filter(estaAtrasado);
  const emprestimosAtivos = listaAtivos.length;
  const exemplaresDisponiveis = livros.reduce((soma, livro) => soma + livro.quantidadeDisponivel, 0);
  const exemplaresTotal = livros.reduce((soma, livro) => soma + livro.quantidadeTotal, 0);
  const livrosEsgotados = livros.filter((livro) => livro.quantidadeDisponivel === 0);
  const livrosBaixoEstoque = livros.filter((livro) => livro.quantidadeDisponivel > 0 && livro.quantidadeDisponivel <= 1);
  const recentes = [...emprestimos].slice(-5).reverse();
  const [tituloPagina, subtituloPagina] = PAGINAS[aba];
  const recolhida = !aberta;

  function comDica(rotulo, no) {
    return recolhida ? <Dica texto={rotulo}>{no}</Dica> : no;
  }

  function itemMenu(id, rotulo, icone, extraClass = '') {
    const botao = (
      <TabsTrigger value={id} className={'lateral-item ' + extraClass} data-sidebar="menu-button">
        {icone && <Icone nome={icone} />}
        <span className="lateral-texto">{rotulo}</span>
      </TabsTrigger>
    );
    return (
      <TabsHighlightItem key={id} value={id}>
        {comDica(rotulo, botao)}
      </TabsHighlightItem>
    );
  }

  function alternarSidebar() {
    setAberta((atual) => !atual);
  }

  return (
    <Sidebar.Provider open={aberta} onOpenChange={setAberta} className="admin-shell" style={{ '--sidebar-width': '16rem', '--sidebar-width-icon': '3.5rem' }}>
      <Tabs value={aba} onValueChange={trocarAba} orientation="vertical" className={recolhida ? 'admin recolhida' : 'admin'}>
        <aside className="lateral" aria-label="Painel administrativo" data-collapsible={recolhida ? 'icon' : undefined}>
          <div className="lateral-cabecalho">
            {comDica(
              'Biblioteca',
              <a className="lateral-equipe" href="#/" title="Biblioteca">
                <span className="lateral-logo">
                  <Logo className="logo-marca" />
                </span>
                <span className="lateral-equipe-texto lateral-texto">
                  <strong>Biblioteca</strong>
                  <small>Admin</small>
                </span>
                <svg className="lateral-setas lateral-texto" viewBox="0 0 24 24" aria-hidden="true">
                  <path d={ICONES.setas} fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
              </a>,
            )}
            {comDica(
              recolhida ? 'Expandir menu' : 'Recolher menu',
              <button
                type="button"
                className="lateral-alternar"
                aria-label={recolhida ? 'Expandir menu' : 'Recolher menu'}
                aria-expanded={aberta}
                onClick={alternarSidebar}
              >
                <Icone nome="painelLateral" />
              </button>,
            )}
          </div>

          <Sidebar.Highlight className="lateral-destaque">
            <TabsHighlight className="lateral-pilula">
              <TabsList className="lateral-menu">
                <div className="lateral-grupo">
                  <span className="lateral-rotulo" data-sidebar="group-label">
                    Plataforma
                  </span>
                  {itemMenu('dashboard', 'Visão geral', 'painel')}

                  <div className="lateral-fold">
                    <div className="lateral-fold-btn" aria-hidden="true">
                      <Icone nome="livro" />
                      <span className="lateral-texto">Acervo</span>
                    </div>
                    <div className="lateral-sub">
                      {itemMenu('livros', 'Livros', null, 'lateral-subitem')}
                      {itemMenu('leitores', 'Leitores', null, 'lateral-subitem')}
                    </div>
                  </div>

                  {itemMenu('emprestimos', 'Empréstimos', 'troca')}
                </div>

                <div className="lateral-grupo">
                  <span className="lateral-rotulo" data-sidebar="group-label">
                    Loja
                  </span>
                  {comDica(
                    'Estante',
                    <a className="lateral-item" href="#/estante" data-sidebar="menu-button">
                      <Icone nome="estante" />
                      <span className="lateral-texto">Estante</span>
                    </a>,
                  )}
                  {comDica(
                    'Sacola',
                    <a className="lateral-item" href="#/sacola" data-sidebar="menu-button">
                      <Icone nome="sacola" />
                      <span className="lateral-texto">Sacola</span>
                    </a>,
                  )}
                </div>
              </TabsList>
            </TabsHighlight>
          </Sidebar.Highlight>

          {comDica(
            'Voltar à loja',
            <a className="lateral-usuario" href="#/estante">
              <span className="lateral-avatar">AD</span>
              <span className="lateral-usuario-texto lateral-texto">
                <strong>Admin</strong>
                <small>Voltar à loja</small>
              </span>
              <svg className="lateral-setas lateral-texto" viewBox="0 0 24 24" aria-hidden="true">
                <path d={ICONES.setas} fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </a>,
          )}
        </aside>

        <main className="admin-conteudo">
          <div className="admin-topo">
            <div>
              <h1>{tituloPagina}</h1>
              <p>{subtituloPagina}</p>
            </div>
            {aba === 'dashboard' && (
              <div className="admin-atalhos">
                <Botao className="secundario pequeno" onClick={() => setDemoAberto(true)}>
                  Gerar demo
                </Botao>
                <ModalDemo
                  aberta={demoAberto}
                  onFechar={() => setDemoAberto(false)}
                  onGerar={() => {
                    setDemoAberto(false);
                    executar(gerarDemo, 'Dados de demonstração gerados.');
                  }}
                  onApagar={() => {
                    setDemoAberto(false);
                    confirmar('Apagar todos os dados de demonstração?', () => executar(apagarDemo, 'Dados de demonstração apagados.'));
                  }}
                />
                <Botao className="secundario pequeno" onClick={() => trocarAba('livros')}>
                  Ir para livros
                </Botao>
                <Botao className="pequeno" onClick={() => trocarAba('emprestimos')}>
                  Novo empréstimo
                </Botao>
              </div>
            )}
          </div>

          <TabsContents>
            <TabsContent value="dashboard">
              <div className="resumo">
                <Effects className="stat" fade slide={{ offset: 14 }} holdDelay={50}>
                  <Fragment key="leitores">
                    <CountingNumber className="stat-numero" number={leitores.length} />
                    <span className="stat-rotulo">Leitores</span>
                  </Fragment>
                  <Fragment key="livros">
                    <CountingNumber className="stat-numero" number={livros.length} />
                    <span className="stat-rotulo">Títulos no acervo</span>
                  </Fragment>
                  <Fragment key="exemplares">
                    <CountingNumber className="stat-numero" number={exemplaresDisponiveis} />
                    <span className="stat-rotulo">
                      Disponíveis de {exemplaresTotal}
                    </span>
                  </Fragment>
                  <Fragment key="ativos">
                    <CountingNumber className="stat-numero" number={emprestimosAtivos} />
                    <span className="stat-rotulo">Empréstimos ativos</span>
                  </Fragment>
                  <Fragment key="atrasados">
                    <CountingNumber className={'stat-numero' + (listaAtrasados.length ? ' alerta' : '')} number={listaAtrasados.length} />
                    <span className="stat-rotulo">Atrasados</span>
                  </Fragment>
                  <Fragment key="esgotados">
                    <CountingNumber className="stat-numero" number={livrosEsgotados.length} />
                    <span className="stat-rotulo">Sem exemplares</span>
                  </Fragment>
                </Effects>
              </div>

              <div className="admin-modulos">
                <button type="button" className="admin-modulo" onClick={() => trocarAba('livros')}>
                  <strong>Livros</strong>
                  <span>
                    {livros.length} títulos · {exemplaresDisponiveis} disponíveis agora
                  </span>
                </button>
                <button type="button" className="admin-modulo" onClick={() => trocarAba('leitores')}>
                  <strong>Leitores</strong>
                  <span>
                    {leitores.length} cadastrados · limite de 3 empréstimos ativos por pessoa
                  </span>
                </button>
                <button type="button" className="admin-modulo" onClick={() => trocarAba('emprestimos')}>
                  <strong>Empréstimos</strong>
                  <span>
                    {emprestimosAtivos} ativos
                    {listaAtrasados.length ? ` · ${listaAtrasados.length} atrasado(s)` : ' · nenhum atraso'}
                  </span>
                </button>
              </div>

              <div className="admin-dash">
                <div className="card">
                  <div className="card-topo">
                    <h2>Empréstimos ativos</h2>
                    <span className="contagem">{emprestimosAtivos}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>Leitor</th>
                          <th>Livro</th>
                          <th>Previsto</th>
                          <th>Status</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody>
                        {listaAtivos.length === 0 && (
                          <LinhaVazia colunas={5} texto="Nenhum empréstimo ativo no momento." />
                        )}
                        {listaAtivos.map((item) => {
                          const atrasado = estaAtrasado(item);
                          return (
                            <tr key={item.id}>
                              <td className="forte"><Nome texto={item.leitorNome} /></td>
                              <td><Nome texto={item.livroTitulo} /></td>
                              <td className="mono">{item.dataPrevistaDevolucao}</td>
                              <td>
                                <span className={'badge ' + (atrasado ? 'atrasado' : 'ativo')}>
                                  {atrasado ? 'ATRASADO' : item.situacao}
                                </span>
                              </td>
                              <td>
                                <Botao
                                  className="pequeno"
                                  onClick={() =>
                                    confirmar('Deseja mesmo registrar a devolução?', () =>
                                      executar(() => api.devolverEmprestimo(item.id), 'Devolução registrada.'),
                                    )
                                  }
                                >
                                  Devolver
                                </Botao>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                </div>

                <div className="card">
                  <div className="card-topo">
                    <h2>Acervo crítico</h2>
                    <span className="contagem">{livrosEsgotados.length + livrosBaixoEstoque.length}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>Título</th>
                          <th>Autor</th>
                          <th>Disp.</th>
                          <th>Total</th>
                        </tr>
                      </thead>
                      <tbody>
                        {livrosEsgotados.length === 0 && livrosBaixoEstoque.length === 0 && (
                          <LinhaVazia colunas={4} texto="Nenhum título em estoque crítico." />
                        )}
                        {[...livrosEsgotados, ...livrosBaixoEstoque].map((livro) => (
                          <tr key={livro.id}>
                            <td className="forte"><Nome texto={livro.titulo} /></td>
                            <td>{livro.autor}</td>
                            <td className="mono">{livro.quantidadeDisponivel}</td>
                            <td className="mono">{livro.quantidadeTotal}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>

                <div className="card admin-dash-largo">
                  <div className="card-topo">
                    <h2>Atividade recente</h2>
                    <span className="contagem">{recentes.length}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Leitor</th>
                          <th>Livro</th>
                          <th>Empréstimo</th>
                          <th>Previsto</th>
                          <th>Devolução</th>
                          <th>Situação</th>
                        </tr>
                      </thead>
                      <tbody>
                        {recentes.length === 0 && (
                          <LinhaVazia colunas={7} texto="Ainda não há movimentação registrada." />
                        )}
                        {recentes.map((item) => (
                          <tr key={item.id}>
                            <td className="mono">{item.id}</td>
                            <td className="forte"><Nome texto={item.leitorNome} /></td>
                            <td><Nome texto={item.livroTitulo} /></td>
                            <td className="mono">{item.dataEmprestimo}</td>
                            <td className="mono">{item.dataPrevistaDevolucao}</td>
                            <td className="mono">{item.dataDevolucao || '-'}</td>
                            <td>
                              <span
                                className={
                                  'badge ' +
                                  (estaAtrasado(item) ? 'atrasado' : String(item.situacao).toLowerCase())
                                }
                              >
                                {estaAtrasado(item) ? 'ATRASADO' : item.situacao}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </TabsContent>

            <TabsContent value="leitores">
              <section className="painel">
                <div className={leitorEditando ? 'card card-form editando' : 'card card-form'}>
                  <h2>{leitorEditando ? 'Editar leitor' : 'Novo leitor'}</h2>
                  <AutoHeight deps={[leitorEditando, dicas]}>
                    <form onSubmit={salvarLeitor} onChange={limparDica} className="form">
                      <label>
                        Nome
                        <input
                          value={leitorForm.nome}
                          onChange={(e) => setLeitorForm({ ...leitorForm, nome: e.target.value })}
                          required
                          {...obrigatorio('leitorNome', 'Informe o nome do leitor.')}
                        />
                        {dicas.leitorNome && <span className="dica">{dicas.leitorNome}</span>}
                      </label>
                      <label>
                        E-mail
                        <input
                          type="email"
                          value={leitorForm.email}
                          onChange={(e) => setLeitorForm({ ...leitorForm, email: e.target.value })}
                          required
                          {...obrigatorio('email', 'Informe um e-mail válido (ex: nome@email.com).')}
                        />
                        {dicas.email && <span className="dica">{dicas.email}</span>}
                      </label>
                      <div className="acoes">
                        <Botao type="submit">{leitorEditando ? 'Atualizar' : 'Cadastrar'}</Botao>
                        {leitorEditando && (
                          <Botao
                            className="secundario"
                            onClick={() => confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLeitor)}
                          >
                            Cancelar
                          </Botao>
                        )}
                      </div>
                    </form>
                  </AutoHeight>
                </div>

                <div className="card">
                  <div className="card-topo">
                    <h2>Lista de leitores</h2>
                    <span className="contagem">{leitores.length}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Nome</th>
                          <th>E-mail</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody>
                        {leitores.length === 0 && <LinhaVazia colunas={4} texto="Nenhum leitor cadastrado ainda." />}
                        {leitores.map((leitor) => (
                          <tr key={leitor.id}>
                            <td className="mono">{leitor.id}</td>
                            <td className="forte"><Nome texto={leitor.nome} /></td>
                            <td>{leitor.email}</td>
                            <td className="acoes-linha">
                              <Botao
                                className="secundario pequeno"
                                onClick={() => {
                                  setDicas({});
                                  setLeitorEditando(leitor.id);
                                  setLeitorForm({ nome: leitor.nome, email: leitor.email });
                                }}
                              >
                                Editar
                              </Botao>
                              <Botao
                                className="perigo pequeno"
                                onClick={() =>
                                  confirmar('Deseja mesmo excluir este leitor?', () =>
                                    executar(() => api.excluirLeitor(leitor.id), 'Leitor excluído.'),
                                  )
                                }
                              >
                                Excluir
                              </Botao>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </section>
            </TabsContent>

            <TabsContent value="livros">
              <section className="painel">
                <div className={livroEditando ? 'card card-form editando' : 'card card-form'}>
                  <h2>{livroEditando ? 'Editar livro' : 'Novo livro'}</h2>
                  <AutoHeight deps={[livroEditando, dicas]}>
                    <form onSubmit={salvarLivro} onChange={limparDica} className="form">
                      <label>
                        ISBN
                        <input
                          value={livroForm.isbn}
                          onChange={(e) => setLivroForm({ ...livroForm, isbn: e.target.value })}
                          required
                          {...obrigatorio('isbn', 'Informe o ISBN do livro.')}
                          disabled={livroEditando !== null}
                        />
                        {dicas.isbn && <span className="dica">{dicas.isbn}</span>}
                      </label>
                      <label>
                        Título
                        <input
                          value={livroForm.titulo}
                          onChange={(e) => setLivroForm({ ...livroForm, titulo: e.target.value })}
                          required
                          {...obrigatorio('titulo', 'Informe o título do livro.')}
                        />
                        {dicas.titulo && <span className="dica">{dicas.titulo}</span>}
                      </label>
                      <label>
                        Autor
                        <input
                          value={livroForm.autor}
                          onChange={(e) => setLivroForm({ ...livroForm, autor: e.target.value })}
                          required
                          {...obrigatorio('autor', 'Informe o autor do livro.')}
                        />
                        {dicas.autor && <span className="dica">{dicas.autor}</span>}
                      </label>
                      {!livroEditando && (
                        <label>
                          Quantidade total
                          <input
                            type="number"
                            min="1"
                            value={livroForm.quantidadeTotal}
                            onChange={(e) => setLivroForm({ ...livroForm, quantidadeTotal: e.target.value })}
                            required
                            {...obrigatorio('quantidade', 'Informe a quantidade (mínimo 1).')}
                          />
                          {dicas.quantidade && <span className="dica">{dicas.quantidade}</span>}
                        </label>
                      )}
                      <div className="acoes">
                        <Botao type="submit">{livroEditando ? 'Atualizar' : 'Cadastrar'}</Botao>
                        {livroEditando && (
                          <Botao
                            className="secundario"
                            onClick={() => confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLivro)}
                          >
                            Cancelar
                          </Botao>
                        )}
                      </div>
                    </form>
                  </AutoHeight>
                </div>

                <div className="card">
                  <div className="card-topo">
                    <h2>Acervo</h2>
                    <span className="contagem">{livros.length}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>ISBN</th>
                          <th>Título</th>
                          <th>Autor</th>
                          <th>Disponíveis</th>
                          <th>Total</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody>
                        {livros.length === 0 && <LinhaVazia colunas={7} texto="Nenhum livro no acervo ainda." />}
                        {livros.map((livro) => (
                          <tr key={livro.id}>
                            <td className="mono">{livro.id}</td>
                            <td className="mono">{livro.isbn}</td>
                            <td className="forte"><Nome texto={livro.titulo} /></td>
                            <td>{livro.autor}</td>
                            <td className="mono">{livro.quantidadeDisponivel}</td>
                            <td className="mono">{livro.quantidadeTotal}</td>
                            <td className="acoes-linha">
                              <Botao
                                className="secundario pequeno"
                                onClick={() => {
                                  setDicas({});
                                  setLivroEditando(livro.id);
                                  setLivroForm(livro);
                                }}
                              >
                                Editar
                              </Botao>
                              <Botao
                                className="perigo pequeno"
                                onClick={() =>
                                  confirmar('Deseja mesmo excluir este livro?', () =>
                                    executar(() => api.excluirLivro(livro.id), 'Livro excluído.'),
                                  )
                                }
                              >
                                Excluir
                              </Botao>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </section>
            </TabsContent>

            <TabsContent value="emprestimos">
              <section className="painel">
                <div className="card card-form">
                  <h2>Novo empréstimo</h2>
                  <AutoHeight deps={[dicas]}>
                    <form onSubmit={salvarEmprestimo} className="form">
                      <label>
                        Leitor
                        <Seletor
                          value={emprestimoForm.leitorId}
                          onValueChange={(valor) => escolherEmprestimo('leitorId', valor)}
                          placeholder="Selecione"
                          opcoes={leitores.map((leitor) => [leitor.id, leitor.nome])}
                          aria-label="Leitor"
                        />
                        {dicas.leitorId && <span className="dica">{dicas.leitorId}</span>}
                      </label>
                      <label>
                        Livro
                        <Seletor
                          value={emprestimoForm.livroId}
                          onValueChange={(valor) => escolherEmprestimo('livroId', valor)}
                          placeholder="Selecione"
                          opcoes={livros.map((livro) => [livro.id, `${livro.titulo} (${livro.quantidadeDisponivel} disp.)`])}
                          aria-label="Livro"
                        />
                        {dicas.livroId && <span className="dica">{dicas.livroId}</span>}
                      </label>
                      <div className="acoes">
                        <Botao type="submit">Emprestar</Botao>
                      </div>
                    </form>
                  </AutoHeight>
                </div>

                <div className="card">
                  <div className="card-topo">
                    <h2>Histórico</h2>
                    <span className="contagem">{emprestimos.length}</span>
                  </div>
                  <div className="tabela">
                    <table>
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Leitor</th>
                          <th>Livro</th>
                          <th>Empréstimo</th>
                          <th>Previsto</th>
                          <th>Devolução</th>
                          <th>Situação</th>
                          <th>Ações</th>
                        </tr>
                      </thead>
                      <tbody>
                        {emprestimos.length === 0 && <LinhaVazia colunas={8} texto="Nenhum empréstimo registrado ainda." />}
                        {emprestimos.map((item) => (
                          <tr key={item.id}>
                            <td className="mono">{item.id}</td>
                            <td className="forte"><Nome texto={item.leitorNome} /></td>
                            <td><Nome texto={item.livroTitulo} /></td>
                            <td className="mono">{item.dataEmprestimo}</td>
                            <td className="mono">{item.dataPrevistaDevolucao}</td>
                            <td className="mono">{item.dataDevolucao || '-'}</td>
                            <td>
                              <span className={'badge ' + String(item.situacao).toLowerCase()}>{item.situacao}</span>
                            </td>
                            <td>
                              {item.situacao === 'ATIVO' && (
                                <Botao
                                  className="pequeno"
                                  onClick={() =>
                                    confirmar('Deseja mesmo registrar a devolução?', () =>
                                      executar(() => api.devolverEmprestimo(item.id), 'Devolução registrada.'),
                                    )
                                  }
                                >
                                  Devolver
                                </Botao>
                              )}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </section>
            </TabsContent>
          </TabsContents>
        </main>
      </Tabs>
    </Sidebar.Provider>
  );
}

export default Admin;
