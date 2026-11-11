import { useEffect, useRef, useState } from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { carregarBanco, reiniciarBanco } from '../services/store'
import { useSessao } from '../context/Sessao'
import { useToast } from './Toast'
import { mensagemDeErro } from '../utils/erros'

const ABAS = [
  { destino: '/produtor', rotulo: 'Produtor', detalhe: '' },
  { destino: '/tecnico', rotulo: 'Técnico', detalhe: '' },
  { destino: '/admin', rotulo: 'Coordenação', detalhe: '' },
  { destino: '/modelo', rotulo: 'Sobre o projeto', detalhe: '' },
]

export function Cabecalho() {
  const [confirmando, setConfirmando] = useState(false)
  const [reiniciando, setReiniciando] = useState(false)
  const timer = useRef<number>()
  const navigate = useNavigate()
  const { reiniciarSessao } = useSessao()
  const toast = useToast()

  useEffect(() => () => window.clearTimeout(timer.current), [])

  async function reiniciar() {
    if (reiniciando) return
    if (!confirmando) {
      setConfirmando(true)
      timer.current = window.setTimeout(() => setConfirmando(false), 4000)
      return
    }
    window.clearTimeout(timer.current)
    setConfirmando(false)
    setReiniciando(true)
    try {
      await reiniciarBanco()
      reiniciarSessao()
      await carregarBanco(true)
      navigate('/produtor')
      toast('Demonstração reiniciada com os dados de exemplo')
    } catch (err) {
      toast(mensagemDeErro(err))
    } finally {
      setReiniciando(false)
    }
  }

  return (
    <header className="top">
      <div className="wrap">
        <div className="brandrow">
          <div>
            <p className="logo">
              <Link to="/" className="brand-link">
                ✳ AgroAssist
              </Link>
            </p>
            <p className="tagline">Assistência técnica rural • Protótipo acadêmico</p>
          </div>
          <button
            type="button"
            className={`btn-onbrand${confirmando ? ' armed' : ''}`}
            onClick={reiniciar}
            disabled={reiniciando}
            title="Substitui os dados cadastrados pelos exemplos iniciais."
          >
            {reiniciando
              ? 'Restaurando...'
              : confirmando
                ? 'Apagar alterações e restaurar?'
                : 'Restaurar dados de exemplo'}
          </button>
        </div>
        <nav className="tabs" aria-label="Ver o sistema como">
          {ABAS.map((aba) => (
            <NavLink
              key={aba.destino}
              to={aba.destino}
              className={({ isActive }) => `tab${isActive ? ' active' : ''}`}
            >
              {aba.rotulo}
              {aba.detalhe && <small>{aba.detalhe}</small>}
            </NavLink>
          ))}
        </nav>
      </div>
    </header>
  )
}
