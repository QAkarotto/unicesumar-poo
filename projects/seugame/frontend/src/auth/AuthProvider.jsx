import { useCallback, useEffect, useRef, useState } from 'react'
import { AuthContext } from './AuthContext.js'
import { api, onSessionExpired, renewCsrf } from '../services/api.js'
import { navigate } from '../lib/router.js'

export default function AuthProvider({ children }) {
  const [session, setSession] = useState({ user: null, loading: true, error: null })
  const [notice, setNotice] = useState(null)
  const [logoutBusy, setLogoutBusy] = useState(false)
  const [version, setVersion] = useState(0)
  const generation = useRef(0)
  const notify = useCallback((message, type = 'sucesso') => setNotice({ message, type }), [])
  const clearNotice = useCallback(() => setNotice(null), [])
  useEffect(() => {
    const controller = new AbortController()
    const current = generation.current
    api('/usuarios/me', { signal: controller.signal, ignoreSession: true }).then(
      (user) => { if (!controller.signal.aborted && current === generation.current) setSession({ user, loading: false, error: null }) },
      (error) => { if (!controller.signal.aborted && current === generation.current) setSession({ user: null, loading: false, error: error.status === 401 ? null : error }) },
    )
    return () => controller.abort()
  }, [version])
  useEffect(() => onSessionExpired(() => {
    generation.current += 1
    setSession({ user: null, loading: false, error: null })
    notify('Sua sessão terminou. Entre novamente para continuar.', 'info')
    const path = window.location.pathname + window.location.search
    if (!['/login', '/cadastro'].includes(window.location.pathname)) navigate('/login?voltar=' + encodeURIComponent(path), { replace: true })
  }), [notify])

  async function refreshToken() {
    try { await renewCsrf(); return true }
    catch { return false }
  }
  async function authenticate(path, values) {
    const user = await api(path, { method: 'POST', body: values, ignoreSession: true })
    generation.current += 1
    setSession({ user, loading: false, error: null })
    return { user, csrfReady: await refreshToken() }
  }
  async function logout() {
    if (logoutBusy) return
    setLogoutBusy(true)
    try {
      await api('/auth/logout', { method: 'POST' })
      generation.current += 1
      setSession({ user: null, loading: false, error: null })
      const csrfReady = await refreshToken()
      notify(csrfReady ? 'Você saiu da sua conta.' : 'Você saiu da sua conta. Não foi possível renovar a proteção da sessão; tente novamente ao entrar.', csrfReady ? 'sucesso' : 'info')
      navigate('/')
    } catch (error) { notify(error.message, 'erro') }
    finally { setLogoutBusy(false) }
  }
  async function updateProfile(values) {
    const user = await api('/usuarios/me', { method: 'PATCH', body: values })
    generation.current += 1
    setSession({ user, loading: false, error: null })
    return user
  }
  const refreshSession = useCallback(() => setVersion((value) => value + 1), [])
  return <AuthContext value={{ ...session, authenticate, updateProfile, logout, logoutBusy, notify, notice, clearNotice, refreshSession }}>{children}</AuthContext>
}
