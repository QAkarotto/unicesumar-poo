interface Props {
  fotos: string[]
  onRemover?: (indice: number) => void
  vazio?: string
}

export function Fotos({ fotos, onRemover, vazio = 'Sem fotos anexadas.' }: Props) {
  if (!fotos.length) return <span className="hint">{vazio}</span>
  return (
    <div className="thumbs">
      {fotos.map((foto, i) => (
        <div className="thumb" key={`${i}-${foto.slice(-24)}`}>
          <img src={foto} alt={`Foto ${i + 1} da lavoura`} />
          {onRemover && (
            <button type="button" onClick={() => onRemover(i)} aria-label={`Remover foto ${i + 1}`}>
              ✕
            </button>
          )}
        </div>
      ))}
    </div>
  )
}
