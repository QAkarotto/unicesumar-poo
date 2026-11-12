import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import ScoreForm from '../components/ScoreForm.jsx'
import { Message } from '../components/Feedback.jsx'
import { formatNames, matchGroups } from '../lib/format.js'

export default function ScoresPage({ data, onSave, busy, error, notice, feedbackId }) {
  const { torneio: tournament, partidas: matches, chaveamento: rounds = [] } = data
  return <section className="content-section narrow"><div className="page-title-row"><div><span className="eyebrow">Resultados</span><h1>Registrar placares</h1><p>{tournament.nome} · {formatNames[tournament.tipo]}</p></div>
    <Link className="btn btn-ghost" href={`/torneios/${tournament.id}`}><Icon name="back" />Ver torneio</Link></div>
    <Message type="info" message="Você pode corrigir placares já lançados. A classificação ou o chaveamento será recalculado automaticamente." />
    {!feedbackId ? <Message message={error?.message} errors={error?.fields} /> : null}
    <div className="score-rounds">{matchGroups(matches).map((round) => <section className="score-round" key={round.rodada}><div className="score-round-heading"><div><span>{String(round.rodada).padStart(2, '0')}</span>
      <h2>{tournament.tipo === 'TABELA' ? `Rodada ${round.rodada}` : rounds.find((item) => item.rodada === round.rodada)?.nome || `Fase ${round.rodada}`}</h2></div>
      <small>{round.partidas.filter((match) => match.placar1 !== null).length}/{round.partidas.filter((match) => match.participante1 && match.participante2).length} concluídos</small></div>
      <div className="score-list">{round.partidas.map((match) => <Fragment key={match.id}>
        <ScoreForm key={`${match.id}:${match.participante1?.id}:${match.participante2?.id}:${match.placar1}:${match.placar2}`} match={match} onSave={onSave} busy={busy} />
        {feedbackId === match.id ? <div className="score-feedback"><Message message={error?.message} errors={error?.fields} /><Message type="sucesso" message={error ? '' : notice} /></div> : null}
      </Fragment>)}</div>
    </section>)}</div>
  </section>
}
import { Fragment } from 'react'
