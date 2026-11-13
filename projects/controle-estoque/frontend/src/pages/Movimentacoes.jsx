import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { api, query } from '../api.js'
import Mensagem from '../Mensagem.jsx'
import TabelaMovimentacoes from '../TabelaMovimentacoes.jsx'

const AJUDA = {
  ENTRADA: 'Recebimento de mercadoria. Exige um fornecedor vinculado ao produto.',
  SAIDA: 'Retirada/venda. Não é permitido deixar o saldo negativo.',
  AJUSTE: 'Correção pelo inventário: informe o NOVO saldo contado e a justificativa (obrigatória).',
}

export default function Movimentacoes() {
  const [params] = useSearchParams()
  const [produtos, setProdutos] = useState([])
  const [fornecedoresDoProduto, setFornecedoresDoProduto] = useState([])
  const [saldoAtual, setSaldoAtual] = useState(null)
  const [movimentacoes, setMovimentacoes] = useState([])
  const [filtro, setFiltro] = useState({ produtoId: '', tipo: '' })
  const [form, setForm] = useState({
    produtoId: params.get('produtoId') ?? '', tipo: 'ENTRADA', quantidade: '', fornecedorId: '', observacao: '',
  })
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  const carregarHistorico = () =>
    api.get('/movimentacoes' + query(filtro)).then(setMovimentacoes).catch(setErro)

  useEffect(() => { api.get('/produtos').then(setProdutos).catch(setErro) }, [])
  useEffect(() => { carregarHistorico() }, [filtro])

  // Ao trocar o produto, busca os fornecedores vinculados e o saldo atual
  useEffect(() => {
    if (!form.produtoId) { setFornecedoresDoProduto([]); setSaldoAtual(null); return }
    api.get(`/produtos/${form.produtoId}`)
      .then((d) => { setFornecedoresDoProduto(d.fornecedores); setSaldoAtual(d.produto.quantidadeEmEstoque) })
      .catch(setErro)
  }, [form.produtoId])

  const campo = (nome) => ({
    value: form[nome],
    onChange: (e) => setForm({ ...form, [nome]: e.target.value }),
  })

  async function registrar(e) {
    e.preventDefault()
    // O mesmo campo da tela vira "quantidade" (ENTRADA/SAIDA) ou "novoSaldo" (AJUSTE)
    const valor = form.quantidade === '' ? null : Number(form.quantidade)
    const ajuste = form.tipo === 'AJUSTE'
    const corpo = {
      produtoId: form.produtoId === '' ? null : Number(form.produtoId),
      tipo: form.tipo,
      quantidade: ajuste ? null : valor,
      novoSaldo: ajuste ? valor : null,
      fornecedorId: form.tipo === 'ENTRADA' && form.fornecedorId !== '' ? Number(form.fornecedorId) : null,
      observacao: form.observacao || null,
    }
    try {
      const m = await api.post('/movimentacoes', corpo)
      setErro(null)
      setSucesso(`Movimentação registrada: saldo de ${m.produtoSku} passou de ${m.saldoAnterior} para ${m.saldoPosterior}.`)
      setSaldoAtual(m.saldoPosterior)
      setForm({ ...form, quantidade: '', observacao: '' })
      carregarHistorico()
      api.get('/produtos').then(setProdutos)
    } catch (e) { setSucesso(null); setErro(e) }
  }

  const ativos = produtos.filter((p) => p.ativo)

  return (
    <section>
      <h2>Movimentações de estoque</h2>
      <Mensagem erro={erro} sucesso={sucesso} aoFechar={() => { setErro(null); setSucesso(null) }} />

      <form className="formulario" onSubmit={registrar}>
        <h3>Registrar movimentação</h3>
        <label>Produto*
          <select {...campo('produtoId')}>
            <option value="">Selecione...</option>
            {ativos.map((p) => <option key={p.id} value={p.id}>{p.sku} — {p.nome}</option>)}
          </select>
        </label>
        <label>Tipo*
          <select {...campo('tipo')}>
            <option value="ENTRADA">Entrada</option>
            <option value="SAIDA">Saída</option>
            <option value="AJUSTE">Ajuste de inventário</option>
          </select>
        </label>
        <label>{form.tipo === 'AJUSTE' ? 'Novo saldo (contagem)*' : 'Quantidade*'}
          <input type="number" min="0" {...campo('quantidade')} />
        </label>
        {form.tipo === 'ENTRADA' && (
          <label>Fornecedor*
            <select {...campo('fornecedorId')}>
              <option value="">Selecione...</option>
              {fornecedoresDoProduto.map((f) => <option key={f.id} value={f.id}>{f.nome}</option>)}
            </select>
          </label>
        )}
        <label className="largo">{form.tipo === 'AJUSTE' ? 'Justificativa*' : 'Observação'}
          <input {...campo('observacao')} />
        </label>
        <p className="dica largo">
          {AJUDA[form.tipo]}
          {saldoAtual !== null && <> Saldo atual: <strong>{saldoAtual}</strong>.</>}
          {form.tipo === 'ENTRADA' && form.produtoId && fornecedoresDoProduto.length === 0 &&
            ' ⚠️ Este produto não tem fornecedores vinculados.'}
        </p>
        <div className="acoes"><button type="submit">Registrar</button></div>
      </form>

      <h3>Histórico</h3>
      <div className="filtros">
        <select value={filtro.produtoId} onChange={(e) => setFiltro({ ...filtro, produtoId: e.target.value })}>
          <option value="">Todos os produtos</option>
          {produtos.map((p) => <option key={p.id} value={p.id}>{p.sku} — {p.nome}</option>)}
        </select>
        <select value={filtro.tipo} onChange={(e) => setFiltro({ ...filtro, tipo: e.target.value })}>
          <option value="">Todos os tipos</option>
          <option value="ENTRADA">Entrada</option>
          <option value="SAIDA">Saída</option>
          <option value="AJUSTE">Ajuste</option>
        </select>
      </div>
      <TabelaMovimentacoes movimentacoes={movimentacoes} />
    </section>
  )
}
