import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import Logo from '../componentes/Logo';
import { api } from '../professor/api';

const LEITOR_VAZIO = { nome: '', email: '' };
const LIVRO_VAZIO = { isbn: '', titulo: '', autor: '', quantidadeTotal: 1 };
const EMPRESTIMO_VAZIO = { leitorId: '', livroId: '' };
const ABAS = [
  ['leitores', 'Leitores'],
  ['livros', 'Livros'],
  ['emprestimos', 'Empréstimos'],
];

function LinhaVazia({ colunas, texto }) {
  return (
    <tr className="vazia">
      <td colSpan={colunas}>{texto}</td>
    </tr>
  );
}

function Admin({ confirmar }) {
  const [aba, setAba] = useState('leitores');

  const [leitores, setLeitores] = useState([]);
  const [livros, setLivros] = useState([]);
  const [emprestimos, setEmprestimos] = useState([]);

  const [leitorForm, setLeitorForm] = useState(LEITOR_VAZIO);
  const [leitorEditando, setLeitorEditando] = useState(null);
  const [livroForm, setLivroForm] = useState(LIVRO_VAZIO);
  const [livroEditando, setLivroEditando] = useState(null);
  const [emprestimoForm, setEmprestimoForm] = useState(EMPRESTIMO_VAZIO);
  const [dicas, setDicas] = useState({});

  // tooltip que aparece quando o campo é obrigatório
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
      setLeitores(await api.listarLeitores());
      setLivros(await api.listarLivros());
      setEmprestimos(await api.listarEmprestimos());
    } catch (e) {
      toast.error(e.message);
    }
  }

  useEffect(() => {
    carregarTudo();
  }, []);

  // executa a ação, mostra a mensagem e recarrega as listas
  async function executar(acao, mensagem) {
    try {
      await acao();
      toast.success(mensagem);
      await carregarTudo();
    } catch (e) {
      toast.error(e.message);
    }
  }

  // ajusta a dica quando o usuário digita no campo ou troca de aba ou abre um menu diferente
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

  const emprestimosAtivos = emprestimos.filter((item) => item.situacao === 'ATIVO').length;
  const exemplaresDisponiveis = livros.reduce((soma, livro) => soma + livro.quantidadeDisponivel, 0);

  const CONTAGENS = { leitores: leitores.length, livros: livros.length, emprestimos: emprestimos.length };

  return (
    <div className="admin" style={{ '--fundo': `url(${process.env.PUBLIC_URL}/img/Bookshelf_admin.jpg)` }}>
      <aside className="lateral" style={{ '--i': ABAS.findIndex(([id]) => id === aba) }}>
        <a className="lateral-marca" href="#/">
          <Logo className="logo-marca" />
          <span>Biblioteca</span>
        </a>
        <nav className="lateral-menu" aria-label="Painel administrativo">
          {ABAS.map(([id, rotulo]) => (
            <button
              key={id}
              type="button"
              className={aba === id ? 'lateral-item ativo' : 'lateral-item'}
              aria-current={aba === id ? 'page' : undefined}
              onClick={() => trocarAba(id)}
            >
              {rotulo}
              <span className="lateral-conta">{CONTAGENS[id]}</span>
            </button>
          ))}
        </nav>
        <a className="lateral-sair" href="#/estante">
          Voltar à loja
        </a>
      </aside>

      <main className="app admin-conteudo">
        <div className="admin-topo">
          <div>
            <h1>Painel administrativo</h1>
            <p>Cadastro de leitores, livros e controle de empréstimos</p>
          </div>
        </div>

        <div className="resumo">
          <div className="stat">
            <span className="stat-numero">{leitores.length}</span>
            <span className="stat-rotulo">Leitores cadastrados</span>
          </div>
          <div className="stat">
            <span className="stat-numero">{livros.length}</span>
            <span className="stat-rotulo">Títulos no acervo</span>
          </div>
          <div className="stat">
            <span className="stat-numero">{exemplaresDisponiveis}</span>
            <span className="stat-rotulo">Exemplares disponíveis</span>
          </div>
          <div className="stat">
            <span className="stat-numero">{emprestimosAtivos}</span>
            <span className="stat-rotulo">Empréstimos ativos</span>
          </div>
        </div>

        {aba === 'leitores' && (
          <section className="painel">
            <div className={leitorEditando ? 'card card-form editando' : 'card card-form'}>
              <h2>{leitorEditando ? 'Editar leitor' : 'Cadastrar leitor'}</h2>
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
                  <button type="submit">{leitorEditando ? 'Atualizar' : 'Cadastrar'}</button>
                  {leitorEditando && (
                    <button
                      type="button"
                      className="secundario"
                      onClick={() =>
                        confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLeitor)
                      }
                    >
                      Cancelar
                    </button>
                  )}
                </div>
              </form>
            </div>

            <div className="card">
              <div className="card-topo">
                <h2>Leitores cadastrados</h2>
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
                        <td className="forte">{leitor.nome}</td>
                        <td>{leitor.email}</td>
                        <td className="acoes-linha">
                          <button
                            type="button"
                            className="secundario pequeno"
                            onClick={() => {
                              setDicas({});
                              setLeitorEditando(leitor.id);
                              setLeitorForm({ nome: leitor.nome, email: leitor.email });
                            }}
                          >
                            Editar
                          </button>
                          <button
                            type="button"
                            className="perigo pequeno"
                            onClick={() =>
                              confirmar('Deseja mesmo excluir este leitor?', () =>
                                executar(() => api.excluirLeitor(leitor.id), 'Leitor excluído.'),
                              )
                            }
                          >
                            Excluir
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </section>
        )}

        {aba === 'livros' && (
          <section className="painel">
            <div className={livroEditando ? 'card card-form editando' : 'card card-form'}>
              <h2>{livroEditando ? 'Editar livro' : 'Cadastrar livro'}</h2>
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
                  <button type="submit">{livroEditando ? 'Atualizar' : 'Cadastrar'}</button>
                  {livroEditando && (
                    <button
                      type="button"
                      className="secundario"
                      onClick={() =>
                        confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLivro)
                      }
                    >
                      Cancelar
                    </button>
                  )}
                </div>
              </form>
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
                        <td className="forte">{livro.titulo}</td>
                        <td>{livro.autor}</td>
                        <td className="mono">{livro.quantidadeDisponivel}</td>
                        <td className="mono">{livro.quantidadeTotal}</td>
                        <td className="acoes-linha">
                          <button
                            type="button"
                            className="secundario pequeno"
                            onClick={() => {
                              setDicas({});
                              setLivroEditando(livro.id);
                              setLivroForm(livro);
                            }}
                          >
                            Editar
                          </button>
                          <button
                            type="button"
                            className="perigo pequeno"
                            onClick={() =>
                              confirmar('Deseja mesmo excluir este livro?', () =>
                                executar(() => api.excluirLivro(livro.id), 'Livro excluído.'),
                              )
                            }
                          >
                            Excluir
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </section>
        )}

        {aba === 'emprestimos' && (
          <section className="painel">
            <div className="card card-form">
              <h2>Novo empréstimo</h2>
              <form onSubmit={salvarEmprestimo} onChange={limparDica} className="form">
                <label>
                  Leitor
                  <select
                    value={emprestimoForm.leitorId}
                    onChange={(e) => setEmprestimoForm({ ...emprestimoForm, leitorId: e.target.value })}
                    required
                    {...obrigatorio('leitorId', 'Selecione um leitor.')}
                  >
                    <option value="">Selecione</option>
                    {leitores.map((leitor) => (
                      <option key={leitor.id} value={leitor.id}>
                        {leitor.nome}
                      </option>
                    ))}
                  </select>
                  {dicas.leitorId && <span className="dica">{dicas.leitorId}</span>}
                </label>
                <label>
                  Livro
                  <select
                    value={emprestimoForm.livroId}
                    onChange={(e) => setEmprestimoForm({ ...emprestimoForm, livroId: e.target.value })}
                    required
                    {...obrigatorio('livroId', 'Selecione um livro.')}
                  >
                    <option value="">Selecione</option>
                    {livros.map((livro) => (
                      <option key={livro.id} value={livro.id}>
                        {livro.titulo} ({livro.quantidadeDisponivel} disp.)
                      </option>
                    ))}
                  </select>
                  {dicas.livroId && <span className="dica">{dicas.livroId}</span>}
                </label>
                <div className="acoes">
                  <button type="submit">Emprestar</button>
                </div>
              </form>
            </div>

            <div className="card">
              <div className="card-topo">
                <h2>Empréstimos</h2>
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
                        <td className="forte">{item.leitorNome}</td>
                        <td>{item.livroTitulo}</td>
                        <td className="mono">{item.dataEmprestimo}</td>
                        <td className="mono">{item.dataPrevistaDevolucao}</td>
                        <td className="mono">{item.dataDevolucao || '-'}</td>
                        <td>
                          <span className={'badge ' + String(item.situacao).toLowerCase()}>{item.situacao}</span>
                        </td>
                        <td>
                          {item.situacao === 'ATIVO' && (
                            <button
                              type="button"
                              className="pequeno"
                              onClick={() =>
                                confirmar('Deseja mesmo registrar a devolução?', () =>
                                  executar(() => api.devolverEmprestimo(item.id), 'Devolução registrada.'),
                                )
                              }
                            >
                              Devolver
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </section>
        )}
      </main>
    </div>
  );
}

export default Admin;
