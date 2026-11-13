import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Mensagem from '../Mensagem.jsx'

const VAZIO = { nome: '', descricao: '' }

export default function Categorias() {
  const [categorias, setCategorias] = useState([])
  const [form, setForm] = useState(VAZIO)
  const [editandoId, setEditandoId] = useState(null)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  const carregar = () => api.get('/categorias').then(setCategorias).catch(setErro)
  useEffect(() => { carregar() }, [])

  const feedback = (msg) => { setErro(null); setSucesso(msg) }
  const falha = (e) => { setSucesso(null); setErro(e) }

  async function salvar(e) {
    e.preventDefault()
    try {
      if (editandoId) {
        await api.put(`/categorias/${editandoId}`, form)
        feedback('Categoria atualizada.')
      } else {
        await api.post('/categorias', form)
        feedback('Categoria cadastrada.')
      }
      cancelar()
      carregar()
    } catch (e) { falha(e) }
  }

  async function excluir(c) {
    if (!confirm(`Excluir a categoria "${c.nome}"?`)) return
    try {
      await api.delete(`/categorias/${c.id}`)
      feedback('Categoria excluída.')
      carregar()
    } catch (e) { falha(e) }
  }

  function editar(c) {
    setEditandoId(c.id)
    setForm({ nome: c.nome, descricao: c.descricao ?? '' })
  }

  function cancelar() {
    setEditandoId(null)
    setForm(VAZIO)
  }

  return (
    <section>
      <h2>Categorias</h2>
      <Mensagem erro={erro} sucesso={sucesso} aoFechar={() => { setErro(null); setSucesso(null) }} />

      <form className="formulario" onSubmit={salvar}>
        <h3>{editandoId ? 'Editar categoria' : 'Nova categoria'}</h3>
        <label>Nome*
          <input value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} />
        </label>
        <label>Descrição
          <input value={form.descricao} onChange={(e) => setForm({ ...form, descricao: e.target.value })} />
        </label>
        <div className="acoes">
          <button type="submit">{editandoId ? 'Salvar alterações' : 'Cadastrar'}</button>
          {editandoId && <button type="button" className="secundario" onClick={cancelar}>Cancelar</button>}
        </div>
      </form>

      <table>
        <thead><tr><th>Nome</th><th>Descrição</th><th></th></tr></thead>
        <tbody>
          {categorias.map((c) => (
            <tr key={c.id}>
              <td>{c.nome}</td>
              <td>{c.descricao}</td>
              <td className="acoes-linha">
                <button className="secundario" onClick={() => editar(c)}>Editar</button>
                <button className="perigo" onClick={() => excluir(c)}>Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}
