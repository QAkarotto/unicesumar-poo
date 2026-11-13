
import { useState } from 'react'
import Sidebar from './components/Sidebar'
import Dashboard from './pages/Dashboard'
import Livros from './pages/Livros'
import Leitores from './pages/Leitores'
import Emprestimos from './pages/Emprestimos'
import './App.css'

const paginas = {
  dashboard: Dashboard,
  livros: Livros,
  leitores: Leitores,
  emprestimos: Emprestimos,
}

export default function App() {
  const [pagina, setPagina] = useState('dashboard')
  const PaginaAtual = paginas[pagina]

  return (
    <div className="app-layout">
      <Sidebar pagina={pagina} onNavegar={setPagina} />

      <main className="main-content">
        <PaginaAtual />
      </main>
    </div>
  )
}
