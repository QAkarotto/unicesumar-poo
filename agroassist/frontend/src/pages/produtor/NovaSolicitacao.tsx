import { useState, type ChangeEvent, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useProdutorId } from '../../context/Sessao'
import { useToast } from '../../components/Toast'
import { Fotos } from '../../components/Fotos'
import { cultura } from '../../data/consultas'
import { LIMITE_FOTOS } from '../../data/constantes'
import { FOTOS_EXEMPLO } from '../../data/fotos'
import { lerImagemReduzida } from '../../utils/imagem'
import { mensagemDeErro } from '../../utils/erros'

export function NovaSolicitacao() {
  const banco = useBanco()
  const produtorId = useProdutorId()
  const navigate = useNavigate()
  const toast = useToast()
  const propriedades = banco.propriedades.filter((p) => p.produtorId === produtorId)

  const [etapa, setEtapa] = useState(1)
  const [tipoProblema, setTipoProblema] = useState('')
  const [propriedadeId, setPropriedadeId] = useState(propriedades[0]?.id ?? '')
  const [culturaId, setCulturaId] = useState(propriedades[0]?.culturaIds[0] ?? '')
  const [estadio, setEstadio] = useState('')
  const [descricao, setDescricao] = useState('')
  const [fotos, setFotos] = useState<string[]>([])
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)
  const [carregandoFotos, setCarregandoFotos] = useState(false)

  if (!propriedades.length) {
    return (
      <div className="scr">
        <h2>Nova solicitação</h2>
        <div className="empty">
          <p>Para abrir uma solicitação, cadastre antes a propriedade e o que está plantado nela.</p>
          <Link to="/produtor/propriedades" className="btn btn-primary">
            Cadastrar propriedade
          </Link>
        </div>
      </div>
    )
  }

  const selecionada = propriedades.find((p) => p.id === propriedadeId) ?? propriedades[0]

  function trocarPropriedade(id: string) {
    setPropriedadeId(id)
    setCulturaId(propriedades.find((p) => p.id === id)?.culturaIds[0] ?? '')
  }

  async function anexar(e: ChangeEvent<HTMLInputElement>) {
    const arquivos = Array.from(e.target.files ?? [])
    e.target.value = ''
    const vagas = Math.max(0, LIMITE_FOTOS - fotos.length)
    if (arquivos.length > vagas) toast(`Limite de ${LIMITE_FOTOS} fotos por solicitação`)
    if (!arquivos.length || !vagas) return
    setCarregandoFotos(true)
    try {
      const novas = await Promise.all(arquivos.slice(0, vagas).map(lerImagemReduzida))
      setFotos((atuais) => [...atuais, ...novas].slice(0, LIMITE_FOTOS))
    } catch (err) {
      toast(mensagemDeErro(err))
    } finally {
      setCarregandoFotos(false)
    }
  }

  function usarFotoExemplo() {
    if (fotos.length >= LIMITE_FOTOS) {
      toast(`Limite de ${LIMITE_FOTOS} fotos por solicitação`)
      return
    }
    setFotos((atuais) => [...atuais, FOTOS_EXEMPLO[atuais.length % FOTOS_EXEMPLO.length]])
  }

  function avancar() {
    if (etapa === 1 && (!selecionada.id || !culturaId)) {
      setErro('Selecione a propriedade e a cultura.')
      return
    }
    if (etapa === 2 && !descricao.trim()) {
      setErro('Descreva o que está acontecendo na lavoura.')
      return
    }
    setErro(null)
    setEtapa((p) => Math.min(4, p + 1))
  }
  async function enviar(e: FormEvent) {
    e.preventDefault()
    if (etapa !== 4 || carregandoFotos || enviando) return
    setErro(null)
    setEnviando(true)
    try {
      const criada = await api.abrirSolicitacao(produtorId, {
        propriedadeId: selecionada.id,
        culturaId,
        estadio,
        descricao,
        fotos,
      })
      toast(`Solicitação ${criada.protocolo} enviada`)
      navigate(`/produtor/solicitacoes/${criada.id}`)
    } catch (err) {
      setErro(mensagemDeErro(err))
      setEnviando(false)
    }
  }

  return (
    <form className="scr" onSubmit={enviar} noValidate>
      <h2>Nova solicitação</h2>
      <div className="step-heading">
        <span>Etapa {etapa} de 4</span>
        <span>
          {etapa === 1
            ? 'Sua lavoura'
            : etapa === 2
              ? 'O que aconteceu?'
              : etapa === 3
                ? 'Adicionar fotos'
                : 'Revisar e enviar'}
        </span>
      </div>
      <div className="step-progress" aria-label={`Etapa ${etapa} de 4`}>
        <span style={{ width: `${(etapa * 100) / 4}%` }} />
      </div>
      {etapa === 1 && (
        <>
          <div className="field">
            <label htmlFor="f-prop">Propriedade</label>
            <select id="f-prop" value={selecionada.id} onChange={(e) => trocarPropriedade(e.target.value)}>
              {propriedades.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.nome} ({p.municipio})
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="f-cult">Cultura</label>
            <select id="f-cult" value={culturaId} onChange={(e) => setCulturaId(e.target.value)}>
              {selecionada.culturaIds.map((id) => (
                <option key={id} value={id}>
                  {cultura(banco, id).nome}
                </option>
              ))}
            </select>
            <span className="hint">Aparecem só as culturas cadastradas nesta propriedade.</span>
          </div>
        </>
      )}
      {etapa === 2 && (
        <>
          <div className="field">
            <label htmlFor="f-est">
              Estádio da lavoura <span className="opt">opcional</span>
            </label>
            <input
              id="f-est"
              placeholder="ex.: V4, R1, perfilhamento"
              value={estadio}
              onChange={(e) => setEstadio(e.target.value)}
            />
          </div>
          <div className="field">
            <label htmlFor="f-tipo">
              Tipo de ocorrência <span className="opt">opcional</span>
            </label>
            <select id="f-tipo" value={tipoProblema} onChange={(e) => setTipoProblema(e.target.value)}>
              <option value="">Não sei identificar</option>
              <option>Pragas</option>
              <option>Doenças</option>
              <option>Plantas daninhas</option>
              <option>Solo e nutrição</option>
              <option>Outros</option>
            </select>
          </div>
          <div className="field">
            <label htmlFor="f-desc">O que está acontecendo?</label>
            <textarea
              id="f-desc"
              rows={4}
              required
              placeholder="Onde aparece, desde quando, o que mudou na lavoura."
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
          </div>
        </>
      )}
      {etapa === 3 && (
        <>
          <p className="hint">
            Adicione uma foto da planta ou da lavoura para ajudar a identificar o problema. Você também pode
            continuar sem foto.
          </p>
          <div className="field">
            <label htmlFor="f-fotos">
              Fotos <span className="opt">até {LIMITE_FOTOS}</span>
            </label>
            <input
              id="f-fotos"
              type="file"
              accept="image/*"
              multiple
              onChange={anexar}
              disabled={carregandoFotos || fotos.length >= LIMITE_FOTOS}
            />
            {carregandoFotos && (
              <span className="hint" role="status">
                Preparando fotos...
              </span>
            )}
            <button type="button" className="btn btn-sm" onClick={usarFotoExemplo}>
              Foto de exemplo
            </button>
            <Fotos
              fotos={fotos}
              onRemover={(i) => setFotos((atuais) => atuais.filter((_, j) => j !== i))}
              vazio=""
            />
          </div>
        </>
      )}
      {etapa === 4 && (
        <div className="review-panel">
          <strong>Confira sua solicitação</strong>
          <span>
            {selecionada.nome} · {cultura(banco, culturaId).nome}
          </span>
          <span>
            {tipoProblema ? `${tipoProblema} · ` : ''}
            {descricao || 'Sem descrição'}
          </span>
          <span>{fotos.length} foto(s) anexada(s)</span>
          <Fotos fotos={fotos} />
        </div>
      )}
      {erro && (
        <p className="form-err" role="alert">
          {erro}
        </p>
      )}
      <div className="step-actions">
        {etapa > 1 && (
          <button
            type="button"
            className="btn"
            onClick={() => {
              setErro(null)
              setEtapa((p) => p - 1)
            }}
          >
            Voltar
          </button>
        )}
        {etapa < 4 ? (
          <button
            key="continuar"
            className="btn btn-primary"
            type="button"
            onClick={(e) => {
              e.preventDefault()
              avancar()
            }}
            disabled={carregandoFotos}
          >
            Continuar →
          </button>
        ) : (
          <button
            key="enviar"
            type="submit"
            className="btn btn-primary"
            disabled={enviando || carregandoFotos}
          >
            {enviando ? 'Enviando...' : 'Enviar solicitação'}
          </button>
        )}
      </div>
    </form>
  )
}
