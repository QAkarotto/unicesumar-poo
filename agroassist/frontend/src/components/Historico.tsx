import type { Solicitacao } from '../domain/tipos'
import { useBanco } from '../services/store'
import { formatarDataHora } from '../utils/formato'

export function Historico({ solicitacao }: { solicitacao: Solicitacao }) {
  const banco = useBanco()
  const eventos = [...solicitacao.historico].reverse()
  return (
    <ol className="tl">
      {eventos.map((evento) => (
        <li key={`${evento.em}-${evento.status}-${evento.descricao}`}>
          <span className="when">
            {formatarDataHora(evento.em)} · {banco.usuarios.find((u) => u.id === evento.autorId)?.nome}
          </span>
          {evento.descricao}
        </li>
      ))}
    </ol>
  )
}
