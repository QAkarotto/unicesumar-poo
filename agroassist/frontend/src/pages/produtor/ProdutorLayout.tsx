import { useEffect, useRef } from 'react'
import { Link, Navigate, Outlet, useLocation } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { useSessao } from '../../context/Sessao'
import { usuario } from '../../data/consultas'
import { primeiroNome } from '../../utils/formato'
import { Codigos } from '../../components/Codigos'
import { useState } from 'react'
import { NOTAS, chaveDaTela } from './notas'
import { Icone, type ChaveMenu } from './Icone'

const MENU: Array<{ chave: ChaveMenu; rotulo: string; destino: string }> = [
  { chave: 'lista', rotulo: 'Solicitações', destino: '/produtor' },
  { chave: 'nova', rotulo: 'Nova', destino: '/produtor/nova' },
  { chave: 'propriedades', rotulo: 'Propriedades', destino: '/produtor/propriedades' },
  { chave: 'perfil', rotulo: 'Perfil', destino: '/produtor/perfil' },
]

export function ProdutorLayout() {
  const [mostrarGuia, setMostrarGuia] = useState(false)
  const banco = useBanco()
  const { produtorId, textoGrande, alternarTextoGrande } = useSessao()
  const { pathname } = useLocation()
  const corpo = useRef<HTMLDivElement>(null)
  const chave = chaveDaTela(pathname)

  useEffect(() => {
    corpo.current?.scrollTo({ top: 0 })
  }, [pathname])

  if (
    chave !== 'entrada' &&
    (!produtorId || !banco.usuarios.some((u) => u.id === produtorId && u.perfil === 'produtor'))
  ) {
    return <Navigate to="/produtor/entrar" replace />
  }

  const conectado = produtorId && chave !== 'entrada' ? usuario(banco, produtorId) : null
  const menuAtivo = chave === 'detalhe' ? 'lista' : chave
  const nota = NOTAS[chave]

  return (
    <div className="prod-grid">
      <div className={`phone${textoGrande ? ' big' : ''}`}>
        <div className="phone-screen">
          {conectado && (
            <div className="app-bar">
              <div className="who">
                <b>Olá, {primeiroNome(conectado.nome)}</b>
                <span>Produtor rural · {conectado.municipio}</span>
              </div>
              <button
                type="button"
                className="a11y"
                onClick={alternarTextoGrande}
                aria-pressed={textoGrande}
                aria-label="Aumentar o texto"
              >
                A+
              </button>
            </div>
          )}
          <div className="app-body" ref={corpo}>
            <Outlet />
          </div>
          {conectado && (
            <nav className="tabbar" aria-label="Menu do aplicativo">
              {MENU.map((item) => (
                <Link
                  key={item.chave}
                  to={item.destino}
                  aria-current={menuAtivo === item.chave ? 'page' : undefined}
                >
                  <Icone nome={item.chave} />
                  {item.rotulo}
                </Link>
              ))}
            </nav>
          )}
        </div>
      </div>

      <aside className="notes">
        <button
          className="btn btn-block guide-toggle"
          type="button"
          aria-expanded={mostrarGuia}
          onClick={() => setMostrarGuia(!mostrarGuia)}
        >
          {mostrarGuia ? 'Ocultar orientações da demonstração' : 'Ver orientações da demonstração'}
        </button>
        <div className={mostrarGuia ? 'guide-detail visible' : 'guide-detail'}>
          <div className="note">
            <span className="eyebrow">Nesta tela</span>
            <h2>{nota.titulo}</h2>
            <p>{nota.descricao}</p>
            <Codigos rf={nota.rf} rn={nota.rn} />
          </div>
          <div className="note">
            <span className="eyebrow">Experimente</span>
            <ul>
              {nota.experimente.map((dica) => (
                <li key={dica}>{dica}</li>
              ))}
            </ul>
          </div>
          <div className="note">
            <span className="eyebrow">Por que celular</span>
            <p>
              O produtor costuma abrir a demanda no campo, na hora em que vê o problema. Por isso essa parte
              foi pensada para tela pequena, com botões grandes e poucos campos. Técnico e administrador
              trabalham no computador.
            </p>
          </div>
        </div>
      </aside>
    </div>
  )
}
