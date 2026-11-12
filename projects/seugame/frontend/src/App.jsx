import { useEffect } from 'react'
import AuthProvider from './auth/AuthProvider.jsx'
import { useAuth } from './auth/AuthContext.js'
import Layout from './components/Layout.jsx'
import { PageError } from './components/Feedback.jsx'
import Catalog from './features/Catalog.jsx'
import { Authentication, Profile, RequireAuth } from './features/Account.jsx'
import { CreateTournament, MyTournaments, Tournament } from './features/Tournaments.jsx'
import { resolveRoute, useLocation } from './lib/router.js'

const titles = { catalog: 'Arena de Torneios', login: 'Entrar', signup: 'Criar conta', profile: 'Meu perfil', mine: 'Meus torneios',
  create: 'Criar torneio', edit: 'Editar torneio', detail: 'Torneio', scores: 'Registrar placares', notFound: 'Página não encontrada' }

function Application() {
  const location = useLocation()
  const route = resolveRoute(location)
  const auth = useAuth()
  useEffect(() => {
    document.title = titles[route.page] + ' | SeuGame'
    document.getElementById('conteudo')?.focus({ preventScroll: true })
  }, [route.path, route.page])
  let content
  switch (route.page) {
    case 'catalog': content = <Catalog search={route.search} />; break
    case 'login': case 'signup': content = <Authentication key={route.page} signup={route.page === 'signup'} search={route.search} />; break
    case 'profile': content = <Profile />; break
    case 'mine': content = <MyTournaments />; break
    case 'create': content = <CreateTournament />; break
    case 'edit': case 'scores': case 'detail': content = <Tournament key={route.page + ':' + route.id} id={route.id} page={route.page} />; break
    default: content = <PageError error={{ status: 404 }} />
  }
  if (['profile', 'mine', 'create', 'edit', 'scores'].includes(route.page)) content = <RequireAuth location={location}>{content}</RequireAuth>
  return <Layout page={route.page} user={auth.user} onLogout={auth.logout} logoutBusy={auth.logoutBusy} notice={auth.notice} clearNotice={auth.clearNotice}>{content}</Layout>
}
export default function App() { return <AuthProvider><Application /></AuthProvider> }
