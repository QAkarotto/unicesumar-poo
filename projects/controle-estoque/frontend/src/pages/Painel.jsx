import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, moeda } from '../api.js'
import Mensagem from '../Mensagem.jsx'

export default function Painel() {
  const [resumo, setResumo] = useState(null)
  const [reposicao, setReposicao] = useState([])
  const [erro, setErro] = useState(null)

  useEffect(() => {
    Promise.all([api.get('/produtos/resumo'), api.get('/produtos/reposicao')])
      .then(([r, p]) => {
        setResumo(r)
        setReposicao(p)
      })
      .catch(setErro)
  }, [])

  return (
    <section>
      <h2>Painel</h2>
      <Mensagem erro={erro} />

      {resumo && (
        <div className="cartoes">
          <Cartao titulo="Produtos cadastrados" valor={resumo.totalProdutos} />
          <Cartao titulo="Produtos ativos" valor={resumo.produtosAtivos} />
          <Cartao titulo="Unidades em estoque" valor={resumo.unidadesEmEstoque} />
          <Cartao titulo="Valor total em estoque" valor={moeda(resumo.valorTotalEmEstoque)} />
          <Cartao titulo="Precisam de reposição" valor={resumo.produtosParaReposicao}
                  alerta={resumo.produtosParaReposicao > 0} />
        </div>
      )}

      <h3>⚠️ Produtos para reposição</h3>
      {reposicao.length === 0 ? (
        <p className="vazio">Nenhum produto abaixo do estoque mínimo.</p>
      ) : (
        <table>
          <thead>
            <tr><th>SKU</th><th>Produto</th><th>Categoria</th><th>Saldo</th><th>Mínimo</th><th></th></tr>
          </thead>
          <tbody>
            {reposicao.map((p) => (
              <tr key={p.id}>
                <td>{p.sku}</td>
                <td>{p.nome}</td>
                <td>{p.categoria}</td>
                <td className="numero alerta">{p.quantidadeEmEstoque}</td>
                <td className="numero">{p.estoqueMinimo}</td>
                <td><Link to={`/produtos/${p.id}`}>Detalhes</Link></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}

function Cartao({ titulo, valor, alerta }) {
  return (
    <div className={`cartao ${alerta ? 'cartao-alerta' : ''}`}>
      <span>{titulo}</span>
      <strong>{valor}</strong>
    </div>
  )
}
