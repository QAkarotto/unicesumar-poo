import { useEffect, useMemo, useRef, useState } from 'react'
import { MoreHorizontal } from 'lucide-react'
import { Link, useSearchParams } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { getTypes } from '../utils/helpers'
import { handleFallbackImage } from '../utils/image'
import { useMediaCatalog } from '../hooks/useMediaCatalog'
import MediaCard from '../components/media/MediaCard'
import '../components/media/media.css'
import './MinhaLista.css'

const tiles = [
  { id: 'favoritos', label: 'Favoritos', tone: 'favoritos' },
  { id: 'a-fazer', label: 'Pendentes', tone: 'a-fazer' },
  { id: 'andamento', label: 'Em andamento', tone: 'andamento' },
  { id: 'concluido', label: 'Concluídas', tone: 'concluido' },
]

const statuses = [
  ['a-fazer', 'Pendentes'],
  ['andamento', 'Em andamento'],
  ['concluido', 'Concluído'],
]

const orders = [
  ['situacao', 'Situação'],
  ['nota', 'Melhor nota'],
  ['az', 'A–Z'],
]

const validFilters = new Set(tiles.map(({ id }) => id))

function EmAltaNoCatalogo({ items, loading, error, favorites, watchlist, onToggleFavorite, onToggleWatchlist, subtitle }) {
  const trending = useMemo(() => [...items].sort((a, b) => b.popularity - a.popularity).slice(0, 4), [items])

  if (loading || error) return null

  return (
    <section className="home-section lista-trending" aria-labelledby="lista-trending-title">
      <div className="home-section-heading">
        <h2 id="lista-trending-title">Em alta no catálogo</h2>
        {subtitle ? <p className="home-mono">{subtitle}</p> : null}
      </div>
      <div className="home-media-list">
        {trending.map((item) => (
          <MediaCard
            key={item.id}
            item={item}
            favorites={favorites}
            watchlist={watchlist}
            onToggleFavorite={onToggleFavorite}
            onToggleWatchlist={onToggleWatchlist}
          />
        ))}
      </div>
    </section>
  )
}

function ListaItem({ entry, index, onToggleFavorite, onToggleWatchlist, onStatusChange, onProgressChange }) {
  const { item, favorite, inWatchlist, status, progress } = entry
  const [menuOpen, setMenuOpen] = useState(false)
  const menuRef = useRef(null)
  const menuButtonRef = useRef(null)

  useEffect(() => {
    if (!menuOpen) return undefined

    function closeOutside(event) {
      if (!menuRef.current?.contains(event.target)) setMenuOpen(false)
    }

    function closeOnEscape(event) {
      if (event.key === 'Escape') {
        setMenuOpen(false)
        menuButtonRef.current?.focus()
      }
    }

    document.addEventListener('pointerdown', closeOutside)
    document.addEventListener('keydown', closeOnEscape)
    return () => {
      document.removeEventListener('pointerdown', closeOutside)
      document.removeEventListener('keydown', closeOnEscape)
    }
  }, [menuOpen])

  function moveMenuFocus(event) {
    if (event.key !== 'ArrowDown' && event.key !== 'ArrowUp') return
    event.preventDefault()
    menuRef.current?.querySelector('[role="menuitem"]')?.focus()
  }

  return (
    <article className="media-card lista-card" style={{ '--i': Math.min(index, 8) }}>
      <button className={`media-favorite ${favorite ? 'is-saved' : ''}`} type="button" aria-pressed={favorite} aria-label={`${favorite ? 'Remover dos favoritos' : 'Favoritar'} ${item.title}`} onClick={() => onToggleFavorite(item.id)}>
        <span aria-hidden="true">{favorite ? '♥' : '♡'}</span>
      </button>
      <Link to={`/obra/${item.id}`} className="media-cover" aria-label={`Ver os detalhes de ${item.title}, nota ${item.rating}`}>
        <img src={item.cover} alt={`Capa de ${item.title}`} onError={handleFallbackImage} loading="lazy" decoding="async" />
        <span className="media-rating" aria-label={`Nota ${item.rating}`}>{item.rating}</span>
      </Link>
      <p className="media-meta">{[...getTypes(item), item.year, item.studio].filter(Boolean).join(' · ')}</p>
      <h3><Link to={`/obra/${item.id}`}>{item.title}</Link></h3>
      <ul className="media-tropes">{(item.tropes || []).slice(0, 2).map((trope) => <li key={trope}>{trope}</li>)}</ul>

      <div className="media-foot lista-foot">
        {inWatchlist ? (
          <>
            <div className="lista-actions">
              <label className="lista-status" data-status={status}>
                <span className="sr-only">Situação de {item.title}</span>
                <select value={status} onChange={(event) => onStatusChange(item.id, event.target.value)}>
                  {statuses.map(([id, label]) => <option key={id} value={id}>{label}</option>)}
                </select>
              </label>
              <div className="lista-item-menu" ref={menuRef}>
                <button ref={menuButtonRef} type="button" className="lista-more-button" aria-label={`Mais ações para ${item.title}`} aria-haspopup="menu" aria-expanded={menuOpen} aria-controls={`lista-menu-${item.id}`} onClick={() => setMenuOpen((open) => !open)}>
                  <MoreHorizontal size={21} aria-hidden="true" />
                </button>
                {menuOpen ? (
                  <div className="lista-item-menu-panel" id={`lista-menu-${item.id}`} role="menu" onKeyDown={moveMenuFocus}>
                    <button type="button" role="menuitem" onClick={() => { setMenuOpen(false); onToggleWatchlist(item.id) }}>Tirar da lista</button>
                  </div>
                ) : null}
              </div>
            </div>
            {status === 'andamento' ? (
              <label className="lista-progress" style={{ '--progress': `${progress}%` }}>
                <span>Progresso <output>{progress}%</output></span>
                <input type="range" min="0" max="100" step="5" value={progress} onChange={(event) => onProgressChange(item.id, Number(event.target.value))} />
              </label>
            ) : null}
          </>
        ) : (
          <div className="media-actions lista-add">
            <button type="button" aria-pressed={false} onClick={() => onToggleWatchlist(item.id)}>Para ver/ler</button>
          </div>
        )}
      </div>
    </article>
  )
}

function ListaLogada() {
  const [searchParams, setSearchParams] = useSearchParams()
  const { items, loading, error, retry } = useMediaCatalog()
  const [order, setOrder] = useState('situacao')
  const { library, favorites, watchlist, toggleFavorite, toggleWatchlist, updateWatchlist } = useAuth()
  const pauta = useMemo(() => library.pauta || {}, [library.pauta])
  const currentFilter = validFilters.has(searchParams.get('filtro')) ? searchParams.get('filtro') : ''

  const allEntries = useMemo(() => items
    .filter((item) => favorites.includes(item.id) || watchlist.includes(item.id))
    .map((item) => ({
      item,
      favorite: favorites.includes(item.id),
      inWatchlist: Boolean(pauta[item.id]),
      status: pauta[item.id]?.status || 'a-fazer',
      progress: pauta[item.id]?.progresso || 0,
    })), [favorites, items, pauta, watchlist])

  const visibleEntries = useMemo(() => {
    const filtered = allEntries.filter((entry) => {
      if (!currentFilter) return true
      if (currentFilter === 'favoritos') return entry.favorite
      return entry.inWatchlist && entry.status === currentFilter
    })

    const weight = (entry) => entry.inWatchlist ? ['a-fazer', 'andamento', 'concluido'].indexOf(entry.status) : 9
    const comparators = {
      situacao: (a, b) => weight(a) - weight(b) || a.item.title.localeCompare(b.item.title, 'pt-BR'),
      nota: (a, b) => b.item.rating - a.item.rating,
      az: (a, b) => a.item.title.localeCompare(b.item.title, 'pt-BR'),
    }
    return filtered.sort(comparators[order])
  }, [allEntries, currentFilter, order])

  const countFor = (filter) => filter === 'favoritos'
    ? allEntries.filter((entry) => entry.favorite).length
    : allEntries.filter((entry) => entry.inWatchlist && entry.status === filter).length

  function toggleFilter(filter) {
    const next = new URLSearchParams(searchParams)
    if (currentFilter === filter) next.delete('filtro')
    else next.set('filtro', filter)
    setSearchParams(next)
  }

  function changeStatus(id, status) {
    updateWatchlist(id, { status })
  }

  function changeProgress(id, progress) {
    updateWatchlist(id, { progresso: progress })
  }

  function clearFilter() {
    const next = new URLSearchParams(searchParams)
    next.delete('filtro')
    setSearchParams(next)
  }

  const filterLabel = tiles.find(({ id }) => id === currentFilter)?.label

  if (loading) return <p className="lista-loading" aria-live="polite">Carregando sua lista…</p>
  if (error) return <div className="lista-empty" role="alert"><p>Ih, o catálogo não carregou. Tenta de novo daqui a pouco?</p><button type="button" onClick={retry}>Tentar de novo</button></div>
  if (!allEntries.length) {
    return (
      <>
        <div className="lista-empty-state">
          <p>Ainda não tem nada por aqui. Que tal começar sua coleção?</p>
          <Link to="/catalogo">Ver catálogo</Link>
        </div>
        <EmAltaNoCatalogo
          items={items}
          favorites={favorites}
          watchlist={watchlist}
          onToggleFavorite={toggleFavorite}
          onToggleWatchlist={toggleWatchlist}
          subtitle="Favorite ou use “Para ver/ler” para montar sua lista."
        />
      </>
    )
  }

  return (
    <>
      <div className="azulejos lista-tiles" role="group" aria-label="Resumo e filtro da lista">
        {tiles.map(({ id, label, tone }) => {
          const total = countFor(id)
          return (
            <button className="azulejo" data-tone={tone} type="button" key={id} aria-pressed={currentFilter === id} onClick={() => toggleFilter(id)}>
              <span className="numero">{total}</span>
              <b>{label}</b>
              <span className="home-mono">{total === 1 ? 'obra' : 'obras'}</span>
            </button>
          )
        })}
      </div>

      <section className="home-section lista-collection" aria-labelledby="lista-collection-title">
        <div className="home-section-heading">
          <h2 id="lista-collection-title">Sua coleção</h2>
          <div className="lista-toolbar">
            <p aria-live="polite">Mostrando <b>{visibleEntries.length}</b> de {allEntries.length} {allEntries.length === 1 ? 'obra' : 'obras'}
              {filterLabel ? <button type="button" className="lista-active-filter" onClick={clearFilter} aria-label={`Remover filtro ${filterLabel}`}>{filterLabel} ×</button> : null}
            </p>
            <label className="lista-order">
              <span>Ordenar por</span>
              <select value={order} onChange={(event) => setOrder(event.target.value)}>
                {orders.map(([id, label]) => <option key={id} value={id}>{label}</option>)}
              </select>
            </label>
          </div>
        </div>

        {visibleEntries.length ? (
          <div className="lista-items">
            {visibleEntries.map((entry, index) => (
              <ListaItem
                key={entry.item.id}
                entry={entry}
                index={index}
                onToggleFavorite={toggleFavorite}
                onToggleWatchlist={toggleWatchlist}
                onStatusChange={changeStatus}
                onProgressChange={changeProgress}
              />
            ))}
          </div>
        ) : (
          <div className="lista-empty">
            <p>Nenhuma obra com esse filtro.</p>
            <div><button type="button" onClick={clearFilter}>Ver tudo</button></div>
          </div>
        )}
      </section>
      <EmAltaNoCatalogo
        items={items}
        favorites={favorites}
        watchlist={watchlist}
        onToggleFavorite={toggleFavorite}
        onToggleWatchlist={toggleWatchlist}
      />
    </>
  )
}

function ListaVisitante() {
  const { items, loading, error } = useMediaCatalog()
  const { toggleFavorite, toggleWatchlist } = useAuth()

  return (
    <>
      <section className="lista-visitor" aria-label="Criar sua lista ANiVA">
        <div className="lista-visitor-actions">
          <Link to="/entrar?modo=cadastro&volta=%2Fminha-lista">Criar conta grátis</Link>
          <Link to="/entrar?volta=%2Fminha-lista">Já tenho conta</Link>
        </div>
      </section>
      <EmAltaNoCatalogo
        items={items}
        loading={loading}
        error={error}
        favorites={[]}
        watchlist={[]}
        onToggleFavorite={toggleFavorite}
        onToggleWatchlist={toggleWatchlist}
        subtitle="Entre para guardar qualquer uma delas."
      />
    </>
  )
}

function MinhaLista() {
  const { user, ready } = useAuth()

  return (
    <div className="lista-page">
      <div className="lista-main">
        <header className={`lista-heading${user ? '' : ' lista-heading-visitor'}`}>
          <p className="lista-kicker">Sua conta</p>
          <h1>Minha lista</h1>
          <p className="lista-lede">Seus favoritos e o que você quer ver ou ler, no mesmo lugar.</p>
        </header>
        {!ready ? <p className="lista-loading" aria-live="polite">Carregando sua lista…</p> : user ? <ListaLogada /> : <ListaVisitante />}
      </div>
    </div>
  )
}

export default MinhaLista
