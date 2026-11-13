
import { useCallback, useEffect, useState } from 'react'
import { apiRequest } from '../services/api'

function obterDataLocal() {
  const agora = new Date()
  const ano = agora.getFullYear()
  const mes = String(agora.getMonth() + 1).padStart(2, '0')
  const dia = String(agora.getDate()).padStart(2, '0')

  return `${ano}-${mes}-${dia}`
}

export default function Dashboard() {
  const [dados, setDados] = useState(null)
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(true)

  const carregarDados = useCallback(async () => {
    setCarregando(true)
    setErro('')

    try {
      const [livros, leitores, emprestimos] = await Promise.all([
        apiRequest('/livros'),
        apiRequest('/leitores'),
        apiRequest('/emprestimos'),
      ])

      const hoje = obterDataLocal()

      setDados({
        titulos: livros.length,

        exemplares: livros.reduce(
          (total, livro) => total + livro.quantidadeTotal,
          0
        ),

        disponiveis: livros.reduce(
          (total, livro) => total + livro.quantidadeDisponivel,
          0
        ),

        leitores: leitores.length,

        ativos: emprestimos.filter(
          (emprestimo) => emprestimo.situacao === 'ATIVO'
        ).length,

        devolvidos: emprestimos.filter(
          (emprestimo) => emprestimo.situacao === 'DEVOLVIDO'
        ).length,

        atrasados: emprestimos.filter(
          (emprestimo) =>
            emprestimo.situacao === 'ATIVO' &&
            emprestimo.dataPrevistaDevolucao < hoje
        ).length,
      })
    } catch (error) {
      setErro(error.message)
    } finally {
      setCarregando(false)
    }
  }, [])

  useEffect(() => {
    carregarDados()
  }, [carregarDados])

  const indicadores = dados
    ? [
        {
          titulo: 'Títulos cadastrados',
          valor: dados.titulos,
        },
        {
          titulo: 'Total de exemplares',
          valor: dados.exemplares,
        },
        {
          titulo: 'Exemplares disponíveis',
          valor: dados.disponiveis,
        },
        {
          titulo: 'Leitores cadastrados',
          valor: dados.leitores,
        },
        {
          titulo: 'Empréstimos ativos',
          valor: dados.ativos,
        },
        {
          titulo: 'Empréstimos devolvidos',
          valor: dados.devolvidos,
        },
        {
          titulo: 'Empréstimos atrasados',
          valor: dados.atrasados,
        },
      ]
    : []

  return (
    <section>
      <div className="page-heading page-heading-actions">
        <div>
          <h1>Dashboard</h1>
          <p>
            Visão geral do acervo e das atividades da biblioteca.
          </p>
        </div>

        <button
          type="button"
          className="btn btn-secondary"
          onClick={carregarDados}
          disabled={carregando}
        >
          {carregando ? 'Atualizando...' : 'Atualizar dados'}
        </button>
      </div>

      {erro && (
        <div className="alert alert-error" role="alert">
          {erro}
        </div>
      )}

      {carregando && !dados && (
        <p>Carregando informações...</p>
      )}

      {dados && (
        <>
          {carregando && (
            <p className="dashboard-loading">
              Atualizando indicadores...
            </p>
          )}

          <div className="stats-grid">
            {indicadores.map((item) => (
              <article className="stat-card" key={item.titulo}>
                <span>{item.titulo}</span>
                <strong>{item.valor}</strong>
              </article>
            ))}
          </div>
        </>
      )}
    </section>
  )
}
