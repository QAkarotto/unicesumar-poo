import { useEffect } from 'react'
import { HashRouter, Navigate, Route, Routes } from 'react-router-dom'
import { carregarBanco, useEstadoBanco } from './services/store'
import { ToastProvider } from './components/Toast'
import { SessaoProvider } from './context/Sessao'
import { Cabecalho } from './components/Cabecalho'
import { FluxoPrincipal } from './components/FluxoPrincipal'
import { ProdutorLayout } from './pages/produtor/ProdutorLayout'
import { Entrada } from './pages/produtor/Entrada'
import { ListaSolicitacoes } from './pages/produtor/ListaSolicitacoes'
import { NovaSolicitacao } from './pages/produtor/NovaSolicitacao'
import { DetalheSolicitacao } from './pages/produtor/DetalheSolicitacao'
import { Propriedades } from './pages/produtor/Propriedades'
import { Perfil } from './pages/produtor/Perfil'
import { TecnicoPage } from './pages/tecnico/TecnicoPage'
import { AdminPage } from './pages/admin/AdminPage'
import { ModeloPage } from './pages/modelo/ModeloPage'
import { HomePage } from './pages/HomePage'

export default function App() {
  const { banco, carregando, erro } = useEstadoBanco()

  useEffect(() => {
    void carregarBanco().catch(() => {})
  }, [])

  if (!banco || erro) {
    return (
      <div className="wrap conteudo">
        <div className="panel">
          <div className="panel-h">
            <h1>✳ AgroAssist</h1>
          </div>
          <div className="panel-b">
            {carregando ? (
              <p role="status">Carregando os dados...</p>
            ) : (
              <>
                <p className="form-err" role="alert">
                  {erro}
                </p>
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={() => {
                    void carregarBanco().catch(() => {})
                  }}
                >
                  Tentar novamente
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    )
  }

  return (
    <HashRouter>
      <ToastProvider>
        <SessaoProvider>
          <Cabecalho />
          <main>
            <div className="wrap conteudo">
              <Routes>
                <Route path="/modelo" element={<FluxoPrincipal />} />
              </Routes>
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/produtor" element={<ProdutorLayout />}>
                  <Route index element={<ListaSolicitacoes />} />
                  <Route path="entrar" element={<Entrada />} />
                  <Route path="nova" element={<NovaSolicitacao />} />
                  <Route path="solicitacoes/:id" element={<DetalheSolicitacao />} />
                  <Route path="propriedades" element={<Propriedades />} />
                  <Route path="perfil" element={<Perfil />} />
                </Route>
                <Route path="/tecnico" element={<TecnicoPage />} />
                <Route path="/admin" element={<AdminPage />} />
                <Route path="/modelo" element={<ModeloPage />} />
                <Route path="*" element={<Navigate to="/produtor" replace />} />
              </Routes>
            </div>
          </main>
        </SessaoProvider>
      </ToastProvider>
    </HashRouter>
  )
}
