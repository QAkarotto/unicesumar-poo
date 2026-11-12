import { useEffect, useState } from 'react'
import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import { TournamentBadges } from '../components/TournamentCard.jsx'
import { Standings, Bracket, Participants, Results } from '../components/Competition.jsx'
import { Message } from '../components/Feedback.jsx'
import { dateLabel } from '../lib/format.js'

export default function TournamentPage({ data, user, onFinalize, busy, error }) {
  const { torneio: tournament, participantes: participants, partidas: matches = [], classificacao: standings = [], chaveamento: rounds = [] } = data
  const owner = user?.id === tournament.criador.id
  const locked = tournament.status === 'FINALIZADO'
  const [active, setActive] = useState('competicao')
  useEffect(() => { document.title = tournament.nome + ' | SeuGame' }, [tournament.nome])
  useEffect(() => {
    const observer = new IntersectionObserver((entries) => {
      for (const entry of entries) if (entry.isIntersecting) setActive(entry.target.id)
    }, { rootMargin: '-25% 0px -65% 0px' })
    document.querySelectorAll('.tournament-section[id]').forEach((section) => observer.observe(section))
    return () => observer.disconnect()
  }, [])
  return <>
    <section className="tournament-hero"><div className="tournament-breadcrumb"><Link href="/">Torneios</Link><Icon name="chevron" /><span>{tournament.nome}</span></div>
      <div className="tournament-title-row"><div><TournamentBadges tournament={tournament} /><h1>{tournament.nome}</h1><p>{tournament.descricao || 'Competição organizada pelo SeuGame.'}</p>
        <div className="tournament-meta"><span><Icon name="user" /> {tournament.criador.nome}</span><span><Icon name="users" /> {participants.length} participantes</span><span><Icon name="calendar" /> {dateLabel(tournament.dataInicio)}</span></div></div>
        {owner ? <div className="organizer-actions">{locked ? <span className="locked-label"><Icon name="lock" /> Competição encerrada</span> : <>
          <Link className="btn btn-primary" href={`/torneios/${tournament.id}/placares`}><Icon name="edit" />Registrar placares</Link><Link className="btn btn-ghost" href={`/torneios/${tournament.id}/editar`}><Icon name="edit" />Editar</Link>
          <button className="btn btn-success" onClick={onFinalize} disabled={busy}><Icon name="flag" />{busy ? 'Finalizando…' : 'Finalizar'}</button></>}</div> : null}
      </div>
      {tournament.campeao ? <div className="champion-banner"><span className="champion-icon"><Icon name="trophy" /></span><div><span>Campeão do torneio</span><strong>{tournament.campeao.nome}</strong></div></div> : null}
      {error ? <div className="action-feedback"><Message message={error.message} errors={error.fields} /></div> : null}
    </section>
    <nav className="section-tabs" aria-label="Seções do torneio">{[['competicao', tournament.tipo === 'TABELA' ? 'Classificação' : 'Chaveamento'], ['resultados', 'Resultados', matches.filter((match) => match.placar1 !== null).length], ['participantes', 'Participantes', participants.length]].map(([id, label, count]) =>
      <a key={id} className={active === id ? 'active' : ''} href={`#${id}`} onClick={() => setActive(id)}>{label}{count != null ? <span>{count}</span> : null}</a>)}</nav>
    <section id="competicao" className="content-section tournament-section"><div className="section-heading"><div><span className="eyebrow">{tournament.tipo === 'TABELA' ? 'Pontos corridos' : 'Caminho até a final'}</span><h2>{tournament.tipo === 'TABELA' ? 'Classificação' : 'Chaveamento'}</h2></div>
      {!locked ? <span className="live-note"><Icon name="refresh" /> Atualizado após cada placar</span> : null}</div>
      {tournament.tipo === 'TABELA' ? <><Standings rows={standings} champion={tournament.campeao} /><p className="competition-caption">Desempate: pontos, vitórias, saldo de gols, gols marcados e nome.</p></> : <Bracket rounds={rounds} />}
    </section>
    <section id="resultados" className="content-section tournament-section alt-section"><div className="section-heading"><div><span className="eyebrow">Placar registrado</span><h2>Resultados</h2></div>
      {owner && !locked ? <Link className="btn btn-small btn-primary" href={`/torneios/${tournament.id}/placares`}>Gerenciar placares</Link> : null}</div><Results matches={matches} type={tournament.tipo} rounds={rounds} /></section>
    <section id="participantes" className="content-section tournament-section"><div className="section-heading"><div><span className="eyebrow">Competidores</span><h2>Participantes</h2></div></div><Participants participants={participants} champion={tournament.campeao} /></section>
  </>
}
