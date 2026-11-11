import type { Banco, Solicitacao } from '../domain/tipos'

function encontrar<T extends { id: string }>(lista: T[], id: string | null, tipo: string): T {
  const item = lista.find((x) => x.id === id)
  if (!item) throw new Error(`${tipo} ${id} não encontrado`)
  return item
}

export const usuario = (b: Banco, id: string) => encontrar(b.usuarios, id, 'Usuário')
export const cultura = (b: Banco, id: string) => encontrar(b.culturas, id, 'Cultura')
export const propriedade = (b: Banco, id: string) => encontrar(b.propriedades, id, 'Propriedade')
export const tecnico = (b: Banco, id: string) => encontrar(b.tecnicos, id, 'Técnico')

export const tecnicoDoUsuario = (b: Banco, usuarioId: string) =>
  b.tecnicos.find((t) => t.usuarioId === usuarioId)
export const atendimentoDe = (b: Banco, solicitacaoId: string) =>
  b.atendimentos.find((a) => a.solicitacaoId === solicitacaoId)
export const avaliacaoDe = (b: Banco, solicitacaoId: string) =>
  b.avaliacoes.find((a) => a.solicitacaoId === solicitacaoId)

export function nomeDoTecnico(b: Banco, tecnicoId: string | null): string {
  const t = b.tecnicos.find((x) => x.id === tecnicoId)
  return t ? usuario(b, t.usuarioId).nome : ''
}

export function notasDoTecnico(b: Banco, tecnicoId: string): number[] {
  return b.avaliacoes
    .filter((a) => b.solicitacoes.find((s) => s.id === a.solicitacaoId)?.tecnicoId === tecnicoId)
    .map((a) => a.nota)
}

export function maisRecentes(lista: Solicitacao[]): Solicitacao[] {
  return [...lista].sort((a, b) => b.criadaEm.localeCompare(a.criadaEm))
}
