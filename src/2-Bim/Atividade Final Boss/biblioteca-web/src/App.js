import { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { api } from './api';
import './App.css';

const LEITOR_VAZIO = { nome: '', email: '' };
const LIVRO_VAZIO = { isbn: '', titulo: '', autor: '', quantidadeTotal: 1 };
const EMPRESTIMO_VAZIO = { leitorId: '', livroId: '' };

function App() {
  const [aba, setAba] = useState('leitores');

  const [leitores, setLeitores] = useState([]);
  const [livros, setLivros] = useState([]);
  const [emprestimos, setEmprestimos] = useState([]);

  const [leitorForm, setLeitorForm] = useState(LEITOR_VAZIO);
  const [leitorEditando, setLeitorEditando] = useState(null);
  const [livroForm, setLivroForm] = useState(LIVRO_VAZIO);
  const [livroEditando, setLivroEditando] = useState(null);
  const [emprestimoForm, setEmprestimoForm] = useState(EMPRESTIMO_VAZIO);
  const [confirmacao, setConfirmacao] = useState(null);

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

  // roda a ação, mostra a mensagem e recarrega as listas
  async function executar(acao, mensagem) {
    try {
      await acao();
      toast.success(mensagem);
      await carregarTudo();
    } catch (e) {
      toast.error(e.message);
    }
  }

  // abre o popup de confirmação; a ação só roda se o usuário clicar em Confirmar
  function confirmar(mensagem, acao) {
    setConfirmacao({ mensagem, acao });
  }

  function cancelarLeitor() {
    setLeitorEditando(null);
    setLeitorForm(LEITOR_VAZIO);
  }

  function cancelarLivro() {
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
      }, 'Leitor salvo.')
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
      }, 'Livro salvo.')
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
      }, 'Empréstimo realizado.')
    );
  }

  return (
    <div className="app">
      <header>
        <h1>Biblioteca Universitária</h1>
        <p>Cadastro de leitores, livros e controle de empréstimos</p>
      </header>

      <nav className="tabs">
        <button type="button" className={aba === 'leitores' ? 'tab active' : 'tab'} onClick={() => setAba('leitores')}>
          Leitores
        </button>
        <button type="button" className={aba === 'livros' ? 'tab active' : 'tab'} onClick={() => setAba('livros')}>
          Livros
        </button>
        <button type="button" className={aba === 'emprestimos' ? 'tab active' : 'tab'} onClick={() => setAba('emprestimos')}>
          Empréstimos
        </button>
      </nav>

      <ToastContainer position="top-right" autoClose={3000} />

      {confirmacao && (
        <div className="modal-fundo">
          <div className="modal">
            <p>{confirmacao.mensagem}</p>
            <div className="acoes">
              <button
                type="button"
                onClick={() => {
                  confirmacao.acao();
                  setConfirmacao(null);
                }}
              >
                Confirmar
              </button>
              <button type="button" className="secundario" onClick={() => setConfirmacao(null)}>
                Voltar
              </button>
            </div>
          </div>
        </div>
      )}

      {aba === 'leitores' && (
        <section className="painel">
          <h2>{leitorEditando ? 'Editar leitor' : 'Cadastrar leitor'}</h2>
          <form onSubmit={salvarLeitor} className="form">
            <label>
              Nome
              <input
                value={leitorForm.nome}
                onChange={(e) => setLeitorForm({ ...leitorForm, nome: e.target.value })}
                required
              />
            </label>
            <label>
              E-mail
              <input
                type="email"
                value={leitorForm.email}
                onChange={(e) => setLeitorForm({ ...leitorForm, email: e.target.value })}
                required
              />
            </label>
            <div className="acoes">
              <button type="submit">{leitorEditando ? 'Atualizar' : 'Cadastrar'}</button>
              {leitorEditando && (
                <button type="button" className="secundario" onClick={() => confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLeitor)}>
                  Cancelar
                </button>
              )}
            </div>
          </form>

          <h2>Leitores cadastrados</h2>
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
              {leitores.map((leitor) => (
                <tr key={leitor.id}>
                  <td>{leitor.id}</td>
                  <td>{leitor.nome}</td>
                  <td>{leitor.email}</td>
                  <td className="acoes-linha">
                    <button
                      type="button"
                      onClick={() => {
                        setLeitorEditando(leitor.id);
                        setLeitorForm({ nome: leitor.nome, email: leitor.email });
                      }}
                    >
                      Editar
                    </button>
                    <button
                      type="button"
                      className="perigo"
                      onClick={() =>
                        confirmar('Deseja mesmo excluir este leitor?', () =>
                          executar(() => api.excluirLeitor(leitor.id), 'Leitor excluído.')
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
        </section>
      )}

      {aba === 'livros' && (
        <section className="painel">
          <h2>{livroEditando ? 'Editar livro' : 'Cadastrar livro'}</h2>
          <form onSubmit={salvarLivro} className="form">
            <label>
              ISBN
              <input
                value={livroForm.isbn}
                onChange={(e) => setLivroForm({ ...livroForm, isbn: e.target.value })}
                required
                disabled={livroEditando !== null}
              />
            </label>
            <label>
              Título
              <input
                value={livroForm.titulo}
                onChange={(e) => setLivroForm({ ...livroForm, titulo: e.target.value })}
                required
              />
            </label>
            <label>
              Autor
              <input
                value={livroForm.autor}
                onChange={(e) => setLivroForm({ ...livroForm, autor: e.target.value })}
                required
              />
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
                />
              </label>
            )}
            <div className="acoes">
              <button type="submit">{livroEditando ? 'Atualizar' : 'Cadastrar'}</button>
              {livroEditando && (
                <button type="button" className="secundario" onClick={() => confirmar('Deseja mesmo sair da edição? As alterações serão perdidas.', cancelarLivro)}>
                  Cancelar
                </button>
              )}
            </div>
          </form>

          <h2>Acervo</h2>
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
              {livros.map((livro) => (
                <tr key={livro.id}>
                  <td>{livro.id}</td>
                  <td>{livro.isbn}</td>
                  <td>{livro.titulo}</td>
                  <td>{livro.autor}</td>
                  <td>{livro.quantidadeDisponivel}</td>
                  <td>{livro.quantidadeTotal}</td>
                  <td className="acoes-linha">
                    <button
                      type="button"
                      onClick={() => {
                        setLivroEditando(livro.id);
                        setLivroForm(livro);
                      }}
                    >
                      Editar
                    </button>
                    <button
                      type="button"
                      className="perigo"
                      onClick={() =>
                        confirmar('Deseja mesmo excluir este livro?', () =>
                          executar(() => api.excluirLivro(livro.id), 'Livro excluído.')
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
        </section>
      )}

      {aba === 'emprestimos' && (
        <section className="painel">
          <h2>Novo empréstimo</h2>
          <form onSubmit={salvarEmprestimo} className="form">
            <label>
              Leitor
              <select
                value={emprestimoForm.leitorId}
                onChange={(e) => setEmprestimoForm({ ...emprestimoForm, leitorId: e.target.value })}
                required
              >
                <option value="">Selecione</option>
                {leitores.map((leitor) => (
                  <option key={leitor.id} value={leitor.id}>
                    {leitor.nome}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Livro
              <select
                value={emprestimoForm.livroId}
                onChange={(e) => setEmprestimoForm({ ...emprestimoForm, livroId: e.target.value })}
                required
              >
                <option value="">Selecione</option>
                {livros.map((livro) => (
                  <option key={livro.id} value={livro.id}>
                    {livro.titulo} ({livro.quantidadeDisponivel} disp.)
                  </option>
                ))}
              </select>
            </label>
            <button type="submit">Emprestar</button>
          </form>

          <h2>Empréstimos</h2>
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
              {emprestimos.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.leitorNome}</td>
                  <td>{item.livroTitulo}</td>
                  <td>{item.dataEmprestimo}</td>
                  <td>{item.dataPrevistaDevolucao}</td>
                  <td>{item.dataDevolucao || '-'}</td>
                  <td>{item.situacao}</td>
                  <td>
                    {item.situacao === 'ATIVO' && (
                      <button
                        type="button"
                        onClick={() =>
                          confirmar('Deseja mesmo registrar a devolução?', () =>
                            executar(() => api.devolverEmprestimo(item.id), 'Devolução registrada.')
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
        </section>
      )}
    </div>
  );
}

export default App;
