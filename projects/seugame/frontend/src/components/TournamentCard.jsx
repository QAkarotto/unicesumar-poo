import Icon from './Icon.jsx'
import Link from './Link.jsx'
import { dateLabel, formatNames, statusNames } from '../lib/format.js'

export function TournamentBadges({ tournament }) {
  return <div className="card-topline"><span className={`status status-${tournament.status.toLowerCase()}`}>{statusNames[tournament.status]}</span>
    <span className="format"><Icon name={tournament.tipo === 'TABELA' ? 'table' : 'bracket'} />{formatNames[tournament.tipo]}</span></div>
}
export default function TournamentCard({ tournament }) {
  return <article className="tournament-card"><TournamentBadges tournament={tournament} />
    <h4 title={tournament.nome}>{tournament.nome}</h4><p className="card-description">{tournament.descricao || 'Competição organizada no SeuGame.'}</p>
    {tournament.campeao ? <div className="champion-mini"><Icon name="trophy" /><span>Campeão</span><strong>{tournament.campeao.nome}</strong></div> : null}
    <dl className="card-details"><div><dt><Icon name="users" />Participantes</dt><dd>{tournament.totalParticipantes}</dd></div><div><dt><Icon name="calendar" />Data</dt><dd>{dateLabel(tournament.dataInicio)}</dd></div></dl>
    <div className="card-footer"><small title={tournament.criador.nome}>por {tournament.criador.nome}</small><Link className="btn btn-small btn-primary" href={`/torneios/${tournament.id}`}>Visualizar</Link></div>
  </article>
}
