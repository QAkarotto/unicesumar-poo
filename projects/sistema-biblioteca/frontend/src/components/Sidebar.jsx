
const itens = [
  { id: 'dashboard', nome: 'Dashboard', simbolo: '▦' },
  { id: 'livros', nome: 'Livros', simbolo: '▤' },
  { id: 'leitores', nome: 'Leitores', simbolo: '♙' },
  { id: 'emprestimos', nome: 'Empréstimos', simbolo: '⇄' },
]

export default function Sidebar({ pagina, onNavegar }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="brand-icon">B</span>
        <div>
          <strong>Biblioteca</strong>
          <small>Sistema de Gestão</small>
        </div>
      </div>

      <nav className="sidebar-nav">
        {itens.map((item) => (
          <button
            key={item.id}
            type="button"
            className={pagina === item.id ? 'nav-item ativo' : 'nav-item'}
            onClick={() => onNavegar(item.id)}
          >
            <span className="nav-symbol">{item.simbolo}</span>
            {item.nome}
          </button>
        ))}
      </nav>

      <div className="sidebar-footer">Biblioteca • v1.0</div>
    </aside>
  )
}
