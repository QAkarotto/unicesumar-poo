import { dataHora } from './api.js'

const ROTULO = { ENTRADA: '⬆ Entrada', SAIDA: '⬇ Saída', AJUSTE: '⟳ Ajuste' }

export default function TabelaMovimentacoes({ movimentacoes, mostrarProduto = true }) {
  return (
    <table>
      <thead>
        <tr>
          <th>Data/hora</th>
          {mostrarProduto && <th>Produto</th>}
          <th>Tipo</th><th>Qtd.</th><th>Saldo anterior</th><th>Saldo posterior</th>
          <th>Fornecedor</th><th>Observação</th>
        </tr>
      </thead>
      <tbody>
        {movimentacoes.length === 0 && (
          <tr><td colSpan={mostrarProduto ? 8 : 7} className="vazio">Nenhuma movimentação.</td></tr>
        )}
        {movimentacoes.map((m) => (
          <tr key={m.id}>
            <td>{dataHora(m.dataHora)}</td>
            {mostrarProduto && <td>{m.produtoSku} — {m.produtoNome}</td>}
            <td><span className={`tipo tipo-${m.tipo.toLowerCase()}`}>{ROTULO[m.tipo]}</span></td>
            <td className="numero">{m.quantidade}</td>
            <td className="numero">{m.saldoAnterior}</td>
            <td className="numero">{m.saldoPosterior}</td>
            <td>{m.fornecedorNome ?? '—'}</td>
            <td>{m.observacao}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
