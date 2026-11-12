import { Navigate, Route, Routes, useLocation } from 'react-router-dom'
import { useEffect } from 'react'
import { useAuth } from './context/useAuth'
import Navbar from './components/common/Navbar'
import Home from './pages/Home'
import Browse from './pages/Browse'
import Details from './pages/Details'
import MinhaLista from './pages/MinhaLista'
import Entrar from './pages/Entrar'

function App() {
  const location = useLocation()
  const { ready } = useAuth()

  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'auto' })
  }, [location.pathname, location.search])

  if (!ready) return <main className="site-shell app-loading"><h1 className="app-loading-logo" aria-label="ANiVA" /></main>

  return (
    <div className="site-shell">
      <Navbar />
      <main className="page-enter" key={location.pathname}>
        <Routes location={location}>
          <Route path="/" element={<Home />} />
          <Route path="/catalogo" element={<Browse />} />
          <Route path="/minha-lista" element={<MinhaLista />} />
          <Route path="/favoritos" element={<Navigate to="/minha-lista" replace />} />
          <Route path="/para-ver-ler" element={<Navigate to="/minha-lista" replace />} />
          <Route path="/obra/:id" element={<Details />} />
          <Route path="/entrar" element={<Entrar />} />
        </Routes>
      </main>
      <footer className="site-footer">ANiVA: anime, mangá, manhwa e manhua para acompanhar, avaliar e organizar.</footer>
    </div>
  )
}

export default App
