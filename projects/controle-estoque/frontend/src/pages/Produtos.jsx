import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, moeda, query } from '../api.js'
import Mensagem from '../Mensagem.jsx'

const VAZIO = { sku: '', nome: '', descricao: '', precoUnitario: '', estoqueMinimo: '0', categoriaId: '' }

export default function Produtos() {
  const [produtos, setProdutos] = useState([])
  const [categorias, setCategorias] = useState([])
  const [filtro, setFiltro] = useState({ busca: '', categoriaId: '' })
  const [form, setForm] = useState(VAZIO)
  const [editandoId, setEditandoId] = useState(null)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  const carregar = () => api.get('/produtos' + query(filtro)).then(setProdutos).catch(setErro)

  useEffect(() => { api.get('/categorias').then(setCategorias).catch(setErro) }, [])
  useEffect(() => { carregar() }, [filtro.categoriaId]) // busca por texto é disparada pelo botão

  const feedback = (msg) => { setErro(null); setSucesso(msg) }
  const falha = (e) => { setSucesso(null); setErro(e) }
  const campo = (nome) => ({
    value: form[nome],
    onChange: (e) => setForm({ ...form, [nome]: e.target.value }),
  })

  async function salvar(e) {
    e.preventDefault()
    const corpo = {
      ...form,
      precoUnitario: form.precoUnitario === '' ? null : Number(form.precoUnitario),
      estoqueMinimo: form.estoqueMinimo === '' ? null : Number(form.estoqueMinimo),
      categoriaId: form.categoriaId === '' ? null : Number(form.categoriaId),
    }
    try {
      if (editandoId) {
        await api.put(`/produtos/${editandoId}`, corpo)
        feedback('Produto atualizado.')
      } else {
        await api.post('/produtos', corpo)
        feedback('Produto cadastrado. Vincule um fornecedor na tela de detalhes para registrar entradas.')
      }
      cancelar()
      carregar()
    } catch (e) { falha(e) }
  }

  async function alternarAtivo(p) {
    try {
      await api.patch(`/produtos/${p.id}/${p.ativo ? 'inativacao' : 'ativacao'}`)
      feedback(`Produto ${p.ativo ? 'inativado' : 'reativado'}.`)
      carregar()
    } catch (e) { falha(e) }
  }

  async function excluir(p) {
    if (!confirm(`Excluir o produto "${p.nome}"?`)) return
    try {
      await api.delete(`/produtos/${p.id}`)
      feedback('Produto excluído.')
      carregar()
    } catch (e) { falha(e) }
  }

  function editar(p) {
    setEditandoId(p.id)
    setForm({
      sku: p.sku, nome: p.nome, descricao: p.descricao ?? '',
      precoUnitario: String(p.precoUnitario), estoqueMinimo: String(p.estoqueMinimo),
      categoriaId: String(p.categoriaId),
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function cancelar() {
    setEditandoId(null)
    setForm(VAZIO)
  }

  return (
    <section>
      <h2>Produtos</h2>
      <Mensagem erro={erro} sucesso={sucesso} aoFechar={() => { setErro(null); setSucesso(null) }} />

      <form className="formulario" onSubmit={salvar}>
        <h3>{editandoId ? 'Editar produto' : 'Novo produto'}</h3>
        <label>SKU*<input {...campo('sku')} disabled={!!editandoId} title="O SKU não pode ser alterado" /></label>
        <label>Nome*<input {...campo('nome')} /></label>
        <label>Categoria*
          <select {...campo('categoriaId')}>
            <option value="">Selecione...</option>
            {categorias.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
          </select>
        </label>
        <label>Preço unitário (R$)*<input type="number" step="0.01" {...campo('precoUnitario')} /></label>
        <label>Estoque mínimo*<input type="number" {...campo('estoqueMinimo')} /></label>
        <label className="largo">Descrição<input {...campo('descricao')} /></label>
        <div className="acoes">
          <button type="submit">{editandoId ? 'Salvar alterações' : 'Cadastrar'}</button>
          {editandoId && <button type="button" className="secundario" onClick={cancelar}>Cancelar</button>}
        </div>
      </form>

      <form className="filtros" onSubmit={(e) => { e.preventDefault(); carregar() }}>
        <input placeholder="Buscar por nome ou SKU" value={filtro.busca}
               onChange={(e) => setFiltro({ ...filtro, busca: e.target.value })} />
        <select value={filtro.categoriaId} onChange={(e) => setFiltro({ ...filtro, categoriaId: e.target.value })}>
          <option value="">Todas as categorias</option>
          {categorias.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
        </select>
        <button type="submit" className="secundario">Buscar</button>
      </form>

      <table>
        <thead>
          <tr>
            <th>SKU</th><th>Nome</th><th>Categoria</th><th>Preço</th>
            <th>Saldo</th><th>Mínimo</th><th>Situação</th><th></th>
          </tr>
        </thead>
        <tbody>
          {produtos.length === 0 && <tr><td colSpan="8" className="vazio">Nenhum produto encontrado.</td></tr>}
          {produtos.map((p) => (
            <tr key={p.id} className={p.ativo ? '' : 'inativo'}>
              <td>{p.sku}</td>
              <td>{p.nome}</td>
              <td>{p.categoria}</td>
              <td className="numero">{moeda(p.precoUnitario)}</td>
              <td className={`numero ${p.precisaReposicao ? 'alerta' : ''}`}>
                {p.quantidadeEmEstoque}{p.precisaReposicao && ' ⚠️'}
              </td>
              <td className="numero">{p.estoqueMinimo}</td>
              <td>{p.ativo ? 'Ativo' : 'Inativo'}</td>
              <td className="acoes-linha">
                <Link to={`/produtos/${p.id}`}>Detalhes</Link>
                <button className="secundario" onClick={() => editar(p)}>Editar</button>
                <button className="secundario" onClick={() => alternarAtivo(p)}>{p.ativo ? 'Inativar' : 'Ativar'}</button>
                <button className="perigo" onClick={() => excluir(p)}>Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}
