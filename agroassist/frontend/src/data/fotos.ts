type Marca = [cx: number, cy: number, rx: number, ry: number, cor: string]

function folha(formato: 'longa' | 'redonda', cor: string, marcas: Marca[]): string {
  const desenho =
    formato === 'longa'
      ? `<path d='M8 98 C 50 72, 110 32, 156 14 C 122 46, 72 86, 12 106 Z' fill='${cor}'/><path d='M10 102 L 154 15' stroke='#1d3b18' stroke-opacity='.45' stroke-width='1.5'/>`
      : `<path d='M80 110 C 28 102, 16 52, 46 26 C 64 12, 98 12, 116 26 C 146 52, 132 102, 80 110 Z' fill='${cor}'/><path d='M80 110 L 80 20' stroke='#1d3b18' stroke-opacity='.45' stroke-width='1.5'/>`
  const pontos = marcas
    .map(([cx, cy, rx, ry, c]) => `<ellipse cx='${cx}' cy='${cy}' rx='${rx}' ry='${ry}' fill='${c}'/>`)
    .join('')
  const svg = `<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 160 120'><rect width='160' height='120' fill='#8a7a5c'/><rect y='74' width='160' height='46' fill='#6f6147'/>${desenho}${pontos}</svg>`
  return `data:image/svg+xml;charset=utf-8,${encodeURIComponent(svg)}`
}

export const FOTO_MILHO = folha('longa', '#6e9a3c', [
  [58, 76, 6, 3, '#8a7a5c'],
  [90, 55, 5, 3, '#8a7a5c'],
  [120, 38, 6, 3, '#8a7a5c'],
  [74, 66, 9, 3, '#d9e3a8'],
  [104, 47, 8, 2.5, '#d9e3a8'],
])

export const FOTO_MILHO_2 = folha('longa', '#5f8e35', [
  [40, 88, 7, 3, '#d9e3a8'],
  [70, 70, 5, 3, '#8a7a5c'],
  [132, 30, 7, 3, '#d9e3a8'],
])

export const FOTO_FEIJAO = folha('redonda', '#4f8a3a', [
  [64, 50, 1.8, 1.8, '#f4f4ee'],
  [70, 56, 1.6, 1.6, '#f4f4ee'],
  [92, 48, 1.8, 1.8, '#f4f4ee'],
  [86, 70, 1.6, 1.6, '#f4f4ee'],
  [60, 78, 1.8, 1.8, '#f4f4ee'],
  [100, 82, 1.6, 1.6, '#f4f4ee'],
  [96, 60, 1.7, 1.7, '#f4f4ee'],
])

export const FOTO_TRIGO = folha('longa', '#a3a05a', [
  [60, 76, 6, 3, '#c27b8a'],
  [96, 52, 7, 3, '#e8e3c8'],
  [124, 34, 5, 2.5, '#c27b8a'],
])

export const FOTOS_EXEMPLO = [FOTO_MILHO_2, FOTO_FEIJAO, FOTO_TRIGO, FOTO_MILHO]
