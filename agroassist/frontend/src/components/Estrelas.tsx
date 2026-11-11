export function NotaEstrelas({ nota }: { nota: number }) {
  return (
    <span className="rating" aria-label={`${nota} de 5`}>
      {[1, 2, 3, 4, 5].map((n) => (
        <span key={n} className={n <= nota ? undefined : 'off'} aria-hidden="true">
          ★
        </span>
      ))}
    </span>
  )
}

interface SeletorProps {
  valor: number
  onChange: (nota: number) => void
}

export function SeletorNota({ valor, onChange }: SeletorProps) {
  return (
    <fieldset className="stars">
      <legend className="sr">Nota de 1 a 5</legend>
      {[1, 2, 3, 4, 5].map((n) => (
        <label key={n} className={`star${n <= valor ? ' on' : ''}`}>
          <input type="radio" name="nota" value={n} checked={valor === n} onChange={() => onChange(n)} />
          <span className="s" aria-hidden="true">
            ★
          </span>
          <span className="sr">{n} de 5</span>
        </label>
      ))}
    </fieldset>
  )
}
