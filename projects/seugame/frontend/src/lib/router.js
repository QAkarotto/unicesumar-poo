import { useSyncExternalStore } from 'react'

function subscribe(callback) {
  window.addEventListener('popstate', callback)
  return () => window.removeEventListener('popstate', callback)
}
function snapshot() { return window.location.pathname + window.location.search }
export function useLocation() { return useSyncExternalStore(subscribe, snapshot) }
export function navigate(to, { replace = false } = {}) {
  const oldPath = window.location.pathname
  window.history[replace ? 'replaceState' : 'pushState'](null, '', to)
  window.dispatchEvent(new PopStateEvent('popstate'))
  if (oldPath !== window.location.pathname) window.scrollTo(0, 0)
}
export function returnPath(search) {
  const value = new URLSearchParams(search).get('voltar')
  return value?.startsWith('/') && !value.startsWith('//') && !value.includes('\\')
    && !/^\/(login|cadastro)([/?#]|$)/.test(value) ? value : '/'
}
export function resolveRoute(location) {
  const [path, search = ''] = location.split('?')
  const fixed = { '/': 'catalog', '/login': 'login', '/cadastro': 'signup', '/perfil': 'profile', '/meus-torneios': 'mine', '/torneios/novo': 'create' }
  if (fixed[path]) return { page: fixed[path], path, search }
  const match = path.match(/^\/torneios\/(\d+)(?:\/(editar|placares))?\/?$/)
  return match ? { page: match[2] === 'editar' ? 'edit' : match[2] === 'placares' ? 'scores' : 'detail', id: match[1], path, search }
    : { page: 'notFound', path, search }
}
