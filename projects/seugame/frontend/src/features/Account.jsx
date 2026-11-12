import { useEffect } from 'react'
import { useAuth } from '../auth/AuthContext.js'
import { Loading, PageError } from '../components/Feedback.jsx'
import AuthPage from '../pages/AuthPage.jsx'
import ProfilePage from '../pages/ProfilePage.jsx'
import useMutation from '../hooks/useMutation.js'
import useResource from '../hooks/useResource.js'
import { navigate, returnPath } from '../lib/router.js'

export function Redirect({ to }) {
  useEffect(() => navigate(to, { replace: true }), [to])
  return <Loading message="Redirecionando…" />
}
export function RequireAuth({ children, location }) {
  const auth = useAuth()
  if (auth.loading) return <Loading message="Consultando sua sessão…" />
  if (auth.error) return <PageError error={auth.error} retry={auth.refreshSession} />
  if (!auth.user) return <Redirect to={'/login?voltar=' + encodeURIComponent(location)} />
  return children
}
export function Authentication({ signup, search }) {
  const auth = useAuth()
  const mutation = useMutation()
  const destination = returnPath(search)
  if (auth.loading) return <Loading message="Consultando sua sessão…" />
  if (auth.user && !mutation.busy) return <Redirect to={destination} />
  function submit(values) {
    mutation.run(async () => {
      const { csrfReady } = await auth.authenticate(signup ? '/auth/cadastro' : '/auth/login', values)
      const success = signup ? 'Conta criada. Bem-vindo ao SeuGame!' : 'Login realizado com sucesso.'
      auth.notify(csrfReady ? success : success + ' Não foi possível renovar a proteção da sessão; tente novamente antes de salvar novos dados.', csrfReady ? 'sucesso' : 'info')
      navigate(destination, { replace: true })
    })
  }
  return <AuthPage signup={signup} onSubmit={submit} busy={mutation.busy} error={mutation.error} returnTo={destination !== '/' ? destination : ''} />
}
export function Profile() {
  const auth = useAuth()
  const resource = useResource('/usuarios/me')
  const mutation = useMutation()
  if (!resource.data) return resource.error ? <PageError error={resource.error} retry={resource.reload} /> : <Loading />
  function submit(values) {
    mutation.run(async () => {
      await auth.updateProfile(values)
      auth.notify('Perfil atualizado.')
      resource.reload()
    })
  }
  return <ProfilePage key={JSON.stringify([resource.data.id, resource.data.nome, resource.data.email])} user={resource.data} onSubmit={submit} onLogout={auth.logout} logoutBusy={auth.logoutBusy}
    busy={mutation.busy || resource.loading} error={mutation.error || resource.error} />
}
