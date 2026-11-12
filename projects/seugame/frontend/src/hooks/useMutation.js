import { useRef, useState } from 'react'

export default function useMutation() {
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState(null)
  const running = useRef(false)
  async function run(action) {
    if (running.current) return
    running.current = true
    setBusy(true)
    setError(null)
    try { return await action() }
    catch (failure) { setError(failure); return undefined }
    finally { running.current = false; setBusy(false) }
  }
  return { busy, error, run }
}
