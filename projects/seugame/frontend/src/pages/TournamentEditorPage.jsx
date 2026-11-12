import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import TournamentForm from '../components/TournamentForm.jsx'

export default function TournamentEditorPage({ initial, matches = [], onSubmit, busy, error }) {
  const editing = Boolean(initial)
  return <section className="content-section narrow"><div className="page-title-row"><div><span className="eyebrow">{editing ? 'Configurações' : 'Novo torneio'}</span>
    <h1>{editing ? 'Editar torneio' : 'Criar torneio'}</h1><p>{editing ? 'Atualize os dados disponíveis desta competição.' : 'Informe os dados e participantes. Os confrontos serão sorteados ao salvar.'}</p></div>
    <Link className="btn btn-ghost" href={editing ? `/torneios/${initial.torneio.id}` : '/meus-torneios'}><Icon name="back" />Voltar</Link></div>
    <TournamentForm initial={initial} editing={editing} onSubmit={onSubmit} busy={busy} error={error}
      formatLocked={editing && initial.torneio.status !== 'FUTURO'} participantsLocked={matches.some((match) => match.placar1 !== null && match.placar2 !== null)} />
  </section>
}
