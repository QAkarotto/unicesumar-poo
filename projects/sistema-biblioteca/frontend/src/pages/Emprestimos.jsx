
import { useEffect, useState } from 'react'
import { apiRequest } from '../services/api'

const formularioInicial = {
  livroId: '',
  leitorId: '',
}

function formatarData(data) {
  if (!data) return '—'

  const [ano, mes, dia] = data.split('-')
  return `${dia}/${mes}/${ano}`
}

function obterDataLocal() {
  const agora = new Date()
  const ano = agora.getFullYear()
  const mes = String(agora.getMonth() + 1).padStart(2, '0')
  const dia = String(agora.getDate()).padStart(2, '0')

  return `${ano}-${mes}-${dia}`
}

function estaAtrasado(emprestimo) {
  return (
    emprestimo.situacao === 'ATIVO' &&
    emprestimo.dataPrevistaDevolucao < obterDataLocal()
  )
}

export default function Emprestimos() {
  const [emprestimos, setEmprestimos] = useState([])
  const [livros, setLivros] = useState([])
  const [leitores, setLeitores] = useState([])

  const [formulario, setFormulario] = useState(formularioInicial)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [pesquisa, setPesquisa] = useState('')
  const [filtro, setFiltro] = useState('TODOS')

  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [devolvendoId, setDevolvendoId] = useState(null)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregarDados() {
    setCarregando(true)

    try {
      const [dadosEmprestimos, dadosLivros, dadosLeitores] =
        await Promise.all([
          apiRequest('/emprestimos'),
          apiRequest('/livros'),
          apiRequest('/leitores'),
        ])

      setEmprestimos(dadosEmprestimos)
      setLivros(dadosLivros)
      setLeitores(dadosLeitores)

      return true
    } catch (error) {
      setErro(error.message)
      return false
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarDados()
  }, [])

  function abrirFormulario() {
    setFormulario({ ...formularioInicial })
    setErro('')
    setSucesso('')
    setMostrarFormulario(true)
  }

  function cancelarFormulario() {
    setMostrarFormulario(false)
    setFormulario({ ...formularioInicial })
  }

  function atualizarCampo(event) {
    const { name, value } = event.target

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value,
    }))
  }

  async function registrarEmprestimo(event) {
    event.preventDefault()

    setErro('')
    setSucesso('')

    const livroId = Number(formulario.livroId)
    const leitorId = Number(formulario.leitorId)

    if (!livroId || !leitorId) {
      setErro('Selecione um livro e um leitor.')
      return
    }

    setSalvando(true)

    try {
      await apiRequest('/emprestimos', {
        method: 'POST',
        body: JSON.stringify({
          livroId,
          leitorId,
        }),
      })

      cancelarFormulario()

      const atualizado = await carregarDados()

      if (atualizado) {
        setSucesso('Empréstimo registrado com sucesso!')
      }
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  async function registrarDevolucao(emprestimo) {
    if (devolvendoId !== null) return

    const confirmado = window.confirm(
      `Confirmar a devolução do livro "${emprestimo.tituloLivro}"?`
    )

    if (!confirmado) return

    setErro('')
    setSucesso('')
    setDevolvendoId(emprestimo.id)

    try {
      await apiRequest(
        `/emprestimos/${emprestimo.id}/devolucao`,
        { method: 'PATCH' }
      )

      const atualizado = await carregarDados()

      if (atualizado) {
        setSucesso('Devolução registrada com sucesso!')
      }
    } catch (error) {
      setErro(error.message)
    } finally {
      setDevolvendoId(null)
    }
  }

  const livrosDisponiveis = livros.filter(
    (livro) => livro.quantidadeDisponivel > 0
  )

  const emprestimosFiltrados = emprestimos.filter((emprestimo) => {
    const termo = pesquisa.toLocaleLowerCase('pt-BR').trim()

    const correspondePesquisa =
      emprestimo.tituloLivro
        .toLocaleLowerCase('pt-BR')
        .includes(termo) ||
      emprestimo.nomeLeitor
        .toLocaleLowerCase('pt-BR')
        .includes(termo)

    let correspondeFiltro = true

    if (filtro === 'ATIVO') {
      correspondeFiltro = emprestimo.situacao === 'ATIVO'
    } else if (filtro === 'DEVOLVIDO') {
      correspondeFiltro = emprestimo.situacao === 'DEVOLVIDO'
    } else if (filtro === 'ATRASADO') {
      correspondeFiltro = estaAtrasado(emprestimo)
    }

    return correspondePesquisa && correspondeFiltro
  })

  function situacaoVisual(emprestimo) {
    if (emprestimo.situacao === 'DEVOLVIDO') {
      return {
        classe: 'devolvido',
        texto: 'Devolvido',
      }
    }

    if (estaAtrasado(emprestimo)) {
      return {
        classe: 'atrasado',
        texto: 'Atrasado',
      }
    }

    return {
      classe: 'ativo',
      texto: 'Ativo',
    }
  }

  return (
    <section>
      <div className="page-heading page-heading-actions">
        <div>
          <h1>Empréstimos</h1>
          <p>Gerencie empréstimos e devoluções da biblioteca.</p>
        </div>

        <button
          type="button"
          className="btn btn-primary"
          onClick={abrirFormulario}
        >
          + Novo empréstimo
        </button>
      </div>

      {erro && (
        <div className="alert alert-error" role="alert">
          {erro}
        </div>
      )}

      {sucesso && (
        <div className="alert alert-success" role="status">
          {sucesso}
        </div>
      )}

      {mostrarFormulario && (
        <div className="form-card">
          <h2>Registrar empréstimo</h2>

          <form onSubmit={registrarEmprestimo}>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="livroId">Livro *</label>

                <select
                  id="livroId"
                  name="livroId"
                  value={formulario.livroId}
                  onChange={atualizarCampo}
                  required
                >
                  <option value="">Selecione um livro</option>

                  {livrosDisponiveis.map((livro) => (
                    <option key={livro.id} value={livro.id}>
                      {livro.titulo} — {livro.quantidadeDisponivel} disponível(is)
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="leitorId">Leitor *</label>

                <select
                  id="leitorId"
                  name="leitorId"
                  value={formulario.leitorId}
                  onChange={atualizarCampo}
                  required
                >
                  <option value="">Selecione um leitor</option>

                  {leitores.map((leitor) => (
                    <option key={leitor.id} value={leitor.id}>
                      {leitor.nome} — {leitor.email}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <p className="form-hint">
              O prazo de devolução é de 14 dias. Cada leitor pode
              possuir no máximo 3 empréstimos ativos.
            </p>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={cancelarFormulario}
                disabled={salvando}
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="btn btn-primary"
                disabled={
                  salvando ||
                  livrosDisponiveis.length === 0 ||
                  leitores.length === 0
                }
              >
                {salvando
                  ? 'Registrando...'
                  : 'Registrar empréstimo'}
              </button>
            </div>
          </form>

          {livrosDisponiveis.length === 0 && (
            <p className="mensagem-erro">
              Não existem livros disponíveis para empréstimo.
            </p>
          )}

          {leitores.length === 0 && (
            <p className="mensagem-erro">
              Cadastre um leitor antes de registrar empréstimos.
            </p>
          )}
        </div>
      )}

      <div className="table-toolbar">
        <input
          type="search"
          placeholder="Pesquisar por livro ou leitor..."
          value={pesquisa}
          onChange={(event) => setPesquisa(event.target.value)}
          aria-label="Pesquisar empréstimos"
        />

        <div className="filter-group">
          <label htmlFor="filtroEmprestimos">Situação:</label>

          <select
            id="filtroEmprestimos"
            value={filtro}
            onChange={(event) => setFiltro(event.target.value)}
          >
            <option value="TODOS">Todos</option>
            <option value="ATIVO">Ativos</option>
            <option value="DEVOLVIDO">Devolvidos</option>
            <option value="ATRASADO">Atrasados</option>
          </select>
        </div>

        <span>{emprestimosFiltrados.length} registro(s)</span>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Livro</th>
              <th>Leitor</th>
              <th>Empréstimo</th>
              <th>Previsão</th>
              <th>Devolução</th>
              <th>Situação</th>
              <th>Ações</th>
            </tr>
          </thead>

          <tbody>
            {carregando && (
              <tr>
                <td colSpan="7">Carregando empréstimos...</td>
              </tr>
            )}

            {!carregando && emprestimosFiltrados.map(
              (emprestimo) => {
                const situacao = situacaoVisual(emprestimo)

                return (
                  <tr key={emprestimo.id}>
                    <td>{emprestimo.tituloLivro}</td>
                    <td>{emprestimo.nomeLeitor}</td>
                    <td>{formatarData(emprestimo.dataEmprestimo)}</td>
                    <td>
                      {formatarData(
                        emprestimo.dataPrevistaDevolucao
                      )}
                    </td>
                    <td>
                      {formatarData(emprestimo.dataDevolucao)}
                    </td>
                    <td>
                      <span className={`status ${situacao.classe}`}>
                        {situacao.texto}
                      </span>
                    </td>
                    <td>
                      {emprestimo.situacao === 'ATIVO' ? (
                        <button
                          type="button"
                          className="btn btn-small btn-primary"
                          onClick={() =>
                            registrarDevolucao(emprestimo)
                          }
                          disabled={devolvendoId !== null}
                        >
                          {devolvendoId === emprestimo.id
                            ? 'Devolvendo...'
                            : 'Registrar devolução'}
                        </button>
                      ) : (
                        <span className="action-completed">
                          Concluído
                        </span>
                      )}
                    </td>
                  </tr>
                )
              }
            )}

            {!carregando && emprestimosFiltrados.length === 0 && (
              <tr>
                <td colSpan="7">
                  Nenhum empréstimo encontrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </section>
  )
}
