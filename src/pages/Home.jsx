import { useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { CARIMBO, getTypes } from '../utils/helpers'
import { useAuth } from '../context/useAuth'
import { useMediaCatalog } from '../hooks/useMediaCatalog'
import { handleFallbackImage } from '../utils/image'
import MediaCard from '../components/media/MediaCard'
import './Home.css'

const formats = [
  { id: 'anime', glyph: 'アニメ', label: 'Anime', language: 'ja' },
  { id: 'mangá', glyph: 'マンガ', label: 'Mangá', language: 'ja' },
  { id: 'manhwa', glyph: '만화', label: 'Manhwa', language: 'ko' },
  { id: 'manhua', glyph: '漫画', label: 'Manhua', language: 'ja' },
]

const formatRating = (value) => Number(value).toLocaleString('pt-BR', { minimumFractionDigits: 1, maximumFractionDigits: 1 })

function Home() {
  const { items, loading, error, retry } = useMediaCatalog()
  const [activeFormat, setActiveFormat] = useState('')
  const { favorites, watchlist, toggleFavorite, toggleWatchlist } = useAuth()

  const featured = useMemo(() => [...items].sort((a, b) => b.popularity - a.popularity)[0], [items])
  const trending = useMemo(
    () => [...items]
      .filter((item) => !activeFormat || getTypes(item).includes(activeFormat))
      .sort((a, b) => b.popularity - a.popularity)
      .slice(0, 4),
    [items, activeFormat],
  )

  return (
    <div className="home-page">
      <div className="home-main">
        <section className="home-opening" aria-labelledby="home-title">
          <div className="home-opening-copy">
            <p className="home-kicker home-mono">Destaque do dia · Matéria de capa</p>
            <h1 id="home-title">O que você viu, leu e ainda quer ver.</h1>
            <p className="home-lede">Organize suas obras, acompanhe suas leituras e encontre o que vem depois.</p>
            {featured ? (
              <p className="home-credits home-mono">
                {[featured.title, featured.episodes && `${featured.episodes} episódios`, `Nota ${formatRating(featured.rating)}`].filter(Boolean).join(' · ')}
              </p>
            ) : null}
            <div className="home-actions">
              <Link className="home-button home-button-primary" to={featured ? `/obra/${featured.id}` : '/catalogo'}>Ver os detalhes <span aria-hidden="true">→</span></Link>
              <Link className="home-button" to="/catalogo">Ver o catálogo</Link>
            </div>
          </div>

          {featured ? (
            <figure className="home-plate">
              <div className="home-plate-sheet" />
              <div className="home-plate-disc" />
              <Link className="home-plate-cover" to={`/obra/${featured.id}`} aria-label={`Ver os detalhes de ${featured.title}`}>
                <span className="home-tape home-tape-top" />
                <span className="home-tape home-tape-bottom" />
                <span className="home-stamp" lang={getTypes(featured)[0] === 'manhwa' ? 'ko' : 'ja'}>{CARIMBO[getTypes(featured)[0]] || '漫画'}</span>
                <img src={featured.cover} alt={`Capa de ${featured.title}`} onError={handleFallbackImage} />
              </Link>
              <div className="home-color-rule" aria-hidden="true">{Array.from({ length: 12 }, (_, index) => <i key={index} />)}</div>
            </figure>
          ) : <div className="home-plate home-plate-empty" aria-hidden="true" />}
        </section>

        <section className="home-section" aria-labelledby="home-formats-title">
          <div className="home-section-heading">
            <h2 id="home-formats-title">Sumário</h2>
            <p className="home-mono">Toque num formato para filtrar o que está em alta</p>
          </div>
          <div className="azulejos">
            {formats.map(({ id, glyph, label, language }) => {
              const count = items.filter((item) => getTypes(item).includes(id)).length
              return (
                <button className="azulejo" type="button" key={id} data-tone={id} aria-pressed={activeFormat === id} onClick={() => setActiveFormat((current) => current === id ? '' : id)}>
                  <span className="glifo" lang={language}>{glyph}</span>
                  <b>{label}</b>
                  <span className="home-mono">{count} obras</span>
                </button>
              )
            })}
          </div>
        </section>

        <section className="home-section home-trending" aria-labelledby="home-trending-title">
          <div className="home-section-heading">
            <h2 id="home-trending-title">Em alta</h2>
            <Link className="home-mono" to="/catalogo">Ver tudo →</Link>
          </div>
          {error ? <div role="alert"><p>Ih, o catálogo não carregou. Tenta de novo daqui a pouco?</p><button className="home-button" type="button" onClick={retry}>Tentar de novo</button></div> : loading ? <p className="home-empty" aria-live="polite">Carregando catálogo…</p> : trending.length ? (
            <div className="home-media-list" aria-live="polite">
              {trending.map((item) => (
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
          ) : <p className="home-empty">Nenhuma obra neste formato por enquanto.</p>}
        </section>
      </div>
    </div>
  )
}

export default Home
