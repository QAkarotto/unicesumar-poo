import { useEffect, useRef, useState } from 'react'
import Icon from './Icon.jsx'
import Link from './Link.jsx'
import { Message } from './Feedback.jsx'

function Brand({ onClick }) {
  return <Link className="brand" href="/" aria-label="SeuGame, página inicial" onClick={onClick}><span className="brand-mark">SG</span><span>Seu<span>Game</span></span></Link>
}
export default function Layout({ children, page, user, onLogout, logoutBusy, notice, clearNotice }) {
  const [menuOpen, setMenuOpen] = useState(false)
  const toggleRef = useRef(null)
  const closeRef = useRef(null)
  const sidebarRef = useRef(null)
  function closeMenu(restoreFocus = true) {
    setMenuOpen(false)
    if (restoreFocus) requestAnimationFrame(() => toggleRef.current?.focus())
  }
  useEffect(() => {
    if (!menuOpen) return undefined
    const previousOverflow = document.body.style.overflow
    document.body.style.overflow = 'hidden'
    const focusTimer = window.setTimeout(() => closeRef.current?.focus(), 50)
    function handleKeyDown(event) {
      if (event.key === 'Escape') {
        closeMenu()
      } else if (event.key === 'Tab') {
        const focusable = [...sidebarRef.current.querySelectorAll('a[href], button:not(:disabled)')]
        const first = focusable[0]
        const last = focusable.at(-1)
        if (event.shiftKey && document.activeElement === first) {
          event.preventDefault()
          last?.focus()
        } else if (!event.shiftKey && document.activeElement === last) {
          event.preventDefault()
          first?.focus()
        }
      }
    }
    function handleNavigation() { setMenuOpen(false) }
    document.addEventListener('keydown', handleKeyDown)
    window.addEventListener('popstate', handleNavigation)
    return () => {
      window.clearTimeout(focusTimer)
      document.body.style.overflow = previousOverflow
      document.removeEventListener('keydown', handleKeyDown)
      window.removeEventListener('popstate', handleNavigation)
    }
  }, [menuOpen])
  const nav = [{ href: '/', label: 'Início', icon: 'home', active: page === 'catalog' }]
  if (user) nav.push({ href: '/meus-torneios', label: 'Meus torneios', mobile: 'Torneios', icon: 'trophy', active: page === 'mine' },
    { href: '/torneios/novo', label: 'Novo torneio', mobile: 'Criar', icon: 'plus', active: page === 'create' })
  const item = (entry, mobile = false) => <Link key={entry.href} href={entry.href} onClick={mobile ? undefined : () => closeMenu(false)} className={entry.active ? 'active' : ''} aria-current={entry.active ? 'page' : undefined}>
    <Icon name={entry.icon} /><span>{mobile ? entry.mobile || entry.label : entry.label}</span></Link>
  return <>
    <a className="skip-link" href="#conteudo">Ir para o conteúdo</a>
    <button className={`sidebar-backdrop${menuOpen ? ' is-open' : ''}`} type="button" tabIndex={-1} aria-hidden="true" onClick={() => closeMenu()} />
    <aside id="seugame-sidebar" ref={sidebarRef} className={`sidebar${menuOpen ? ' is-open' : ''}`} role="dialog" aria-label="Menu principal" aria-modal={menuOpen ? 'true' : undefined} aria-hidden={!menuOpen} inert={!menuOpen}>
      <div className="sidebar-top"><Brand onClick={() => closeMenu(false)} /><button ref={closeRef} className="sidebar-close icon-button" type="button" aria-label="Fechar menu" onClick={() => closeMenu()}><Icon name="close" /></button></div>
      <nav className="main-nav" aria-label="Navegação principal">{nav.map((entry) => item(entry))}</nav>
      <div className="sidebar-account">{user ? <><Link href="/perfil" onClick={() => closeMenu(false)} aria-current={page === 'profile' ? 'page' : undefined}><Icon name="user" /><span>{user.nome}</span></Link>
        <button onClick={() => { closeMenu(false); onLogout() }} disabled={logoutBusy}><Icon name="logout" /><span>{logoutBusy ? 'Saindo…' : 'Sair'}</span></button></>
        : <><Link className="btn btn-primary btn-block" href="/login" onClick={() => closeMenu(false)}>Entrar</Link><Link className="btn btn-ghost btn-block" href="/cadastro" onClick={() => closeMenu(false)}>Criar conta</Link></>}</div>
    </aside>
    <div className="page-shell"><header className="mobile-header"><div className="header-start"><button ref={toggleRef} className="menu-toggle icon-button" type="button" aria-label="Abrir menu" aria-controls="seugame-sidebar" aria-expanded={menuOpen} onClick={() => setMenuOpen(true)}><Icon name="menu" /></button><Brand /></div>{user ? <Link className="icon-button" href="/perfil" aria-label="Abrir perfil"><Icon name="user" /></Link>
      : <Link className="btn btn-small btn-primary" href="/login">Entrar</Link>}</header>
      {notice ? <div className="global-notice"><Message {...notice} onClose={clearNotice} /></div> : null}
      <main id="conteudo" className="page-content" tabIndex={-1}>{children}</main>
      <footer className="site-footer"><p>SeuGame © {new Date().getFullYear()}. Competições bem organizadas, do início ao campeão.</p></footer>
    </div>
    <nav className="bottom-nav" aria-label="Navegação para celular">{nav.map((entry) => item(entry, true))}
      {item(user ? { href: '/perfil', label: 'Perfil', icon: 'user', active: page === 'profile' } : { href: '/login', label: 'Entrar', icon: 'user', active: page === 'login' })}
    </nav>
  </>
}
