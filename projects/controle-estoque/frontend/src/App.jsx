import { NavLink, Route, Routes } from 'react-router-dom'
import Painel from './pages/Painel.jsx'
import Produtos from './pages/Produtos.jsx'
import ProdutoDetalhe from './pages/ProdutoDetalhe.jsx'
import Movimentacoes from './pages/Movimentacoes.jsx'
import Categorias from './pages/Categorias.jsx'
import Fornecedores from './pages/Fornecedores.jsx'

export default function App() {
  return (
    <div className="app">
      <header className="topo">
        <h1>📦 Controle de Estoque</h1>
        <nav>
          <NavLink to="/" end>Painel</NavLink>
          <NavLink to="/produtos">Produtos</NavLink>
          <NavLink to="/movimentacoes">Movimentações</NavLink>
          <NavLink to="/categorias">Categorias</NavLink>
          <NavLink to="/fornecedores">Fornecedores</NavLink>
        </nav>
      </header>

      <main className="conteudo">
        <Routes>
          <Route path="/" element={<Painel />} />
          <Route path="/produtos" element={<Produtos />} />
          <Route path="/produtos/:id" element={<ProdutoDetalhe />} />
          <Route path="/movimentacoes" element={<Movimentacoes />} />
          <Route path="/categorias" element={<Categorias />} />
          <Route path="/fornecedores" element={<Fornecedores />} />
          <Route path="*" element={<p>Página não encontrada.</p>} />
        </Routes>
      </main>
    </div>
  )
}
