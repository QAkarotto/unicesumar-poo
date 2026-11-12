import { useEffect, useMemo, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import { feedbackApi } from '../services/api'
import { CARIMBO, getTypes } from '../utils/helpers'
import { handleFallbackImage } from '../utils/image'
import { useMediaCatalog } from '../hooks/useMediaCatalog'
import './Details.css'

const formatDate = (value) => new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' }).format(new Date(value))
const formatScore = (value) => Number(value).toLocaleString('pt-BR', { minimumFractionDigits: 1, maximumFractionDigits: 1 })

function Details() {
  const { id } = useParams()
  const { items, loading, error: catalogError, retry } = useMediaCatalog()
  const [ratings, setRatings] = useState({})
  const [comments, setComments] = useState([])
  const [selectedRating, setSelectedRating] = useState(0)
  const [ratingOpen, setRatingOpen] = useState(false)
  const [sort, setSort] = useState('melhores')
  const [showRules, setShowRules] = useState(false)
  const [revealed, setRevealed] = useState([])
  const [commentText, setCommentText] = useState('')
  const [spoiler, setSpoiler] = useState(false)
  const [error, setError] = useState('')
  const [copied, setCopied] = useState(false)
  const { user, favorites, watchlist, toggleFavorite, toggleWatchlist, goToLogin } = useAuth()

  const item = items?.find((entry) => entry.id === id)
  const itemId = item?.id
  const userId = user?.id

  useEffect(() => {
    if (!itemId) return
    let live = true
    Promise.all([feedbackApi.ratings(itemId), feedbackApi.comments(itemId)])
      .then(([savedRatings, savedComments]) => {
        if (!live) return
        setRatings(savedRatings)
        setComments(savedComments)
        setSelectedRating(userId ? savedRatings[userId] || 0 : 0)
      })
      .catch((cause) => { if (live) setError(cause.message) })
    return () => { live = false }
  }, [itemId, userId])

  const types = useMemo(() => item ? getTypes(item) : [], [item])
  // gênero e trope valem mais pontos que compartilhar formato
  const recommendations = useMemo(() => item
    ? items.filter((entry) => entry.id !== item.id)
      .map((entry) => ({
        entry,
        score: entry.genres.filter((genre) => item.genres.includes(genre)).length * 2
          + getTypes(entry).filter((type) => types.includes(type)).length
          + (entry.tropes || []).filter((trope) => item.tropes?.includes(trope)).length * 2,
      }))
      .filter(({ score }) => score > 0)
      .sort((a, b) => b.score - a.score || b.entry.popularity - a.entry.popularity)
      .slice(0, 4).map(({ entry }) => entry)
    : [], [item, items, types])

  // quando não escolhe recente ou antigo, os mais curtidos ficam primeiro
  const sortedComments = useMemo(() => [...comments].sort((a, b) => {
    if (sort === 'recentes') return new Date(b.createdAt) - new Date(a.createdAt)
    if (sort === 'antigos') return new Date(a.createdAt) - new Date(b.createdAt)
    return b.likes.length - a.likes.length || new Date(b.createdAt) - new Date(a.createdAt)
  }), [comments, sort])

  // sessão expirada leva ao login; os outros erros da API aparecem na mensagem de erro da página
  function handleApiError(cause) {
    if (cause.status === 401) goToLogin()
    else setError(cause.message)
  }

  async function saveRating(value) {
    if (!user) return goToLogin()
    try {
      const next = await feedbackApi.rate(item.id, user.id, value)
      setRatings(next)
      setSelectedRating(value)
      setRatingOpen(false)
      setError('')
    } catch (cause) { handleApiError(cause) }
  }

  async function removeRating() {
    try {
      const next = await feedbackApi.removeRating(item.id, user.id)
      setRatings(next)
      setSelectedRating(0)
      setRatingOpen(false)
      setError('')
    } catch (cause) { handleApiError(cause) }
  }

  async function submitComment(event) {
    event.preventDefault()
    if (!user) return goToLogin()
    const text = commentText.trim()
    if (!text) { setError('Escreva algo antes de publicar.'); return }
    try {
      const next = await feedbackApi.addComment(item.id, user, text, spoiler)
      setComments(next)
      setCommentText('')
      setSpoiler(false)
      setError('')
    } catch (cause) { handleApiError(cause) }
  }

  async function toggleLike(commentId) {
    if (!user) return goToLogin()
    try {
      setComments(await feedbackApi.toggleCommentLike(item.id, user.id, commentId))
    } catch (cause) { handleApiError(cause) }
  }

  async function deleteComment(commentId) {
    if (!window.confirm('Vai mesmo apagar? Essa ação não pode ser desfeita.')) return
    try {
      setComments(await feedbackApi.deleteComment(item.id, user.id, commentId))
    } catch (cause) { handleApiError(cause) }
  }

  async function copyLink() {
    try {
      await navigator.clipboard.writeText(window.location.href)
      setCopied(true)
      window.setTimeout(() => setCopied(false), 1800)
    } catch {
      setError('Não foi possível copiar o link neste navegador.')
    }
  }

  if (loading) return <main className="details-page details-loading"><h1>Carregando obra…</h1></main>
  if (catalogError) return <main className="details-page details-loading" role="alert"><h1>Ih, o catálogo não carregou. Tenta de novo daqui a pouco?</h1><button className="details-button" type="button" onClick={retry}>Tentar de novo</button></main>
  if (!item) return <main className="details-page details-empty"><h1>Obra não encontrada</h1><p>Essa obra não apareceu por aqui. Que tal voltar ao catálogo?</p><Link to="/catalogo">Catálogo</Link></main>

  const formatStats = item.formats?.length ? item.formats : []
  const voteValues = Object.values(ratings)
  const average = voteValues.length ? voteValues.reduce((sum, value) => sum + value, 0) / voteValues.length : null
  const distribution = Array.from({ length: 10 }, (_, index) => voteValues.filter((value) => value === index + 1).length)
  const maxVotes = Math.max(1, ...distribution)

  return (
    <main className="details-page">
      <div className="details-wrap">
        <p className="details-breadcrumb"><Link to="/">Início</Link><span>/</span><Link to="/catalogo">Catálogo</Link><span>/</span>{item.title}</p>

        <section className="details-hero">
          <figure className="details-cover-wrap">
            <span className="details-stamp" lang={types[0] === 'manhwa' ? 'ko' : 'ja'} aria-hidden="true">{CARIMBO[types[0]] || '漫画'}</span>
            <img className="details-cover" src={item.cover} alt={`Capa de ${item.title}`} onError={handleFallbackImage} />
            <figcaption>{types.join(' · ')} <span>·</span> {item.year}</figcaption>
          </figure>

          <div className="details-summary">
            <ul className="details-tags details-types" aria-label="Formatos">
              {types.map((type) => <li key={type} data-format={type}><span lang={type === 'manhwa' ? 'ko' : 'ja'}>{CARIMBO[type] || ''}</span>{type}</li>)}
              <li>{item.year}</li>
            </ul>
            <h1>{item.title}</h1>
            <div className="details-rating-line">
              <span className="details-rating-badge"><span aria-hidden="true">★</span> Nota {formatScore(item.rating)}</span>
              <span className="details-status">{item.status}</span>
            </div>
            <p className="details-synopsis">{item.synopsis}</p>
            <ul className="details-tags">{item.genres.map((genre) => <li key={genre}>{genre}</li>)}</ul>
            <ul className="details-tags details-tropes">{(item.tropes || []).map((trope) => <li key={trope}>{trope}</li>)}</ul>
            <div className="details-actions">
              <button type="button" className={`details-button details-primary ${favorites.includes(item.id) ? 'is-selected' : ''}`} aria-pressed={favorites.includes(item.id)} onClick={() => toggleFavorite(item.id)}>{favorites.includes(item.id) ? '♥ Favorito' : '♡ Favoritar'}</button>
              <button type="button" className={`details-button ${watchlist.includes(item.id) ? 'is-selected' : ''}`} aria-pressed={watchlist.includes(item.id)} onClick={() => toggleWatchlist(item.id)}>{watchlist.includes(item.id) ? 'Na minha lista' : 'Adicionar à lista'}</button>
              <button type="button" className="details-button details-copy" onClick={copyLink}>{copied ? 'Link copiado' : 'Copiar link'}</button>
            </div>
          </div>
        </section>

        <section className="details-panels" aria-label="Informações da obra">
          <article className="details-panel"><h2>Produção</h2><dl><div><dt>Estúdio</dt><dd>{item.studio || 'Não informado'}</dd></div><div><dt>Autoria</dt><dd>{item.author || 'Não informado'}</dd></div>{item.duration ? <div><dt>Duração</dt><dd>{item.duration}</dd></div> : null}</dl>
            {formatStats.length ? <><h3>Formatos</h3>{formatStats.map((format) => <div className="details-format" key={`${format.type}-${format.length}`}><b>{format.type}</b><span>{format.length}</span>{format.note ? <small>{format.note}</small> : null}</div>)}</> : null}
          </article>
          <article className="details-panel"><h2>Onde assistir ou ler</h2>
            {item.platforms?.streaming?.length ? <PlatformGroup label="Streaming" platforms={item.platforms.streaming} /> : null}
            {item.platforms?.reading?.length ? <PlatformGroup label="Leitura" platforms={item.platforms.reading} /> : null}
            {item.platforms?.note ? <p className="details-platform-note">{item.platforms.note}</p> : null}
            {!item.platforms?.streaming?.length && !item.platforms?.reading?.length ? <p>Sem plataformas cadastradas.</p> : null}
          </article>
          <article className="details-panel"><h2>Avisos</h2>{item.warnings?.length ? <ul className="details-tags">{item.warnings.map((warning) => <li key={warning}>{warning}</li>)}</ul> : <p>Sem avisos cadastrados.</p>}</article>
        </section>

        {item.characters?.length ? <section className="details-section"><SectionHeading title="Personagens principais" />
          <div className="details-characters">{item.characters.map((character) => <article key={character.name}><h3>{character.name}</h3><p className="details-character-role">{character.role}</p><p>{character.description}</p></article>)}</div>
        </section> : null}

        <section className="details-section" aria-labelledby="details-reviews-heading">
          <SectionHeading title="Qual é o seu veredito?" id="details-reviews-heading" />
          <div className="details-reviews">
            <div className="details-average"><b>{average === null ? '—' : formatScore(average)}</b><span>/10</span><p>{voteValues.length ? `${voteValues.length} ${voteValues.length === 1 ? 'avaliação' : 'avaliações'}` : 'Ainda sem avaliações'}</p></div>
            <div className="details-bars" role="img" aria-label={`Distribuição de ${voteValues.length} avaliações`}>
              {distribution.map((count, index) => <div className="details-bar-col" key={index}><i style={{ height: `${count ? Math.max(8, count / maxVotes * 100) : 3}%` }} title={`${count} votos`} /><span>{index + 1}</span></div>)}
            </div>
            <div className="details-rate-control"><button type="button" className="details-button" aria-expanded={ratingOpen} onClick={() => { if (!user) { goToLogin(); return } setRatingOpen((open) => !open) }}>{selectedRating ? `Sua nota: ${selectedRating} · alterar` : '+ Avaliar'}</button>
              {ratingOpen ? <div className="details-rate-picker"><p>Escolha uma nota de 1 a 10.</p><div>{Array.from({ length: 10 }, (_, index) => index + 1).map((value) => <button type="button" key={value} aria-label={`Nota ${value} de 10`} aria-pressed={selectedRating === value} onClick={() => setSelectedRating(value)}>{value}</button>)}</div><button className="details-button details-primary" type="button" disabled={!selectedRating} onClick={() => saveRating(selectedRating)}>Publicar avaliação</button>{selectedRating ? <button className="details-text-button" type="button" onClick={removeRating}>Remover minha nota</button> : null}</div> : null}
            </div>
          </div>
        </section>

        <section className="details-section" aria-labelledby="details-comments-heading">
          <SectionHeading title="Comentários" id="details-comments-heading" />
          <div className="details-rules"><p><b>Aviso:</b> leia as <button className="details-text-button" type="button" aria-expanded={showRules} onClick={() => setShowRules((open) => !open)}>regras de comentários</button> antes de publicar.</p>
            {showRules ? <ul><li>Respeite as pessoas; não publique ofensas.</li><li>Marque spoiler quando o comentário contar partes da história.</li><li>Não publique propaganda ou links suspeitos.</li></ul> : null}
          </div>
          <div className="details-comments-toolbar"><h3>{comments.length} {comments.length === 1 ? 'comentário' : 'comentários'}</h3><label>Ordenar por <select value={sort} onChange={(event) => setSort(event.target.value)}><option value="melhores">Mais úteis</option><option value="recentes">Mais recentes</option><option value="antigos">Mais antigos</option></select></label></div>
          {user ? <form className="details-comment-form" onSubmit={submitComment}><span className="details-avatar" aria-hidden="true">{user.nome.trim().charAt(0).toUpperCase()}</span><div className="details-comment-fields"><label className="sr-only" htmlFor="details-comment-input">Seu comentário</label><textarea id="details-comment-input" value={commentText} onChange={(event) => setCommentText(event.target.value)} maxLength={1000} placeholder="O que você achou?" required /><p className="details-error" role="alert">{error}</p><div className="details-comment-submit"><label><input type="checkbox" checked={spoiler} onChange={(event) => setSpoiler(event.target.checked)} /> Marcar como spoiler</label><button className="details-button details-primary" type="submit">Publicar</button></div></div></form>
            : <p className="details-login-prompt">Para comentar, <button className="details-text-button" type="button" onClick={goToLogin}>entre ou crie uma conta</button>.</p>}
          <div className="details-comment-list">{sortedComments.length ? sortedComments.map((comment) => {
            const hidden = comment.spoiler && !revealed.includes(comment.id)
            return <article className="details-comment" key={comment.id}><span className="details-avatar" aria-hidden="true">{comment.author.trim().charAt(0).toUpperCase()}</span><div><p className="details-comment-meta"><b>{comment.author}</b> · {formatDate(comment.createdAt)}{comment.spoiler && hidden ? <> · <strong>SPOILER</strong></> : null}</p>
              {hidden ? <div className="details-spoiler"><button className="details-text-button" type="button" aria-label="Mostrar comentário com spoiler" onClick={() => setRevealed((seen) => [...seen, comment.id])}>Mostrar</button></div> : <p className="details-comment-text">{comment.text}</p>}
              <div className="details-comment-actions"><button className="details-text-button" type="button" aria-pressed={Boolean(user && comment.likes.includes(user.id))} onClick={() => toggleLike(comment.id)}>Útil ({comment.likes.length})</button>{user?.id === comment.authorId ? <button className="details-text-button" type="button" onClick={() => deleteComment(comment.id)}>Apagar</button> : null}</div>
            </div></article>
          }) : <p className="details-no-comments">Ainda não há comentários. Seja a primeira pessoa a comentar.</p>}</div>
        </section>

        {recommendations.length ? <section className="details-section"><SectionHeading title="Talvez você curta também" /><div className="details-recommendations">{recommendations.map((entry) => <Link className="details-recommendation" to={`/obra/${entry.id}`} key={entry.id}><img src={entry.cover} alt={`Capa de ${entry.title}`} onError={handleFallbackImage} /><span><b>{entry.title}</b><small>{entry.genres.slice(0, 3).join(' · ')}</small></span></Link>)}</div></section> : null}
      </div>
    </main>
  )
}

function SectionHeading({ title, id }) {
  return <div className="details-section-heading"><h2 id={id}>{title}</h2><span aria-hidden="true" /></div>
}

function PlatformGroup({ label, platforms }) {
  return <div className="details-platform-group"><h3>{label}</h3><div>{platforms.map((platform) => <a key={platform.name} href={platform.url} target="_blank" rel="noopener noreferrer">{platform.name} ↗</a>)}</div></div>
}

export default Details
