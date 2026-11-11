import type { Status } from '../domain/tipos'

const CLASSE: Record<Status, string> = {
  Aberta: 'st-aberta',
  Atribuída: 'st-atrib',
  'Em atendimento': 'st-atend',
  Concluída: 'st-concl',
  Avaliada: 'st-aval',
}

export function StatusPill({ status }: { status: Status }) {
  return <span className={`pill ${CLASSE[status]}`}>{status}</span>
}
