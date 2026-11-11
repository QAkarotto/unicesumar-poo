import { ETAPAS, type Status } from '../domain/tipos'

export function Etapas({ status }: { status: Status }) {
  const atual = ETAPAS.indexOf(status)
  return (
    <ol className="stepper" aria-label={`Etapa atual: ${status}`}>
      {ETAPAS.map((etapa, i) => (
        <li
          key={etapa}
          className={i < atual ? 'done' : i === atual ? 'cur' : undefined}
          aria-current={i === atual ? 'step' : undefined}
        >
          <span className="bar" />
          {etapa}
        </li>
      ))}
    </ol>
  )
}
