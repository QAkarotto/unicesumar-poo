import { useNavigate } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { useProdutorId, useSessao } from '../../context/Sessao'
import { usuario } from '../../data/consultas'
import { formatarDataHora } from '../../utils/formato'

export function Perfil() {
  const banco = useBanco()
  const produtorId = useProdutorId()
  const { sair } = useSessao()
  const navigate = useNavigate()
  const u = usuario(banco, produtorId)

  function trocarUsuario() {
    navigate('/produtor/entrar')
    sair()
  }

  return (
    <div className="scr">
      <h2>Perfil</h2>
      <div className="box">
        <h3>Seus dados</h3>
        <dl className="kv">
          <dt>Nome</dt>
          <dd>{u.nome}</dd>
          <dt>Telefone</dt>
          <dd>{u.telefone}</dd>
          <dt>Município</dt>
          <dd>{u.municipio}</dd>
          <dt>E-mail</dt>
          <dd>{u.email || 'não informado'}</dd>
        </dl>
      </div>
      <div className="box">
        <h3>Privacidade</h3>
        <p className="texto">Consentimento registrado em {formatarDataHora(u.consentimentoEm)}.</p>
        <p className="texto hint">
          Seus dados são usados só para o atendimento técnico. O telefone aparece apenas para o técnico
          responsável pela sua solicitação. O AgroAssist não pede CPF nem documentos.
        </p>
      </div>
      <button type="button" className="btn btn-block" onClick={trocarUsuario}>
        Sair e trocar de usuário
      </button>
    </div>
  )
}
