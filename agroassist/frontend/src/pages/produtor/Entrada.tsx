import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useSessao } from '../../context/Sessao'
import { useToast } from '../../components/Toast'
import { MUNICIPIOS } from '../../data/constantes'
import { mensagemDeErro } from '../../utils/erros'

export function Entrada() {
  const banco = useBanco()
  const { entrarComoProdutor } = useSessao()
  const navigate = useNavigate()
  const toast = useToast()
  const [nome, setNome] = useState('')
  const [telefone, setTelefone] = useState('')
  const [municipio, setMunicipio] = useState('')
  const [email, setEmail] = useState('')
  const [consentimento, setConsentimento] = useState(false)
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)

  const produtores = banco.usuarios.filter((u) => u.perfil === 'produtor')

  function entrar(id: string) {
    entrarComoProdutor(id)
    navigate('/produtor')
  }

  async function criarConta(e: FormEvent) {
    e.preventDefault()
    setErro(null)
    setEnviando(true)
    try {
      const id = await api.criarConta({ nome, telefone, municipio, email, consentimento })
      entrarComoProdutor(id)
      navigate('/produtor/propriedades')
      toast('Conta criada. Agora cadastre sua propriedade.')
    } catch (err) {
      setErro(mensagemDeErro(err))
      setEnviando(false)
    }
  }

  return (
    <>
      <div className="entry-hero">
        <b>AgroAssist</b>
        <span>Peça assistência técnica e acompanhe pelo celular.</span>
      </div>
      <div className="scr">
        <div className="stack">
          <span className="hint">Entrar com uma conta de demonstração</span>
          {produtores.map((p) => (
            <button key={p.id} type="button" className="btn btn-block" onClick={() => entrar(p.id)}>
              {p.nome} · {p.municipio}
            </button>
          ))}
        </div>
        <div className="divider">ou</div>
        <form className="stack" onSubmit={criarConta} noValidate>
          <h2>Criar conta de produtor</h2>
          <div className="field">
            <label htmlFor="c-nome">Nome completo</label>
            <input id="c-nome" autoComplete="name" value={nome} onChange={(e) => setNome(e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="c-tel">Telefone (WhatsApp)</label>
            <input
              id="c-tel"
              inputMode="tel"
              placeholder="(42) 99999-0000"
              autoComplete="tel"
              value={telefone}
              onChange={(e) => setTelefone(e.target.value)}
            />
          </div>
          <div className="field">
            <label htmlFor="c-mun">Município</label>
            <select id="c-mun" value={municipio} onChange={(e) => setMunicipio(e.target.value)}>
              <option value="">Selecione</option>
              {MUNICIPIOS.map((m) => (
                <option key={m}>{m}</option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="c-email">
              E-mail <span className="opt">opcional</span>
            </label>
            <input
              id="c-email"
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <label className="consent" htmlFor="c-lgpd">
            <input
              type="checkbox"
              id="c-lgpd"
              checked={consentimento}
              onChange={(e) => setConsentimento(e.target.checked)}
            />
            <span>
              Concordo que meu nome, telefone e município sejam usados somente para o atendimento técnico,
              conforme a LGPD. Posso pedir a exclusão a qualquer momento.
            </span>
          </label>
          {erro && (
            <p className="form-err" role="alert">
              {erro}
            </p>
          )}
          <button type="submit" className="btn btn-primary btn-block" disabled={enviando}>
            {enviando ? 'Criando conta...' : 'Criar conta'}
          </button>
        </form>
      </div>
    </>
  )
}
