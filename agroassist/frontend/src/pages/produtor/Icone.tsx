import type { ChaveTela } from './notas'

type ChaveMenu = Exclude<ChaveTela, 'entrada' | 'detalhe'>

const PROPS = {
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  strokeWidth: 2,
  strokeLinecap: 'round' as const,
  strokeLinejoin: 'round' as const,
  'aria-hidden': true,
}

export function Icone({ nome }: { nome: ChaveMenu }) {
  if (nome === 'lista')
    return (
      <svg {...PROPS}>
        <path d="M8 6h13M8 12h13M8 18h13M3.5 6h.01M3.5 12h.01M3.5 18h.01" />
      </svg>
    )
  if (nome === 'nova')
    return (
      <svg {...PROPS}>
        <circle cx="12" cy="12" r="9" />
        <path d="M12 8v8M8 12h8" />
      </svg>
    )
  if (nome === 'propriedades')
    return (
      <svg {...PROPS}>
        <path d="M3 20h18M5 20V10l7-5 7 5v10M10 20v-5h4v5" />
      </svg>
    )
  return (
    <svg {...PROPS}>
      <circle cx="12" cy="8" r="4" />
      <path d="M4 21c0-4 4-6 8-6s8 2 8 6" />
    </svg>
  )
}

export type { ChaveMenu }
