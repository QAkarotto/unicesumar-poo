import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import { TournamentBadges } from '../components/TournamentCard.jsx'
import { Empty, Loading, Message } from '../components/Feedback.jsx'
import { dateLabel } from '../lib/format.js'

export default function MyTournamentsPage({ tournaments = [], loading, error, retry, onDelete, busy }) {
  return <section className="content-section"><div className="page-title-row"><div><span className="eyebrow">Seus torneios</span><h1>Meus torneios</h1><p>Acompanhe as competições que você organiza.</p></div>
    <Link className="btn btn-primary" href="/torneios/novo"><Icon name="plus" />Novo torneio</Link></div>
    <Message message={error?.message} />
    {loading ? <Loading /> : error && !tournaments.length ? <button className="btn btn-ghost" onClick={retry}>Tentar novamente</button> : tournaments.length ? <div className="manage-list">{tournaments.map((tournament) => <article className="manage-card" key={tournament.id}>
      <div className="manage-main"><TournamentBadges tournament={tournament} /><h2>{tournament.nome}</h2><p>{tournament.totalParticipantes != null ? `${tournament.totalParticipantes} participantes · ${tournament.resultados} resultados · ` : ''}{dateLabel(tournament.dataInicio)}</p>
        {tournament.campeao ? <span className="champion-line"><Icon name="trophy" /> Campeão: <strong>{tournament.campeao.nome}</strong></span> : null}</div>
      <div className="manage-actions"><Link className="btn btn-primary" href={`/torneios/${tournament.id}`}><Icon name="eye" />Visualizar</Link>
        {tournament.status !== 'FINALIZADO' ? <><Link className="btn btn-ghost" href={`/torneios/${tournament.id}/placares`}><Icon name="edit" />Placares</Link>
          <Link className="icon-button" href={`/torneios/${tournament.id}/editar`} aria-label={`Editar ${tournament.nome}`}><Icon name="edit" /></Link>
          <button className="icon-button danger" type="button" aria-label={`Excluir ${tournament.nome}`} disabled={busy} onClick={() => onDelete?.(tournament)}><Icon name="trash" /></button></> : null}</div>
    </article>)}</div> : <Empty title="Sua arena ainda está vazia" action={<Link className="btn btn-primary" href="/torneios/novo">Criar primeiro torneio</Link>}>Crie um torneio para gerar partidas e começar a disputa.</Empty>}
  </section>
}
