export const CARIMBO = { anime: 'アニメ', 'mangá': 'マンガ', manhwa: '만화', manhua: '漫画', novel: '小説' }

export function getTypes(item) {
  return item.types?.length ? item.types : [item.type].filter(Boolean)
}

export function isSaved(list, id) {
  return list.includes(id)
}
