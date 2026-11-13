import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api, cnpjFormatado, dataHora, moeda, query } from '../api.js'
import Mensagem from '../Mensagem.jsx'
import TabelaMovimentacoes from '../TabelaMovimentacoes.jsx'

export default function ProdutoDetalhe() {
  const { id } = useParams()
  const [detalhe, setDetalhe] = useState(null)
  const [historico, setHistorico] = useState([])
  const [todosFornecedores, setTodosFornecedores] = useState([])
  const [fornecedorEscolhido, setFornecedorEscolhido] = useState('')
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  const carregar = () =>
    Promise.all([api.get(`/produtos/${id}`), api.get('/movimentacoes' + query({ produtoId: id }))])
      .then(([d, h]) => { setDetalhe(d); setHistorico(h) })
      .catch(setErro)

  useEffect(() => {
    carregar()
    api.get('/fornecedores').then(setTodosFornecedores).catch(setErro)
  }, [id])

  async function vincular(e) {
    e.preventDefault()
    if (!fornecedorEscolhido) return
    try {
      const d = await api.post(`/produtos/${id}/fornecedores/${fornecedorEscolhido}`)
      setDetalhe(d)
      setFornecedorEscolhido('')
      setErro(null); setSucesso('Fornecedor vinculado.')
    } catch (e) { setSucesso(null); setErro(e) }
  }

  async function desvincular(f) {
    try {
      const d = await api.delete(`/produtos/${id}/fornecedores/${f.id}`)
      setDetalhe(d)
      setErro(null); setSucesso('Fornecedor desvinculado.')
    } catch (e) { setSucesso(null); setErro(e) }
  }

  if (!detalhe) return <Mensagem erro={erro} />

  const { produto: p, fornecedores } = detalhe
  const vinculados = new Set(fornecedores.map((f) => f.id))
  const disponiveis = todosFornecedores.filter((f) => !vinculados.has(f.id))

  return (
    <section>
      <p><Link to="/produtos">← Voltar para produtos</Link></p>
      <h2>{p.nome} <small>({p.sku})</small></h2>
      <Mensagem erro={erro} sucesso={sucesso} aoFechar={() => { setErro(null); setSucesso(null) }} />

      <div className="cartoes">
        <div className={`cartao ${p.precisaReposicao ? 'cartao-alerta' : ''}`}>
          <span>Saldo atual</span><strong>{p.quantidadeEmEstoque}</strong>
        </div>
        <div className="cartao"><span>Estoque mínimo</span><strong>{p.estoqueMinimo}</strong></div>
        <div className="cartao"><span>Preço unitário</span><strong>{moeda(p.precoUnitario)}</strong></div>
        <div className="cartao"><span>Valor em estoque</span><strong>{moeda(p.valorEmEstoque)}</strong></div>
        <div className="cartao"><span>Situação</span><strong>{p.ativo ? 'Ativo' : 'Inativo'}</strong></div>
      </div>
      <p><strong>Categoria:</strong> {p.categoria} {p.descricao && <> · <strong>Descrição:</strong> {p.descricao}</>}</p>
      <p><Link to={`/movimentacoes?produtoId=${p.id}`}>➕ Registrar movimentação deste produto</Link></p>

      <h3>Fornecedores vinculados</h3>
      <p className="dica">Somente fornecedores vinculados podem registrar entradas deste produto.</p>
      <table>
        <thead><tr><th>Nome</th><th>CNPJ</th><th>E-mail</th><th></th></tr></thead>
        <tbody>
          {fornecedores.length === 0 && <tr><td colSpan="4" className="vazio">Nenhum fornecedor vinculado.</td></tr>}
          {fornecedores.map((f) => (
            <tr key={f.id}>
              <td>{f.nome}</td>
              <td>{cnpjFormatado(f.cnpj)}</td>
              <td>{f.email}</td>
              <td><button className="perigo" onClick={() => desvincular(f)}>Desvincular</button></td>
            </tr>
          ))}
        </tbody>
      </table>
      <form className="filtros" onSubmit={vincular}>
        <select value={fornecedorEscolhido} onChange={(e) => setFornecedorEscolhido(e.target.value)}>
          <option value="">Selecione um fornecedor...</option>
          {disponiveis.map((f) => <option key={f.id} value={f.id}>{f.nome}</option>)}
        </select>
        <button type="submit" disabled={!fornecedorEscolhido}>Vincular fornecedor</button>
      </form>

      <h3>Histórico de movimentações</h3>
      <TabelaMovimentacoes movimentacoes={historico} mostrarProduto={false} />
      {historico.length > 0 && <p className="dica">Última movimentação: {dataHora(historico[0].dataHora)}</p>}
    </section>
  )
}
