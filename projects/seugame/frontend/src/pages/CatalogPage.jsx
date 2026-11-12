import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import TournamentCard from '../components/TournamentCard.jsx'
import { Empty, Loading, Message } from '../components/Feedback.jsx'

const groups = [['ANDAMENTO', 'Em andamento', 'play'], ['FUTURO', 'Próximos torneios', 'calendar'], ['FINALIZADO', 'Finalizados', 'flag']]
export default function CatalogPage({ tournaments = [], user, search = '', onFilter, loading, error, retry }) {
  const filters = new URLSearchParams(search)
  const create = user ? '/torneios/novo' : '/cadastro'
  function filter(event) {
    event.preventDefault()
    const params = new URLSearchParams()
    for (const [name, value] of new FormData(event.currentTarget)) if (value.trim()) params.set(name, value.trim())
    onFilter?.(params.toString())
  }
  return <>
    <section className="hero"><div><span className="eyebrow">Organização de torneios</span><h1>Torneios do seu jeito.</h1><p>Crie a disputa, lance os resultados e acompanhe a classificação. Sem planilha e sem confusão.</p>
      <div className="hero-actions"><Link className="btn btn-primary" href={create}>Criar torneio</Link><a className="btn btn-ghost" href="#torneios">Ver torneios</a></div></div></section>
    <section id="torneios" className="content-section"><div className="section-heading"><div><span className="eyebrow">Em destaque</span><h2>Torneios da comunidade</h2></div>
      <span className="result-count">{loading ? 'Consultando…' : `${tournaments.length} encontrado${tournaments.length === 1 ? '' : 's'}`}</span></div>
      <form className="filter-bar" onSubmit={filter} key={search}>
        <label className="search-field"><Icon name="search" /><input type="search" name="busca" defaultValue={filters.get('busca') || ''} placeholder="Pesquisar torneio" aria-label="Pesquisar torneio" maxLength={120} /></label>
        <select name="tipo" defaultValue={filters.get('tipo') || ''} aria-label="Filtrar por tipo"><option value="">Todos os formatos</option><option value="TABELA">Tabela</option><option value="CHAVEAMENTO">Chaveamento</option></select>
        <select name="status" defaultValue={filters.get('status') || ''} aria-label="Filtrar por status"><option value="">Todos os status</option><option value="ANDAMENTO">Em andamento</option><option value="FUTURO">Futuros</option><option value="FINALIZADO">Finalizados</option></select>
        <button className="btn btn-primary" type="submit" disabled={loading}>Filtrar</button>{search ? <Link className="btn btn-ghost" href="/">Limpar</Link> : null}
      </form>
      {loading ? <Loading message="Buscando torneios…" /> : error ? <><Message message={error.message} /><button className="btn btn-ghost" onClick={retry}>Tentar novamente</button></> : tournaments.length ?
        groups.map(([status, label, icon]) => {
          const items = tournaments.filter((tournament) => tournament.status === status)
          return items.length ? <div className="tournament-group" key={status}><h3><Icon name={icon} />{label}<span>{items.length}</span></h3><div className="card-grid">{items.map((tournament) => <TournamentCard key={tournament.id} tournament={tournament} />)}</div></div> : null
        }) : <Empty title="Nenhum torneio encontrado" action={<Link className="btn btn-primary" href={create}>Criar torneio</Link>}>Ajuste os filtros ou crie a primeira competição da arena.</Empty>}
    </section>
  </>
}
