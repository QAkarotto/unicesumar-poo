import { useNavigate } from 'react-router-dom'
import { useSessao } from '../context/Sessao'
import { PRODUTOR_PADRAO } from '../data/constantes'

export function FluxoPrincipal() {
  const navigate = useNavigate()
  const { produtorId, entrarComoProdutor } = useSessao()

  function irComoProdutor(destino: string) {
    if (!produtorId) entrarComoProdutor(PRODUTOR_PADRAO)
    navigate(destino)
  }

  const passos = [
    { rotulo: 'Produtor abre a solicitação', acao: () => irComoProdutor('/produtor/nova') },
    { rotulo: 'Administrador atribui um técnico habilitado', acao: () => navigate('/admin') },
    { rotulo: 'Técnico registra o atendimento', acao: () => navigate('/tecnico') },
    { rotulo: 'Produtor acompanha e avalia', acao: () => irComoProdutor('/produtor') },
  ]

  return (
    <div className="guia">
      <span className="guia-l">Fluxo principal</span>
      <ol>
        {passos.map((passo, i) => (
          <li key={passo.rotulo}>
            <button type="button" onClick={passo.acao}>
              <span className="num">{i + 1}</span>
              {passo.rotulo}
            </button>
          </li>
        ))}
      </ol>
    </div>
  )
}
