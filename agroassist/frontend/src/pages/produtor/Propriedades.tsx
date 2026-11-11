import { useState, type FormEvent } from 'react'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useProdutorId } from '../../context/Sessao'
import { useToast } from '../../components/Toast'
import { cultura, usuario } from '../../data/consultas'
import { MUNICIPIOS } from '../../data/constantes'
import { formatarDecimal } from '../../utils/formato'
import { mensagemDeErro } from '../../utils/erros'

export function Propriedades() {
  const banco = useBanco()
  const produtorId = useProdutorId()
  const toast = useToast()
  const minhas = banco.propriedades.filter((p) => p.produtorId === produtorId)

  const [nome, setNome] = useState('')
  const [municipio, setMunicipio] = useState(usuario(banco, produtorId).municipio)
  const [area, setArea] = useState('')
  const [culturaIds, setCulturaIds] = useState<string[]>([])
  const [erro, setErro] = useState<string | null>(null)
  const [enviando, setEnviando] = useState(false)

  function alternarCultura(id: string) {
    setCulturaIds((atuais) => (atuais.includes(id) ? atuais.filter((c) => c !== id) : [...atuais, id]))
  }

  async function salvar(e: FormEvent) {
    e.preventDefault()
    setErro(null)
    setEnviando(true)
    try {
      await api.cadastrarPropriedade(produtorId, {
        nome,
        municipio,
        areaHa: Number(area.replace(',', '.')),
        culturaIds,
      })
      toast(`Propriedade ${nome.trim()} cadastrada`)
      setNome('')
      setArea('')
      setCulturaIds([])
    } catch (err) {
      setErro(mensagemDeErro(err))
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="scr">
      <h2>Propriedades</h2>
      {minhas.length ? (
        minhas.map((p) => (
          <div className="box" key={p.id}>
            <div className="scr-head">
              <b>{p.nome}</b>
              <span className="hint">{formatarDecimal(p.areaHa)} ha</span>
            </div>
            <span className="hint">{p.municipio}</span>
            <div className="chips">
              {p.culturaIds.map((c) => (
                <span className="chip" key={c}>
                  {cultura(banco, c).nome}
                </span>
              ))}
            </div>
          </div>
        ))
      ) : (
        <p className="hint">Nenhuma propriedade cadastrada ainda.</p>
      )}

      <form className="box" onSubmit={salvar} noValidate>
        <h3>Cadastrar propriedade</h3>
        <div className="field">
          <label htmlFor="pr-nome">Nome da propriedade</label>
          <input
            id="pr-nome"
            placeholder="ex.: Sítio São José"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />
        </div>
        <div className="field">
          <label htmlFor="pr-mun">Município</label>
          <select id="pr-mun" value={municipio} onChange={(e) => setMunicipio(e.target.value)}>
            <option value="">Selecione</option>
            {MUNICIPIOS.map((m) => (
              <option key={m}>{m}</option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="pr-area">Área (hectares)</label>
          <input id="pr-area" inputMode="decimal" value={area} onChange={(e) => setArea(e.target.value)} />
        </div>
        <fieldset className="field">
          <legend>Culturas plantadas</legend>
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
        <button type="submit" className="btn btn-primary btn-block" disabled={enviando}>
          {enviando ? 'Salvando...' : 'Salvar propriedade'}
        </button>
      </form>
    </div>
  )
}
