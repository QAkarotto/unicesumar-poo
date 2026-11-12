import Icon from './Icon.jsx'

export default function ScoreForm({ match, onSave, busy }) {
  const available = Boolean(match.participante1 && match.participante2)
  const first = match.participante1?.nome || (match.rodada === 1 ? 'BYE' : 'A definir')
  const second = match.participante2?.nome || (match.rodada === 1 ? 'BYE' : 'A definir')
  function submit(event) {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    onSave?.(match.id, { placar1: Number(data.get('placar1')), placar2: Number(data.get('placar2')) })
  }
  return <form id={`partida-${match.id}`} className={`score-form${available ? '' : ' disabled'}`} onSubmit={submit} aria-label={`Placar: ${first} contra ${second}`}>
    <span className="team team-left" title={first}>{first}</span><div className="score-inputs">
      <input type="number" name="placar1" defaultValue={match.placar1 ?? ''} min="0" max="999" step="1" inputMode="numeric" aria-label={`Placar de ${first}`} disabled={!available || busy} required />
      <span>:</span><input type="number" name="placar2" defaultValue={match.placar2 ?? ''} min="0" max="999" step="1" inputMode="numeric" aria-label={`Placar de ${second}`} disabled={!available || busy} required />
    </div><span className="team team-right" title={second}>{second}</span>
    {available ? <button className={`btn btn-small ${match.placar1 !== null ? 'btn-ghost' : 'btn-primary'}`} disabled={busy}><Icon name="save" />{match.placar1 !== null ? 'Corrigir' : 'Salvar'}</button>
      : <span className="waiting-label">{match.rodada === 1 ? 'BYE' : 'Aguardando'}</span>}
  </form>
}
