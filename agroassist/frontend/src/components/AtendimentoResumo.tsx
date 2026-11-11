import type { Atendimento } from '../domain/tipos'
import { formatarData } from '../utils/formato'

export function AtendimentoResumo({ atendimento }: { atendimento: Atendimento }) {
  return (
    <>
      <dl className="kv">
        <dt>Data</dt>
        <dd>{formatarData(atendimento.data)}</dd>
        <dt>Modalidade</dt>
        <dd>{atendimento.modalidade}</dd>
      </dl>
      <div>
        <span className="hint">Diagnóstico</span>
        <p className="texto">{atendimento.diagnostico}</p>
      </div>
      <div>
        <span className="hint">Recomendação</span>
        <p className="texto">{atendimento.recomendacao}</p>
      </div>
    </>
  )
}
