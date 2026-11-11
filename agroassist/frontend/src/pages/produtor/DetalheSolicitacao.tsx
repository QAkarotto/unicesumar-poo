import { useState, type FormEvent } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useProdutorId } from '../../context/Sessao'
import { useToast } from '../../components/Toast'
import { Etapas } from '../../components/Etapas'
import { Historico } from '../../components/Historico'
import { Fotos } from '../../components/Fotos'
import { NotaEstrelas, SeletorNota } from '../../components/Estrelas'
import { AtendimentoResumo } from '../../components/AtendimentoResumo'
import { atendimentoDe, avaliacaoDe, cultura, propriedade, tecnico, usuario } from '../../data/consultas'
import { podeAvaliar } from '../../domain/regras'
import { formatarDataHora } from '../../utils/formato'
import { mensagemDeErro } from '../../utils/erros'

export function DetalheSolicitacao() {
  const { id } = useParams()
  const banco = useBanco()
  const produtorId = useProdutorId()
  const s = banco.solicitacoes.find((x) => x.id === id && x.produtorId === produtorId)

  if (!s) {
    return (
      <div className="scr">
        <Link to="/produtor" className="back">
          ‹ Minhas solicitações
        </Link>
        <p className="hint">Solicitação não encontrada nesta conta.</p>
      </div>
    )
  }

  const prop = propriedade(banco, s.propriedadeId)
  const atendimento = atendimentoDe(banco, s.id)
  const avaliacao = avaliacaoDe(banco, s.id)
  const responsavel = s.tecnicoId ? tecnico(banco, s.tecnicoId) : null
  const nomeCultura = cultura(banco, s.culturaId).nome

  return (
    <div className="scr">
      <Link to="/produtor" className="back">
        ‹ Minhas solicitações
      </Link>
      <div className="scr-head">
        <h2>{nomeCultura}</h2>
        <span className="mono muted">{s.protocolo}</span>
      </div>
      <Etapas status={s.status} />

      <div className="box">
        <h3>Solicitação</h3>
        <dl className="kv">
          <dt>Propriedade</dt>
          <dd>{prop.nome}</dd>
          <dt>Estádio</dt>
          <dd>{s.estadio || 'não informado'}</dd>
          <dt>Aberta em</dt>
          <dd>{formatarDataHora(s.criadaEm)}</dd>
        </dl>
        <p className="texto">{s.descricao}</p>
        <Fotos fotos={s.fotos} />
      </div>

      <div className="box">
        <h3>Técnico responsável</h3>
        {responsavel ? (
          <div>
            <b>{usuario(banco, responsavel.usuarioId).nome}</b>
            <br />
            <span className="hint">
              {responsavel.formacao} · {responsavel.registro}
            </span>
          </div>
        ) : (
          <span className="hint">
            Aguardando a coordenação atribuir um técnico habilitado em {nomeCultura}.
          </span>
        )}
      </div>

      {atendimento && (
        <div className="box">
          <h3>Atendimento registrado</h3>
          <AtendimentoResumo atendimento={atendimento} />
        </div>
      )}

      {podeAvaliar(banco, produtorId, s) ? (
        <FormAvaliacao solicitacaoId={s.id} produtorId={produtorId} />
      ) : avaliacao ? (
        <div className="box">
          <h3>Sua avaliação</h3>
          <NotaEstrelas nota={avaliacao.nota} />
          {avaliacao.comentario && <p className="texto">{avaliacao.comentario}</p>}
        </div>
      ) : (
        <div className="box">
          <h3>Avaliação</h3>
          <span className="hint">Fica disponível quando o técnico concluir o atendimento.</span>
        </div>
      )}

      <div className="box">
        <h3>Histórico</h3>
        <Historico solicitacao={s} />
      </div>
    </div>
  )
}

function FormAvaliacao({ solicitacaoId, produtorId }: { solicitacaoId: string; produtorId: string }) {
  const toast = useToast()
  const [nota, setNota] = useState(0)
  const [comentario, setComentario] = useState('')
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)

  async function enviar(e: FormEvent) {
    e.preventDefault()
    setErro(null)
    setEnviando(true)
    try {
      await api.avaliar(produtorId, solicitacaoId, nota, comentario)
      toast('Obrigado! Avaliação registrada.')
    } catch (err) {
      setErro(mensagemDeErro(err))
      setEnviando(false)
    }
  }

  return (
    <form className="box box-accent" onSubmit={enviar} noValidate>
      <h3>Avalie o atendimento</h3>
      <SeletorNota valor={nota} onChange={setNota} />
      <div className="field">
        <label htmlFor="f-coment">
          Comentário <span className="opt">opcional</span>
        </label>
        <textarea id="f-coment" rows={2} value={comentario} onChange={(e) => setComentario(e.target.value)} />
      </div>
      {erro && (
        <p className="form-err" role="alert">
          {erro}
        </p>
      )}
      <button type="submit" className="btn btn-primary btn-block" disabled={enviando}>
        {enviando ? 'Enviando...' : 'Enviar avaliação'}
      </button>
    </form>
  )
}
