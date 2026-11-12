export function handleFallbackImage(event) {
  // Se a imagem externa falha, usa a capa local como fallback.
  if (event.currentTarget.src.endsWith('/data/fallback.svg')) return
  event.currentTarget.src = '/data/fallback.svg'
}
