import { useState, type FormEvent } from 'react'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useToast } from '../../components/Toast'
import { FORMACOES, MUNICIPIOS } from '../../data/constantes'
import { mensagemDeErro } from '../../utils/erros'

export function FormTecnico() {
  const banco = useBanco()
  const toast = useToast()
  const [nome, setNome] = useState('')
  const [formacao, setFormacao] = useState(FORMACOES[0])
  const [registro, setRegistro] = useState('')
  const [telefone, setTelefone] = useState('')
  const [municipio, setMunicipio] = useState(MUNICIPIOS[0])
  const [culturaIds, setCulturaIds] = useState<string[]>([])
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)

  function alternarCultura(id: string) {
    setCulturaIds((atuais) => (atuais.includes(id) ? atuais.filter((c) => c !== id) : [...atuais, id]))
  }

  async function cadastrar(e: FormEvent) {
    e.preventDefault()
    setErro(null)
    setEnviando(true)
    try {
      await api.cadastrarTecnico({ nome, formacao, registro, telefone, municipio, culturaIds })
      toast(`${nome.trim()} cadastrado como técnico`)
      setNome('')
      setRegistro('')
      setTelefone('')
      setCulturaIds([])
    } catch (err) {
      setErro(mensagemDeErro(err))
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="panel" onSubmit={cadastrar} noValidate>
      <div className="panel-h">
        <h3>Cadastrar técnico</h3>
      </div>
      <div className="panel-b">
        <div className="field">
          <label htmlFor="tc-nome">Nome</label>
          <input id="tc-nome" value={nome} onChange={(e) => setNome(e.target.value)} />
        </div>
        <div className="form-grid">
          <div className="field">
            <label htmlFor="tc-form">Formação</label>
            <select id="tc-form" value={formacao} onChange={(e) => setFormacao(e.target.value)}>
              {FORMACOES.map((f) => (
                <option key={f}>{f}</option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="tc-reg">Registro profissional</label>
            <input
              id="tc-reg"
              placeholder="CREA-PR 000000/D"
              value={registro}
              onChange={(e) => setRegistro(e.target.value)}
            />
          </div>
        </div>
        <div className="form-grid">
          <div className="field">
            <label htmlFor="tc-tel">Telefone</label>
            <input
              id="tc-tel"
              inputMode="tel"
              value={telefone}
              onChange={(e) => setTelefone(e.target.value)}
            />
          </div>
          <div className="field">
            <label htmlFor="tc-mun">Município</label>
            <select id="tc-mun" value={municipio} onChange={(e) => setMunicipio(e.target.value)}>
              {MUNICIPIOS.map((m) => (
                <option key={m}>{m}</option>
              ))}
            </select>
          </div>
        </div>
        <fieldset className="field">
          <legend>Culturas em que é habilitado</legend>
          <div className="checks">
            {banco.culturas.map((c) => (
              <label className="check" key={c.id}>
                <input
                  type="checkbox"
                  checked={culturaIds.includes(c.id)}
                  onChange={() => alternarCultura(c.id)}
                />
                {c.nome}
              </label>
            ))}
          </div>
        </fieldset>
        {erro && (
          <p className="form-err" role="alert">
            {erro}
          </p>
        )}
        <button type="submit" className="btn btn-primary" disabled={enviando}>
          {enviando ? 'Cadastrando...' : 'Cadastrar técnico'}
        </button>
      </div>
    </form>
  )
}
