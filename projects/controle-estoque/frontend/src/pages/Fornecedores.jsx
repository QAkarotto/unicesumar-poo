import { useEffect, useState } from 'react'
import { api, cnpjFormatado } from '../api.js'
import Mensagem from '../Mensagem.jsx'

const VAZIO = { nome: '', cnpj: '', email: '', telefone: '' }

export default function Fornecedores() {
  const [fornecedores, setFornecedores] = useState([])
  const [form, setForm] = useState(VAZIO)
  const [editandoId, setEditandoId] = useState(null)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  const carregar = () => api.get('/fornecedores').then(setFornecedores).catch(setErro)
  useEffect(() => { carregar() }, [])

  const feedback = (msg) => { setErro(null); setSucesso(msg) }
  const falha = (e) => { setSucesso(null); setErro(e) }
  const campo = (nome) => ({
    value: form[nome],
    onChange: (e) => setForm({ ...form, [nome]: e.target.value }),
  })

  async function salvar(e) {
    e.preventDefault()
    // campos opcionais vazios são enviados como null
    const corpo = { ...form, email: form.email || null, telefone: form.telefone || null }
    try {
      if (editandoId) {
        await api.put(`/fornecedores/${editandoId}`, corpo)
        feedback('Fornecedor atualizado.')
      } else {
        await api.post('/fornecedores', corpo)
        feedback('Fornecedor cadastrado.')
      }
      cancelar()
      carregar()
    } catch (e) { falha(e) }
  }

  async function excluir(f) {
    if (!confirm(`Excluir o fornecedor "${f.nome}"?`)) return
    try {
      await api.delete(`/fornecedores/${f.id}`)
      feedback('Fornecedor excluído.')
      carregar()
    } catch (e) { falha(e) }
  }

  function editar(f) {
    setEditandoId(f.id)
    setForm({ nome: f.nome, cnpj: f.cnpj, email: f.email ?? '', telefone: f.telefone ?? '' })
  }

  function cancelar() {
    setEditandoId(null)
    setForm(VAZIO)
  }

  return (
    <section>
      <h2>Fornecedores</h2>
      <Mensagem erro={erro} sucesso={sucesso} aoFechar={() => { setErro(null); setSucesso(null) }} />

      <form className="formulario" onSubmit={salvar}>
        <h3>{editandoId ? 'Editar fornecedor' : 'Novo fornecedor'}</h3>
        <label>Nome*<input {...campo('nome')} /></label>
        <label>CNPJ*<input placeholder="00.000.000/0000-00" {...campo('cnpj')} /></label>
        <label>E-mail<input type="email" {...campo('email')} /></label>
        <label>Telefone<input {...campo('telefone')} /></label>
        <div className="acoes">
          <button type="submit">{editandoId ? 'Salvar alterações' : 'Cadastrar'}</button>
          {editandoId && <button type="button" className="secundario" onClick={cancelar}>Cancelar</button>}
        </div>
      </form>

      <table>
        <thead><tr><th>Nome</th><th>CNPJ</th><th>E-mail</th><th>Telefone</th><th></th></tr></thead>
        <tbody>
          {fornecedores.map((f) => (
            <tr key={f.id}>
              <td>{f.nome}</td>
              <td>{cnpjFormatado(f.cnpj)}</td>
              <td>{f.email}</td>
              <td>{f.telefone}</td>
              <td className="acoes-linha">
                <button className="secundario" onClick={() => editar(f)}>Editar</button>
                <button className="perigo" onClick={() => excluir(f)}>Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}
