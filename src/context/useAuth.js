import { useContext } from 'react'
import { AuthContext } from './auth-context'

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth precisa estar dentro de AuthProvider.')
  return context
}
