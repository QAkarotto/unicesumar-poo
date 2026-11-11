import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from 'react'
import { PRODUTOR_PADRAO, TECNICO_PADRAO } from '../data/constantes'

interface Sessao {
  produtorId: string | null
  tecnicoUsuarioId: string
  textoGrande: boolean
  entrarComoProdutor: (id: string) => void
  sair: () => void
  trocarTecnico: (usuarioId: string) => void
  alternarTextoGrande: () => void
  reiniciarSessao: () => void
}

const SessaoContext = createContext<Sessao | null>(null)

export function SessaoProvider({ children }: { children: ReactNode }) {
  const [produtorId, setProdutorId] = useState<string | null>(PRODUTOR_PADRAO)
  const [tecnicoUsuarioId, setTecnicoUsuarioId] = useState(TECNICO_PADRAO)
  const [textoGrande, setTextoGrande] = useState(false)

  const reiniciarSessao = useCallback(() => {
    setProdutorId(PRODUTOR_PADRAO)
    setTecnicoUsuarioId(TECNICO_PADRAO)
    setTextoGrande(false)
  }, [])

  const valor = useMemo<Sessao>(
    () => ({
      produtorId,
      tecnicoUsuarioId,
      textoGrande,
      entrarComoProdutor: setProdutorId,
      sair: () => setProdutorId(null),
      trocarTecnico: setTecnicoUsuarioId,
      alternarTextoGrande: () => setTextoGrande((v) => !v),
      reiniciarSessao,
    }),
    [produtorId, tecnicoUsuarioId, textoGrande, reiniciarSessao],
  )

  return <SessaoContext.Provider value={valor}>{children}</SessaoContext.Provider>
}

export function useSessao(): Sessao {
  const sessao = useContext(SessaoContext)
  if (!sessao) throw new Error('useSessao precisa estar dentro de SessaoProvider')
  return sessao
}

export function useProdutorId(): string {
  const { produtorId } = useSessao()
  if (!produtorId) throw new Error('Nenhum produtor conectado')
  return produtorId
}
