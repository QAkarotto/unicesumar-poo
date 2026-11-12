import { useAuth } from '../auth/AuthContext.js'
import CatalogPage from '../pages/CatalogPage.jsx'
import useResource from '../hooks/useResource.js'
import { navigate } from '../lib/router.js'

export default function Catalog({ search }) {
  const { user } = useAuth()
  const resource = useResource('/torneios' + (search ? '?' + search : ''))
  return <CatalogPage tournaments={resource.data || []} user={user} search={search} loading={resource.loading} error={resource.error} retry={resource.reload}
    onFilter={(params) => navigate(params ? '/?' + params : '/')} />
}
