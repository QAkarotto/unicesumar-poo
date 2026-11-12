import Icon from './Icon.jsx'
import { matchLabel } from '../lib/format.js'

export function Standings({ rows, champion }) {
  return <div className="table-card"><div className="table-scroll" tabIndex={0} role="region" aria-label="Tabela de classificação; role horizontalmente para ver todas as colunas"><table className="standings-table">
    <caption className="sr-only">Classificação interna do torneio</caption>
    <thead><tr>{[['Pos.', 'Posição'], ['Participante', 'Participante'], ['J', 'Jogos'], ['V', 'Vitórias'], ['E', 'Empates'], ['D', 'Derrotas'], ['GP', 'Gols pró'], ['GC', 'Gols contra'], ['SG', 'Saldo de gols'], ['Pts', 'Pontos']].map(([text, title]) => <th key={text} scope="col" title={title}>{text}</th>)}</tr></thead>
    <tbody>{rows.map((row) => <tr key={row.participante.id} className={row.posicao === 1 ? 'leader' : ''}>
      <td><span className="position">{row.posicao}</span></td><td><strong>{row.participante.nome}</strong>{row.posicao === 1 ? <small>{champion ? 'Campeão' : 'Líder'}</small> : null}</td>
      <td>{row.jogos}</td><td>{row.vitorias}</td><td>{row.empates}</td><td>{row.derrotas}</td><td>{row.golsPro}</td><td>{row.golsContra}</td><td>{row.saldo}</td><td><strong className="points">{row.pontos}</strong></td>
    </tr>)}</tbody>
  </table></div><div className="table-legend"><span><b>J</b> Jogos</span><span><b>V</b> Vitórias</span><span><b>E</b> Empates</span><span><b>D</b> Derrotas</span><span><b>SG</b> Saldo de gols</span></div></div>
}
export function Bracket({ rounds }) {
  return <div className="bracket-scroll" tabIndex={0} role="region" aria-label="Chaveamento; role horizontalmente para ver as fases"><div className="bracket" style={{ '--rounds': Math.max(1, rounds.length) }}>
    {rounds.map((round) => <div className="bracket-round" key={round.rodada}><h3>{round.nome}</h3><div className="round-matches">
      {round.partidas.map((match) => <article className="bracket-match" key={match.id} aria-label={`${round.nome}, confronto ${match.ordem}`}>
        {[1, 2].map((side) => {
          const participant = match[`participante${side}`]
          const bye = match.rodada === 1 && (!match.participante1 || !match.participante2)
          return <div key={side} className={participant && participant.id === match.vencedor?.id ? 'winner' : ''}>
            <span title={participant?.nome}>{participant?.nome || (bye ? 'BYE' : 'A definir')}</span><strong>{match[`placar${side}`] ?? (bye && participant ? 'BYE' : '—')}</strong>
          </div>
        })}
      </article>)}
    </div></div>)}
  </div></div>
}
export function Participants({ participants, champion }) {
  return <div className="participant-cards">{participants.map((participant, index) => <div key={participant.id}><span>{index + 1}</span><strong title={participant.nome}>{participant.nome}</strong>
    {participant.id === champion?.id ? <span className="champion-participant" title="Campeão"><Icon name="trophy" /><span className="sr-only">Campeão</span></span> : null}</div>)}</div>
}
export function Results({ matches, type, rounds }) {
  const completed = matches.filter((match) => match.placar1 !== null && match.placar2 !== null)
    .toSorted((first, second) => second.rodada - first.rodada || first.ordem - second.ordem)
  if (!completed.length) return <div className="empty-inline"><Icon name="clock" /><div><h3>Nenhum placar registrado</h3><p>Os resultados aparecerão aqui durante a competição.</p></div></div>
  return <div className="results-grid">{completed.map((match) => <article className="result-card" key={match.id}><div className="result-meta"><span>{matchLabel(match, type, rounds)}</span><span>Finalizado</span></div>
    <div className="result-score"><span title={match.participante1.nome}>{match.participante1.nome}</span><strong>{match.placar1}<i>:</i>{match.placar2}</strong><span title={match.participante2.nome}>{match.participante2.nome}</span></div></article>)}</div>
}
