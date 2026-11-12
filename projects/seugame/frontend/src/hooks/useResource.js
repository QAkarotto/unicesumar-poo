import { useCallback, useEffect, useState } from 'react'
import { api } from '../services/api.js'

const readApi = (path, signal) => api(path, { signal })

export default function useResource(key, loader = readApi) {
  const [version, setVersion] = useState(0)
  const [state, setState] = useState(null)
  useEffect(() => {
    const controller = new AbortController()
    let active = true
    loader(key, controller.signal).then(
      (data) => { if (active) setState({ key, version, data, error: null }) },
      (error) => { if (active) setState((previous) => ({ key, version, data: previous?.key === key ? previous.data : null, error })) },
    )
    return () => { active = false; controller.abort() }
  }, [key, loader, version])
  const reload = useCallback(() => setVersion((value) => value + 1), [])
  const current = state?.key === key && state.version === version
  return { data: state?.key === key ? state.data : null, error: current ? state.error : null, loading: !current, reload }
}
