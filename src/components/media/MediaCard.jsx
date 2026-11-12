import { Link } from 'react-router-dom'
import { getTypes, isSaved } from '../../utils/helpers'
import { handleFallbackImage } from '../../utils/image'
import './media.css'

function MediaCard({ item, favorites = [], watchlist = [], onToggleFavorite, onToggleWatchlist }) {
  const favorite = isSaved(favorites, item.id)
  const inList = isSaved(watchlist, item.id)
  const types = getTypes(item)

  return (
    <article className="media-card">
      <button className={`media-favorite ${favorite ? 'is-saved' : ''}`} type="button" aria-pressed={favorite} aria-label={`${favorite ? 'Remover dos favoritos' : 'Favoritar'} ${item.title}`} onClick={() => onToggleFavorite(item.id)}>
        <span aria-hidden="true">{favorite ? '♥' : '♡'}</span>
      </button>
      <Link to={`/obra/${item.id}`} className="media-cover" aria-label={`Ver os detalhes de ${item.title}, nota ${item.rating}`}>
        <img src={item.cover} alt={`Capa de ${item.title}`} onError={handleFallbackImage} loading="lazy" decoding="async" />
        <span className="media-rating" aria-label={`Nota ${item.rating}`}>{item.rating}</span>
      </Link>
      <p className="media-meta">{types.join(' · ')} · {item.year}{item.studio ? ` · ${item.studio}` : ''}</p>
      <h3><Link to={`/obra/${item.id}`}>{item.title}</Link></h3>
      <ul className="media-tropes">{(item.tropes || []).slice(0, 2).map((trope) => <li key={trope}>{trope}</li>)}</ul>
      <div className="media-foot">
        <p className="media-warning"><strong>Aviso</strong><span>{item.warnings?.length ? item.warnings.join(', ') : 'Sem avisos cadastrados'}</span></p>
        <div className="media-actions">
          <button type="button" className={inList ? 'is-saved' : ''} aria-pressed={inList} onClick={() => onToggleWatchlist(item.id)}>
            {inList ? 'Na lista' : 'Para ver/ler'}
          </button>
        </div>
      </div>
    </article>
  )
}

export default MediaCard
