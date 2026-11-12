import { LogOut, Search } from 'lucide-react'
import { useEffect, useRef, useState } from 'react'
import { Link, NavLink, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/useAuth'

const links = [
  { to: '/', label: 'Início' },
  { to: '/catalogo', label: 'Catálogo' },
  { to: '/minha-lista', label: 'Minha lista' },
]

function Navbar() {
  const [query, setQuery] = useState('')
  const [theme, setTheme] = useState(() => document.documentElement.dataset.theme || 'light')
  const [accountMenuOpen, setAccountMenuOpen] = useState(false)
  const accountMenuRef = useRef(null)
  const accountButtonRef = useRef(null)
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout } = useAuth()

  useEffect(() => {
    if (!accountMenuOpen) return undefined

    function closeOutside(event) {
      if (!accountMenuRef.current?.contains(event.target)) setAccountMenuOpen(false)
    }

    function closeOnEscape(event) {
      if (event.key === 'Escape') {
        setAccountMenuOpen(false)
        accountButtonRef.current?.focus()
      }
    }

    document.addEventListener('pointerdown', closeOutside)
    document.addEventListener('keydown', closeOnEscape)
    return () => {
      document.removeEventListener('pointerdown', closeOutside)
      document.removeEventListener('keydown', closeOnEscape)
    }
  }, [accountMenuOpen])

  function toggleTheme() {
    const next = theme === 'dark' ? 'light' : 'dark'
    document.documentElement.dataset.theme = next
    localStorage.setItem('aniva:tema', next)
    setTheme(next)
  }

  function submitSearch(event) {
    event.preventDefault()
    navigate(`/catalogo?q=${encodeURIComponent(query.trim())}`)
  }

  const initials = String(user?.nome || '').trim().split(/\s+/).slice(0, 2).map((part) => part[0]).join('').toLocaleUpperCase('pt-BR')

  return (
    <header className="home-site-nav">
      <div className="home-nav-inner">
        <div className="home-nav-top">
          <NavLink to="/" className="home-logo" aria-label="ANiVA início" />
          <div className="home-nav-tools">
            {user ? (
              <div className="home-user-menu" ref={accountMenuRef}>
                <button ref={accountButtonRef} className="home-icon-button home-user-avatar" type="button" aria-label={`Menu da conta de ${user.nome}`} aria-haspopup="menu" aria-expanded={accountMenuOpen} aria-controls="home-account-menu" onClick={() => setAccountMenuOpen((open) => !open)}>{initials}</button>
                {accountMenuOpen ? (
                  <div className="home-account-menu" id="home-account-menu" role="menu">
                    <p className="home-account-name">{user.nome}</p>
                    <Link to="/minha-lista" role="menuitem" onClick={() => setAccountMenuOpen(false)}>Minha lista</Link>
                    <button type="button" role="menuitem" className="home-account-logout" onClick={() => { setAccountMenuOpen(false); logout() }}><LogOut size={16} aria-hidden="true" />Sair</button>
                  </div>
                ) : null}
              </div>
            ) : (
              <Link className="home-account-button" to={`/entrar?volta=${encodeURIComponent(`${location.pathname}${location.search}`)}`}>Entrar</Link>
            )}
            <button className="home-icon-button" type="button" aria-label={`Alternar tema, atual ${theme === 'dark' ? 'escuro' : 'claro'}`} aria-pressed={theme === 'dark'} onClick={toggleTheme}><span aria-hidden="true">◐</span></button>
          </div>
        </div>

        <nav className="home-nav-bottom" aria-label="Navegação do site">
          <div className="home-nav-links">
            {links.map((link) => (
              <NavLink key={link.to} to={link.to} className={({ isActive }) => `home-nav-link ${isActive ? 'is-active' : ''}`}>
                {link.label}
              </NavLink>
            ))}
          </div>
          <form onSubmit={submitSearch} className="home-nav-search" role="search">
            <input
              className="home-search-input"
              type="search"
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Buscar títulos"
              aria-label="Buscar títulos"
            />
            <button type="submit" aria-label="Buscar"><Search size={17} aria-hidden="true" /></button>
          </form>
        </nav>
      </div>
    </header>
  )
}

export default Navbar
