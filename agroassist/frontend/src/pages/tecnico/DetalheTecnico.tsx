import { useState, type FormEvent } from 'react'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useToast } from '../../components/Toast'
import { StatusPill } from '../../components/StatusPill'
import { Fotos } from '../../components/Fotos'
import { Historico } from '../../components/Historico'
import { NotaEstrelas } from '../../components/Estrelas'
import { AtendimentoResumo } from '../../components/AtendimentoResumo'
import {
  atendimentoDe,
  avaliacaoDe,
  cultura,
  nomeDoTecnico,
  propriedade,
  usuario,
} from '../../data/consultas'
import { ehTecnicoResponsavel, podeVerContatoDoProdutor } from '../../domain/regras'
import { MODALIDADES, type Modalidade, type Solicitacao } from '../../domain/tipos'
import { formatarDecimal, hojeISO, mascararNome } from '../../utils/formato'
import { mensagemDeErro } from '../../utils/erros'

interface Props {
  solicitacao: Solicitacao
  usuarioId: string
}

export function DetalheTecnico({ solicitacao: s, usuarioId }: Props) {
  const banco = useBanco()
  const toast = useToast()
  const [iniciando, setIniciando] = useState(false)
  const responsavel = ehTecnicoResponsavel(banco, usuarioId, s)
  const verContato = podeVerContatoDoProdutor(banco, usuarioId, s)
  const produtor = usuario(banco, s.produtorId)
  const prop = propriedade(banco, s.propriedadeId)
  const atendimento = atendimentoDe(banco, s.id)
  const avaliacao = avaliacaoDe(banco, s.id)

  async function iniciar() {
    setIniciando(true)
    try {
      await api.iniciarAtendimento(usuarioId, s.id)
      toast(`${s.protocolo} em atendimento`)
    } catch (err) {
      toast(mensagemDeErro(err))
    } finally {
      setIniciando(false)
    }
  }

  return (
    <div className="panel">
      <div className="panel-h">
        <h2 className="mono">{s.protocolo}</h2>
        <StatusPill status={s.status} />
      </div>
      <div className="panel-b">
        <dl className="kv">
          <dt>Produtor</dt>
          <dd>
            {verContato
              ? `${produtor.nome} · ${produtor.telefone}`
              : `${mascararNome(produtor.nome)} · contato oculto`}
          </dd>
          <dt>Propriedade</dt>
          <dd>
            {verContato ? `${prop.nome}, ` : ''}
            {prop.municipio} · {formatarDecimal(prop.areaHa)} ha
          </dd>
          <dt>Cultura</dt>
          <dd>
            {cultura(banco, s.culturaId).nome}
            {s.estadio ? ` · ${s.estadio}` : ''}
          </dd>
          <dt>Responsável</dt>
          <dd>{s.tecnicoId ? nomeDoTecnico(banco, s.tecnicoId) : 'ainda não atribuído'}</dd>
        </dl>
        {!verContato && (
          <p className="privacy">
            RN06: nome completo, propriedade e telefone do produtor só aparecem para o técnico responsável.
          </p>
        )}
        <p className="texto">{s.descricao}</p>
        <Fotos fotos={s.fotos} />

        {!responsavel && (
          <>
            <div className="rule-note">
              RN03: só o técnico responsável pode iniciar, registrar ou mudar o status desta solicitação.
            </div>
            <button type="button" className="btn" disabled>
              Iniciar atendimento
            </button>
          </>
        )}
        {responsavel && s.status === 'Atribuída' && (
          <button type="button" className="btn btn-primary" onClick={iniciar} disabled={iniciando}>
            {iniciando ? 'Iniciando...' : 'Iniciar atendimento'}
          </button>
        )}
        {responsavel && s.status === 'Em atendimento' && (
          <FormAtendimento solicitacao={s} usuarioId={usuarioId} />
        )}

        {atendimento && (
          <div className="box">
            <h3>Atendimento registrado</h3>
            <AtendimentoResumo atendimento={atendimento} />
          </div>
        )}
        {avaliacao ? (
          <div className="box">
            <h3>Avaliação do produtor</h3>
            <NotaEstrelas nota={avaliacao.nota} />
            {avaliacao.comentario && <p className="texto">{avaliacao.comentario}</p>}
          </div>
        ) : (
          s.status === 'Concluída' && <p className="muted texto">Aguardando a avaliação do produtor.</p>
        )}
        <div className="box">
          <h3>Histórico</h3>
          <Historico solicitacao={s} />
        </div>
      </div>
    </div>
  )
}

function FormAtendimento({ solicitacao, usuarioId }: Props) {
  const toast = useToast()
  const [data, setData] = useState(hojeISO())
  const [modalidade, setModalidade] = useState<Modalidade>('Visita à propriedade')
  const [diagnostico, setDiagnostico] = useState('')
  const [recomendacao, setRecomendacao] = useState('')
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)

  async function concluir(e: FormEvent) {
    e.preventDefault()
    setErro(null)
    setEnviando(true)
    try {
      await api.registrarAtendimento(usuarioId, solicitacao.id, {
        data,
        modalidade,
        diagnostico,
        recomendacao,
      })
      toast(`${solicitacao.protocolo} concluída. O produtor já pode avaliar.`)
    } catch (err) {
      setErro(mensagemDeErro(err))
      setEnviando(false)
    }
  }

  return (
    <form className="box" onSubmit={concluir} noValidate>
      <h3>Registrar atendimento</h3>
      <div className="form-grid">
        <div className="field">
          <label htmlFor="at-data">Data</label>
          <input id="at-data" type="date" value={data} onChange={(e) => setData(e.target.value)} />
        </div>
        <div className="field">
          <label htmlFor="at-mod">Modalidade</label>
          <select
            id="at-mod"
            value={modalidade}
            onChange={(e) => setModalidade(e.target.value as Modalidade)}
          >
            {MODALIDADES.map((m) => (
              <option key={m}>{m}</option>
            ))}
          </select>
        </div>
      </div>
      <div className="field">
        <label htmlFor="at-diag">Diagnóstico</label>
        <textarea
          id="at-diag"
          rows={3}
          placeholder="O que foi observado na lavoura."
          value={diagnostico}
          onChange={(e) => setDiagnostico(e.target.value)}
        />
      </div>
      <div className="field">
        <label htmlFor="at-rec">Recomendação</label>
        <textarea
          id="at-rec"
          rows={3}
          placeholder="O que o produtor deve fazer e quando."
          value={recomendacao}
          onChange={(e) => setRecomendacao(e.target.value)}
        />
      </div>
      {erro && (
        <p className="form-err" role="alert">
          {erro}
        </p>
      )}
      <button type="submit" className="btn btn-primary" disabled={enviando}>
        {enviando ? 'Salvando...' : 'Concluir atendimento'}
      </button>
    </form>
  )
}
