export const statusNames = { FUTURO: 'Futuro', ANDAMENTO: 'Em andamento', FINALIZADO: 'Finalizado' }
export const formatNames = { TABELA: 'Tabela', CHAVEAMENTO: 'Chaveamento' }
export function dateLabel(date) {
  if (!date) return 'Data não informada'
  const [year, month, day] = date.split('-')
  return `${day}/${month}/${year}`
}
export function today() {
  const date = new Date()
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
export function matchGroups(matches) {
  return [...Map.groupBy(matches, (match) => match.rodada)].map(([rodada, partidas]) => ({ rodada, partidas }))
}
export function matchLabel(match, type, rounds) {
  return type === 'TABELA' ? `Rodada ${match.rodada}` : rounds.find((round) => round.rodada === match.rodada)?.nome || `Fase ${match.rodada}`
}
