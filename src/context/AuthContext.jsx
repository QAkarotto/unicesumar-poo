import { useCallback, useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { accountApi } from '../services/api'
import { AuthContext } from './auth-context'
const blankLibrary = { favoritos: [], pauta: {} }

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [library, setLibrary] = useState(blankLibrary)
  const [ready, setReady] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()

  // evita mostrar a lista vazia antes de recuperar a sessão salva
  useEffect(() => {
    let active = true
    accountApi.me()
      .then(async (profile) => {
        const saved = profile ? await accountApi.library(profile) : blankLibrary
        if (active) {
          setUser(profile)
          setLibrary(saved)
        }
      })
      .catch(() => {
        if (active) {
          setUser(null)
          setLibrary(blankLibrary)
        }
      })
      .finally(() => { if (active) setReady(true) })

    return () => { active = false }
  }, [])

  const enter = useCallback(async (credentials, createAccount = false) => {
    const profile = createAccount
      ? await accountApi.register(credentials)
      : await accountApi.login(credentials)
    setUser(profile)
    setLibrary(await accountApi.library(profile))
    return profile
  }, [])

  const logout = useCallback(async () => {
    await accountApi.logout()
    setUser(null)
    setLibrary(blankLibrary)
    navigate('/')
  }, [navigate])

  const goToLogin = useCallback(() => {
    const returnTo = `${location.pathname}${location.search}`
    navigate(`/entrar?volta=${encodeURIComponent(returnTo)}`)
  }, [location.pathname, location.search, navigate])

  // executa uma ação na lista pessoal e trata os erros mais comuns da API
  const runLibraryAction = useCallback(async (action) => {
    if (!user) return goToLogin()
    try {
      setLibrary(await action())
    } catch (cause) {
      if (cause.status === 401) {
        // a sessão expirou no servidor
        setUser(null)
        setLibrary(blankLibrary)
        goToLogin()
      } else {
        window.alert(cause.message)
      }
    }
  }, [goToLogin, user])

  const toggleFavorite = useCallback(
    (mediaId) => runLibraryAction(() => accountApi.toggleFavorite(user, mediaId)),
    [runLibraryAction, user],
  )

  const toggleWatchlist = useCallback(
    (mediaId) => runLibraryAction(() => accountApi.toggleWatchlist(user, mediaId)),
    [runLibraryAction, user],
  )

  const updateWatchlist = useCallback(
    (mediaId, change) => runLibraryAction(() => accountApi.updateWatchlist(user, mediaId, change)),
    [runLibraryAction, user],
  )

  const value = {
    user,
    library,
    favorites: library.favoritos || [],
    watchlist: Object.keys(library.pauta || {}),
    ready,
    enter,
    logout,
    goToLogin,
    toggleFavorite,
    toggleWatchlist,
    updateWatchlist,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
