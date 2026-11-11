// Estas verificações orientam a interface. O backend também valida as regras.
import type { Banco, Propriedade, Solicitacao, Status, Tecnico } from './tipos'

export type ResultadoRegra = { ok: true } | { ok: false; motivo: string }

export const STATUS_QUE_ACEITAM_ATRIBUICAO: Status[] = ['Aberta', 'Atribuída']

export function culturaPertenceAPropriedade(propriedade: Propriedade, culturaId: string): boolean {
  return propriedade.culturaIds.includes(culturaId)
}

export function verificarAtribuicao(
  tecnico: Tecnico,
  solicitacao: Solicitacao,
  nomeCultura: string,
): ResultadoRegra {
  if (!STATUS_QUE_ACEITAM_ATRIBUICAO.includes(solicitacao.status))
    return { ok: false, motivo: 'atendimento já iniciado' }
  if (!tecnico.ativo) return { ok: false, motivo: 'registro inativo' }
  if (!tecnico.culturaIds.includes(solicitacao.culturaId))
    return { ok: false, motivo: `não habilitado em ${nomeCultura}` }
  return { ok: true }
}

export function ehTecnicoResponsavel(banco: Banco, usuarioId: string, solicitacao: Solicitacao): boolean {
  const tecnico = banco.tecnicos.find((t) => t.id === solicitacao.tecnicoId)
  return !!tecnico && tecnico.usuarioId === usuarioId
}

export const podeVerContatoDoProdutor = ehTecnicoResponsavel

export function podeAvaliar(banco: Banco, usuarioId: string, solicitacao: Solicitacao): boolean {
  return (
    solicitacao.status === 'Concluída' &&
    solicitacao.produtorId === usuarioId &&
    !banco.avaliacoes.some((a) => a.solicitacaoId === solicitacao.id)
  )
}
