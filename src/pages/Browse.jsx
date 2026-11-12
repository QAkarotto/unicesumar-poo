import { useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import MediaCard from '../components/media/MediaCard'
import { useAuth } from '../context/useAuth'
import { getTypes } from '../utils/helpers'
import { handleFallbackImage } from '../utils/image'
import { filtrarObras } from '../utils/filtrarObras'
import { useMediaCatalog } from '../hooks/useMediaCatalog'
import './Browse.css'

const filterLabels = {
  q: 'Busca',
  formato: 'Formato',
  genero: 'Gênero',
  trope: 'Trope',
  status: 'Situação',
}

const sorts = [
  ['nota', 'Melhor nota'],
  ['popularidade', 'Popularidade'],
  ['recentes', 'Mais recentes'],
  ['alfabetica', 'Ordem alfabética'],
]

const unique = (values) => [...new Set(values.filter(Boolean))]
  .sort((a, b) => String(a).localeCompare(String(b), 'pt-BR'))

function SelectField({ label, value, options, allLabel = 'Todos', onChange }) {
  return (
    <label className="catalog-field">
      <span>{label}</span>
      <select value={value} onChange={(event) => onChange(event.target.value)}>
        <option value="">{allLabel}</option>
        {options.map((option) => {
          const [optionValue, optionLabel] = Array.isArray(option) ? option : [option, option]
          return <option key={optionValue} value={optionValue}>{optionLabel}</option>
        })}
      </select>
    </label>
  )
}

function MultiSelectField({ label, options, selected, onToggle }) {
  return (
    <div className="catalog-field catalog-multi-field">
      <span>{label}</span>
      <details className="catalog-multi">
        <summary aria-label={`${label}: ${selected.length ? selected.join(', ') : 'Todos'}`}>
          {label}{selected.length ? ` (${selected.length})` : ''}
        </summary>
        <div className="catalog-multi-options">
          {options.map((option) => {
            const [value, optionLabel] = Array.isArray(option) ? option : [option, option]
            return (
              <label className="catalog-multi-option" key={value}>
                <input type="checkbox" checked={selected.includes(value)} onChange={() => onToggle(value)} />
                <span>{optionLabel}</span>
              </label>
            )
          })}
        </div>
      </details>
    </div>
  )
}

function CatalogRow({ item, favorites, watchlist, onToggleFavorite, onToggleWatchlist }) {
  return (
    <article className="media-row catalog-row">
      <Link className="media-cover catalog-row-cover" to={`/obra/${item.id}`} aria-label={`Ver os detalhes de ${item.title}`}>
        <img src={item.cover} alt={`Capa de ${item.title}`} onError={handleFallbackImage} loading="lazy" decoding="async" />
      </Link>
      <div className="catalog-row-copy">
        <p className="catalog-row-meta">{[...getTypes(item), item.year, item.studio].filter(Boolean).join(' · ')}</p>
        <h2><Link to={`/obra/${item.id}`}>{item.title}</Link></h2>
        <p className="catalog-row-rating">Nota {Number(item.rating).toLocaleString('pt-BR', { minimumFractionDigits: 1 })}</p>
      </div>
      <div className="catalog-row-actions">
        <button type="button" aria-pressed={favorites.includes(item.id)} onClick={() => onToggleFavorite(item.id)}>
          {favorites.includes(item.id) ? '♥ Favorito' : '♡ Favoritar'}
        </button>
        <button type="button" aria-pressed={watchlist.includes(item.id)} onClick={() => onToggleWatchlist(item.id)}>
          {watchlist.includes(item.id) ? '✓ Na lista' : '+ Para ver/ler'}
        </button>
      </div>
    </article>
  )
}

function Browse() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [view, setView] = useState('lista')
  const { favorites, watchlist, toggleFavorite, toggleWatchlist } = useAuth()
  const { items: catalogItems, loading, error, retry } = useMediaCatalog()

  const formatOptions = useMemo(() => unique(catalogItems.flatMap(getTypes)), [catalogItems])
  const genreOptions = useMemo(() => unique(catalogItems.flatMap((item) => item.genres || [])), [catalogItems])
  const tropeOptions = useMemo(() => unique(catalogItems.flatMap((item) => item.tropes || [])), [catalogItems])
  const statusOptions = useMemo(() => unique(catalogItems.map((item) => item.status)), [catalogItems])

  const query = searchParams.get('q') || ''
  const format = searchParams.getAll('formato')
  const genre = searchParams.getAll('genero')
  const trope = searchParams.getAll('trope')
  const status = searchParams.getAll('status')
  const sort = searchParams.get('ordem') || ''

  const filteredItems = useMemo(() => filtrarObras(catalogItems, {
    q: query, formato: format, genero: genre, trope, status, ordem: sort || 'todos',
  }), [catalogItems, format, genre, query, sort, status, trope])

  function changeParam(key, value, replace = false) {
    setSearchParams((current) => {
      const next = new URLSearchParams(current)
      if (value) next.set(key, value)
      else next.delete(key)
      return next
    }, { replace })
  }

  function toggleParam(key, value) {
    setSearchParams((current) => {
      const next = new URLSearchParams(current)
      const selected = [...new Set(next.getAll(key))]
      const updated = selected.includes(value)
        ? selected.filter((entry) => entry !== value)
        : [...selected, value]
      next.delete(key)
      updated.forEach((entry) => next.append(key, entry))
      return next
    })
  }

  function removeParam(key, value) {
    setSearchParams((current) => {
      const next = new URLSearchParams(current)
      const remaining = next.getAll(key).filter((entry) => entry !== value)
      next.delete(key)
      remaining.forEach((entry) => next.append(key, entry))
      return next
    })
  }

  function clearFilters() {
    setSearchParams(new URLSearchParams())
  }

  const activeFilters = Object.entries(filterLabels).flatMap(([key, label]) =>
    searchParams.getAll(key).map((value, index) => ({ key, label, value, id: `${key}-${index}-${value}` })),
  )

  return (
    <div className="catalog-page">
      <div className="catalog-main">
        <header className="catalog-heading">
          <p className="catalog-kicker">Arquivo editorial</p>
          <h1>Catálogo</h1>
          <p className="catalog-lede">Combine formato, gênero e trope no catálogo.</p>
        </header>

        <div className="catalog-layout">
          <aside className="catalog-sidebar">
            <section className="catalog-filter-panel" aria-label="Filtros do catálogo">
              <div className="catalog-filter-row">
                <label className="catalog-field catalog-search-field">
                  <span>Buscar</span>
                  <input type="search" value={query} onChange={(event) => changeParam('q', event.target.value, true)} placeholder="Título, autoria ou palavra" />
                </label>
                <SelectField label="Ordenar por" value={sort} options={sorts} onChange={(value) => changeParam('ordem', value)} />
                <MultiSelectField label="Formato" selected={format} options={formatOptions} onToggle={(value) => toggleParam('formato', value)} />
                <MultiSelectField label="Trope" selected={trope} options={tropeOptions} onToggle={(value) => toggleParam('trope', value)} />
                <MultiSelectField label="Situação" selected={status} options={statusOptions} onToggle={(value) => toggleParam('status', value)} />
                <MultiSelectField label="Gênero" selected={genre} options={genreOptions} onToggle={(value) => toggleParam('genero', value)} />
              </div>
            </section>
          </aside>

          <section className="catalog-content" aria-label="Obras do catálogo">
            <div className="catalog-results-heading">
              <p aria-live="polite">Mostrando <b>{filteredItems.length}</b> de {catalogItems.length} obras</p>
              <div className="catalog-view-toggle" role="group" aria-label="Modo de exibição">
                <button type="button" aria-pressed={view === 'lista'} onClick={() => setView('lista')}>Lista</button>
                <button type="button" aria-pressed={view === 'grade'} onClick={() => setView('grade')}>Grade</button>
              </div>
            </div>

            <div className="catalog-active-filters" aria-label="Filtros ativos">
              {activeFilters.map(({ key, label, value, id }) => (
                <button type="button" key={id} onClick={() => removeParam(key, value)} aria-label={`Remover filtro ${label}: ${value}`}>
                  {label}: {value} ×
                </button>
              ))}
              {activeFilters.length ? <button type="button" className="catalog-clear-filters" onClick={clearFilters}>Limpar tudo</button> : null}
            </div>

            {loading ? <p className="catalog-loading" aria-live="polite">Carregando catálogo…</p> : error ? (
              <div className="catalog-empty" role="alert"><p>Ih, o catálogo não carregou. Tenta de novo daqui a pouco?</p><button type="button" onClick={retry}>Tentar de novo</button></div>
            ) : !filteredItems.length ? (
              <div className="catalog-empty">
                <p>Essa combinação não deu match. Que tal tentar outros filtros?</p>
                {activeFilters.length ? <button type="button" onClick={clearFilters}>Limpar filtros</button> : null}
              </div>
            ) : view === 'lista' ? (
              <div className="catalog-results-list" aria-live="polite">
                {filteredItems.map((item) => (
                  <CatalogRow
                    key={item.id}
                    item={item}
                    favorites={favorites}
                    watchlist={watchlist}
                    onToggleFavorite={toggleFavorite}
                    onToggleWatchlist={toggleWatchlist}
                  />
                ))}
              </div>
            ) : (
              <div className="catalog-results-grid" aria-live="polite">
                {filteredItems.map((item) => (
                  <MediaCard
                    key={item.id}
                    item={item}
                    favorites={favorites}
                    watchlist={watchlist}
                    onToggleFavorite={toggleFavorite}
                    onToggleWatchlist={toggleWatchlist}
                  />
                ))}
              </div>
            )}
          </section>
        </div>
      </div>
    </div>
  )
}

export default Browse
