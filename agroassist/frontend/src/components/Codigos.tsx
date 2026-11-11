import { Link } from 'react-router-dom'

export function Codigos({ rf = [], rn = [] }: { rf?: string[]; rn?: string[] }) {
  return (
    <div className="chips">
      {rf.map((c) => (
        <Link key={c} to="/modelo" className="code" title="Ver requisito em Modelo e regras">
          {c}
        </Link>
      ))}
      {rn.map((c) => (
        <Link key={c} to="/modelo" className="code rn" title="Ver regra em Modelo e regras">
          {c}
        </Link>
      ))}
    </div>
  )
}
