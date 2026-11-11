import { createContext, useCallback, useContext, useRef, useState, type ReactNode } from 'react'

const ToastContext = createContext<(mensagem: string) => void>(() => undefined)

export function ToastProvider({ children }: { children: ReactNode }) {
  const [mensagem, setMensagem] = useState<string | null>(null)
  const timer = useRef<number>()

  const mostrar = useCallback((texto: string) => {
    setMensagem(texto)
    window.clearTimeout(timer.current)
    timer.current = window.setTimeout(() => setMensagem(null), 3200)
  }, [])

  return (
    <ToastContext.Provider value={mostrar}>
      {children}
      <div className="toast" role="status" aria-live="polite" hidden={!mensagem}>
        {mensagem}
      </div>
    </ToastContext.Provider>
  )
}

export function useToast() {
  return useContext(ToastContext)
}
