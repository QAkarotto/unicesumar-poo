import { useCallback, useEffect, useRef, useState } from 'react';
import { request } from './api';

const views = [
  { id: 'inicio', label: 'Início' },
  { id: 'livros', label: 'Livros' },
  { id: 'leitores', label: 'Leitores' },
  { id: 'emprestimos', label: 'Empréstimos' },
];
const titles = {
  inicio: ['Resumo da biblioteca', 'Consulte os cadastros e os últimos empréstimos.'],
  livros: ['Livros', 'Cadastro e consulta dos livros da biblioteca.'],
  leitores: ['Leitores', 'Cadastro e consulta dos leitores.'],
  emprestimos: ['Empréstimos', 'Registre empréstimos e devoluções.'],
};
const blankBook = { titulo: '', autor: '', isbn: '', categoria: '', exemplares: 1 };
const date = value => value ? new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' }).format(new Date(`${value}T12:00:00`)) : '—';
const normalize = value => value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();

function Badge({ status }) {
  const labels = { ATIVO: 'Em leitura', ATRASADO: 'Em atraso', DEVOLVIDO: 'Devolvido' };
  return <span className={`badge ${status.toLowerCase()}`}><span/>{labels[status]}</span>;
}

function Empty({ title, text, action }) {
  return <div className="empty"><h3>{title}</h3><p>{text}</p>{action}</div>;
}

function LoanTable({ loans, onReturn, busy }) {
  return <div className="table-scroll"><table><thead><tr><th>Livro / leitor</th><th>Empréstimo</th><th>Devolução prevista</th><th>Situação</th><th><span className="sr-only">Ação</span></th></tr></thead><tbody>{loans.map(loan => <tr key={loan.id}>
    <td><strong>{loan.livro}</strong><span className="cell-secondary">{loan.leitor}</span></td><td>{date(loan.emprestadoEm)}</td><td className={loan.situacao === 'ATRASADO' ? 'late-date' : ''}>{date(loan.previstoPara)}{loan.devolvidoEm && <span className="cell-secondary">Devolvido: {date(loan.devolvidoEm)}</span>}</td><td><Badge status={loan.situacao}/></td><td>{loan.situacao !== 'DEVOLVIDO' && <button className="text-button" onClick={() => onReturn(loan)} disabled={busy}>Devolver </button>}</td>
  </tr>)}</tbody></table></div>;
}

function Modal({ modal, books, readers, onClose, onSubmit, busy }) {
  const ref = useRef(null);
  const [error, setError] = useState('');
  const [form, setForm] = useState(() => modal.type === 'livro' ? { ...blankBook, ...modal.item } : modal.type === 'leitor' ? { nome: '', email: '', ...modal.item } : { livroId: '', leitorId: '' });
  useEffect(() => { const dialog = ref.current; dialog.showModal(); return () => dialog.close(); }, []);
  const change = event => setForm(current => ({ ...current, [event.target.name]: event.target.value }));
  const title = modal.type === 'devolucao' ? 'Confirmar devolução' : modal.type === 'livro' ? (modal.item ? 'Editar livro' : 'Cadastrar livro') : modal.type === 'leitor' ? (modal.item ? 'Editar leitor' : 'Cadastrar leitor') : 'Novo empréstimo';
  async function submit(event) {
    event.preventDefault(); setError('');
    try { await onSubmit(form); } catch (err) { setError(err.message); }
  }
  return <dialog ref={ref} className="modal" aria-labelledby="modal-title" onCancel={event => { event.preventDefault(); if (!busy) onClose(); }}>
    <div className="modal-top"><span className="eyebrow">Biblioteca / {modal.type === 'devolucao' ? 'Devolução' : 'Cadastro'}</span><button type="button" className="icon-button" onClick={onClose} aria-label="Fechar" disabled={busy}>Fechar</button></div>
    <h2 id="modal-title">{title}</h2>
    <form onSubmit={submit}>
      <fieldset disabled={busy}>
        {modal.type === 'livro' && <>
          <label>Título<input name="titulo" value={form.titulo} onChange={change} required maxLength={180} placeholder="Título do livro" autoFocus/></label>
          <label>Autor(a)<input name="autor" value={form.autor} onChange={change} required maxLength={120} placeholder="Nome do autor"/></label>
          <div className="form-row"><label>ISBN<input name="isbn" value={form.isbn} onChange={change} required pattern="[0-9]{13}" maxLength={13} inputMode="numeric" title="Informe 13 dígitos, sem hífens" placeholder="13 dígitos, sem hífens"/></label><label>Exemplares<input name="exemplares" value={form.exemplares} onChange={change} type="number" min="1" max="10000" required/></label></div>
          <label>Categoria<input name="categoria" value={form.categoria} onChange={change} required maxLength={60} placeholder="Ex.: Literatura brasileira"/></label>
        </>}
        {modal.type === 'leitor' && <><label>Nome completo<input name="nome" value={form.nome} onChange={change} required maxLength={120} autoFocus placeholder="Nome do leitor"/></label><label>E-mail<input name="email" value={form.email} onChange={change} type="email" required maxLength={180} placeholder="leitor@exemplo.com"/></label></>}
        {modal.type === 'emprestimo' && <>
          <p className="modal-description">Escolha um leitor e um livro disponível. O prazo de devolução é de 14 dias.</p>
          <label>Leitor<select name="leitorId" value={form.leitorId} onChange={change} required autoFocus><option value="">Selecione um leitor</option>{readers.filter(r => r.ativo).map(r => <option key={r.id} value={r.id}>{r.nome} · {r.emprestimosAtivos}/3 empréstimos</option>)}</select></label>
          <label>Livro<select name="livroId" value={form.livroId} onChange={change} required><option value="">Selecione um livro</option>{books.filter(b => b.disponiveis > 0).map(b => <option key={b.id} value={b.id}>{b.titulo} · {b.disponiveis} disponível(is)</option>)}</select></label>
          {(!books.some(b => b.disponiveis > 0) || !readers.some(r => r.ativo)) && <p className="notice">Cadastre um livro com disponibilidade e um leitor ativo para continuar.</p>}
          <div className="form-note">Até 3 empréstimos ativos por leitor. Atrasos bloqueiam novas retiradas.</div>
        </>}
        {modal.type === 'devolucao' && <p className="modal-description">Confirmar a devolução de <strong>{modal.item.livro}</strong>, emprestado para <strong>{modal.item.leitor}</strong>? O exemplar voltará a ficar disponível.</p>}
      </fieldset>
      {error && <div className="error" role="alert">{error}</div>}
      <div className="modal-actions"><button type="button" className="button secondary" onClick={onClose} disabled={busy}>Cancelar</button><button className="button" type="submit" disabled={busy}>{busy ? 'Salvando…' : modal.type === 'devolucao' ? 'Confirmar devolução' : modal.type === 'emprestimo' ? 'Registrar empréstimo' : 'Salvar cadastro'}</button></div>
    </form>
  </dialog>;
}

export default function App() {
  const [view, setView] = useState('inicio');
  const [data, setData] = useState({ books: [], readers: [], loans: [] });
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');
  const [actionError, setActionError] = useState('');
  const [notice, setNotice] = useState('');
  const [modal, setModal] = useState(null);
  const [busy, setBusy] = useState(false);
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState('TODOS');
  const { books, readers, loans } = data;

  const reload = useCallback(async () => {
    setLoadError(''); setLoading(true);
    try {
      const [books, readers, loans] = await Promise.all([request('/livros'), request('/leitores'), request('/emprestimos')]);
      setData({ books, readers, loans });
    } catch (err) { setLoadError(err.message); }
    finally { setLoading(false); }
  }, []);
  useEffect(() => { reload(); const onFocus = () => reload(); window.addEventListener('focus', onFocus); return () => window.removeEventListener('focus', onFocus); }, [reload]);
  useEffect(() => { if (!notice) return; const timer = setTimeout(() => setNotice(''), 6000); return () => clearTimeout(timer); }, [notice]);
  function navigate(id) { setView(id); setQuery(''); setFilter('TODOS'); setActionError(''); }
  function open(type, item) { setActionError(''); setModal({ type, item }); }
  async function save(form) {
    setBusy(true);
    try {
      const { type, item } = modal;
      if (type === 'livro') await request(`/livros${item ? `/${item.id}` : ''}`, { method: item ? 'PUT' : 'POST', body: { titulo: form.titulo.trim(), autor: form.autor.trim(), isbn: form.isbn, categoria: form.categoria.trim(), exemplares: Number(form.exemplares) } });
      if (type === 'leitor') await request(`/leitores${item ? `/${item.id}` : ''}`, { method: item ? 'PUT' : 'POST', body: { nome: form.nome.trim(), email: form.email.trim() } });
      if (type === 'emprestimo') await request('/emprestimos', { method: 'POST', body: { livroId: Number(form.livroId), leitorId: Number(form.leitorId) } });
      if (type === 'devolucao') await request(`/emprestimos/${item.id}/devolucao`, { method: 'PATCH' });
      setModal(null); setNotice(type === 'devolucao' ? 'Devolução registrada.' : 'Registro salvo com sucesso.');
      await reload();
    } finally { setBusy(false); }
  }
  async function toggleReader(reader) {
    setBusy(true); setActionError('');
    try { await request(`/leitores/${reader.id}/atividade`, { method: 'PATCH', body: { ativo: !reader.ativo } }); setNotice(reader.ativo ? 'Leitor inativado.' : 'Leitor reativado.'); await reload(); }
    catch (err) { setActionError(err.message); }
    finally { setBusy(false); }
  }
  const matches = (...values) => normalize(values.join(' ')).includes(normalize(query));
  const visibleBooks = books.filter(b => matches(b.titulo, b.autor, b.isbn, b.categoria) && (filter === 'TODOS' || (filter === 'DISPONIVEIS' ? b.disponiveis > 0 : b.disponiveis === 0)));
  const visibleReaders = readers.filter(r => matches(r.nome, r.email) && (filter === 'TODOS' || (filter === 'ATIVOS' ? r.ativo : !r.ativo)));
  const visibleLoans = loans.filter(l => matches(l.livro, l.leitor) && (filter === 'TODOS' || l.situacao === filter));
  const activeLoans = loans.filter(l => l.situacao !== 'DEVOLVIDO');
  const overdue = loans.filter(l => l.situacao === 'ATRASADO');
  const totalCopies = books.reduce((sum, b) => sum + b.exemplares, 0);
  const primaryType = view === 'livros' ? 'livro' : view === 'leitores' ? 'leitor' : 'emprestimo';

  return (
    <div className="app-shell">
      <a className="skip-link" href="#main">Ir para o conteúdo</a>
      <header className="site-header">
        <div className="header-content">
          <strong>Sistema de Biblioteca</strong>
          <nav aria-label="Navegação principal">
            {views.map(item => (
              <button key={item.id} className={view === item.id ? 'selected' : ''}
                onClick={() => navigate(item.id)} aria-current={view === item.id ? 'page' : undefined}>
                {item.label}
              </button>
            ))}
          </nav>
        </div>
      </header>

      <main id="main">
        <div className="page-heading">
          <div>
            <h1>{titles[view][0]}</h1>
            <p>{titles[view][1]}</p>
          </div>
          <button className="button" disabled={loading || !!loadError} onClick={() => open(primaryType)}>
            {primaryType === 'livro' ? 'Cadastrar livro' : primaryType === 'leitor' ? 'Cadastrar leitor' : 'Novo empréstimo'}
          </button>
        </div>

        <div aria-live="polite" className={notice ? 'toast' : 'sr-only'}>{notice}</div>
        {actionError && <div className="error" role="alert">{actionError}</div>}

        {loadError ? (
          <div className="connection-error" role="alert">
            <h2>Erro ao carregar os dados</h2>
            <p>{loadError}</p>
            <button className="button" onClick={reload}>Tentar novamente</button>
          </div>
        ) : loading ? (
          <p className="loading" role="status">Carregando dados...</p>
        ) : (
          <>
            {view === 'inicio' && <>
              <section className="stats" aria-label="Resumo da biblioteca">
                <article><span>Livros cadastrados</span><strong>{books.length}</strong><small>{totalCopies} exemplares no total</small></article>
                <article><span>Leitores ativos</span><strong>{readers.filter(r => r.ativo).length}</strong><small>{readers.length} cadastrados</small></article>
                <article><span>Empréstimos em aberto</span><strong>{activeLoans.length}</strong></article>
                <article><span>Em atraso</span><strong>{overdue.length}</strong></article>
              </section>
              <section className="panel">
                <div className="section-header">
                  <h2>Últimos empréstimos</h2>
                  <button className="text-button" onClick={() => navigate('emprestimos')}>Ver todos</button>
                </div>
                {loans.length > 0
                  ? <LoanTable loans={loans.slice(0, 5)} onReturn={loan => open('devolucao', loan)} busy={busy}/>
                  : <Empty title="Nenhum empréstimo registrado" text="Cadastre um livro e um leitor para fazer o primeiro empréstimo."/>}
              </section>
              <p className="help-text">Prazo de devolução: 14 dias. Cada leitor pode ter até 3 empréstimos em aberto.</p>
            </>}

            {view !== 'inicio' && <>
              <div className="toolbar">
                <label className="search">Buscar
                  <input value={query} onChange={event => setQuery(event.target.value)}
                    placeholder={view === 'livros' ? 'Título, autor, categoria ou ISBN' : view === 'leitores' ? 'Nome ou e-mail' : 'Livro ou leitor'}/>
                </label>
                <label>Filtrar
                  <select value={filter} onChange={event => setFilter(event.target.value)}>
                    <option value="TODOS">Todos</option>
                    {(view === 'livros'
                      ? [['DISPONIVEIS', 'Com exemplares disponíveis'], ['INDISPONIVEIS', 'Sem exemplares disponíveis']]
                      : view === 'leitores'
                        ? [['ATIVOS', 'Ativos'], ['INATIVOS', 'Inativos']]
                        : [['ATIVO', 'Em leitura'], ['ATRASADO', 'Em atraso'], ['DEVOLVIDO', 'Devolvidos']]
                    ).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                  </select>
                </label>
              </div>

              <p className="result-summary">
                {view === 'livros' ? visibleBooks.length : view === 'leitores' ? visibleReaders.length : visibleLoans.length} registro(s) encontrado(s)
              </p>

              {view === 'livros' && <section className="panel" aria-label="Livros">
                {visibleBooks.length > 0 ? <div className="table-scroll"><table>
                  <thead><tr><th>Título</th><th>Autor</th><th>ISBN</th><th>Categoria</th><th>Total</th><th>Disponíveis</th><th>Ações</th></tr></thead>
                  <tbody>{visibleBooks.map(book => <tr key={book.id}>
                    <td>{book.titulo}</td><td>{book.autor}</td><td>{book.isbn}</td><td>{book.categoria}</td>
                    <td>{book.exemplares}</td><td>{book.disponiveis}</td>
                    <td><button className="text-button" onClick={() => open('livro', book)} aria-label={'Editar ' + book.titulo}>Editar</button></td>
                  </tr>)}</tbody>
                </table></div> : <Empty title="Nenhum livro encontrado" text="Cadastre um livro ou altere os filtros da busca."/>}
              </section>}

              {view === 'leitores' && <section className="panel" aria-label="Leitores">
                {visibleReaders.length > 0 ? <div className="table-scroll"><table>
                  <thead><tr><th>Nome</th><th>E-mail</th><th>Situação</th><th>Empréstimos ativos</th><th>Ações</th></tr></thead>
                  <tbody>{visibleReaders.map(reader => <tr key={reader.id}>
                    <td>{reader.nome}</td><td>{reader.email}</td><td>{reader.ativo ? 'Ativo' : 'Inativo'}</td>
                    <td>{reader.emprestimosAtivos} de 3</td>
                    <td><div className="row-actions">
                      <button className="text-button" onClick={() => open('leitor', reader)} aria-label={'Editar ' + reader.nome}>Editar</button>
                      <button className="text-button" disabled={busy} onClick={() => toggleReader(reader)}>{reader.ativo ? 'Inativar' : 'Reativar'}</button>
                    </div></td>
                  </tr>)}</tbody>
                </table></div> : <Empty title="Nenhum leitor encontrado" text="Cadastre um leitor ou altere os filtros da busca."/>}
              </section>}

              {view === 'emprestimos' && <section className="panel">
                {visibleLoans.length > 0
                  ? <LoanTable loans={visibleLoans} onReturn={loan => open('devolucao', loan)} busy={busy}/>
                  : <Empty title="Nenhum empréstimo encontrado" text="Registre um empréstimo ou altere os filtros da busca."/>}
              </section>}
            </>}
          </>
        )}

        <footer>Sistema de Biblioteca — Trabalho de Programação Orientada a Objetos</footer>
      </main>

      {modal && <Modal key={modal.type + '-' + (modal.item?.id || 'novo')}
        modal={modal} books={books} readers={readers}
        onClose={() => { if (!busy) setModal(null); }} onSubmit={save} busy={busy}/>}
    </div>
  );
}
