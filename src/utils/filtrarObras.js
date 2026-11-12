import { getTypes } from './helpers'

const values = (value) => (Array.isArray(value) ? value : [value]).filter(Boolean)

const getPlatforms = (item) => [
  ...(item.platforms?.streaming || []),
  ...(item.platforms?.reading || []),
].map((platform) => platform.name)

export function filtrarObras(obras, criterios = {}) {
  const query = String(criterios.q || '').trim().toLocaleLowerCase('pt-BR')
  const minimumRating = Number(criterios.nota) || 0
  // cada filtro se combina com os outros, mas aceita qualquer valor do próprio grupo
  const results = obras.filter((obra) => {
    const searchable = [obra.title, obra.author, obra.studio, ...getTypes(obra), ...(obra.genres || []), ...(obra.tropes || [])]
      .filter(Boolean)
      .join(' ')
      .toLocaleLowerCase('pt-BR')

    return (!query || searchable.includes(query))
      && (!values(criterios.formato).length || values(criterios.formato).some((value) => getTypes(obra).includes(value)))
      && (!values(criterios.genero).length || values(criterios.genero).some((value) => (obra.genres || []).includes(value)))
      && (!values(criterios.trope).length || values(criterios.trope).some((value) => (obra.tropes || []).includes(value)))
      && (!values(criterios.status).length || values(criterios.status).includes(obra.status))
      && (!values(criterios.estudio).length || values(criterios.estudio).includes(obra.studio))
      && (!values(criterios.onde).length || values(criterios.onde).some((value) => getPlatforms(obra).includes(value)))
      && Number(obra.rating) >= minimumRating
  })

  const comparators = {
    nota: (a, b) => b.rating - a.rating,
    popularidade: (a, b) => b.popularity - a.popularity,
    recentes: (a, b) => b.year - a.year,
    alfabetica: (a, b) => a.title.localeCompare(b.title, 'pt-BR'),
  }

  if (criterios.ordem === 'todos') return results
  return results.sort(comparators[criterios.ordem] || comparators.nota)
}
