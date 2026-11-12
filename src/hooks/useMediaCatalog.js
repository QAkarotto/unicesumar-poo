import { useCallback, useEffect, useState } from 'react'
import { getMediaCatalog } from '../services/api'

export function useMediaCatalog() {
  const [result, setResult] = useState({ items: null, error: false })
  const [reload, setReload] = useState(0)

  useEffect(() => {
    let active = true
    getMediaCatalog()
      .then((items) => { if (active) setResult({ items, error: false }) })
      .catch(() => { if (active) setResult({ items: [], error: true }) })
    return () => { active = false }
  }, [reload])

  const retry = useCallback(() => {
    setResult({ items: null, error: false })
    setReload((current) => current + 1)
  }, [])

  return { items: result.items || [], loading: result.items === null && !result.error, error: result.error, retry }
}
