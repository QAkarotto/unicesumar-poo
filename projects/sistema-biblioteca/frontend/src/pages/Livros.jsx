
import { useEffect, useState } from 'react'
import { apiRequest } from '../services/api'

const formularioInicial = {
  titulo: '',
  autor: '',
  isbn: '',
  quantidadeTotal: 1,
}

export default function Livros() {
  const [livros, setLivros] = useState([])
  const [pesquisa, setPesquisa] = useState('')
  const [formulario, setFormulario] = useState(formularioInicial)
  const [editandoId, setEditandoId] = useState(null)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregarLivros() {
    setCarregando(true)
    setErro('')

    try {
      const dados = await apiRequest('/livros')
      setLivros(dados)
    } catch (error) {
      setErro(error.message)
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarLivros()
  }, [])

  function atualizarCampo(event) {
    const { name, value } = event.target

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value,
    }))
  }

  function novoLivro() {
    setFormulario({ ...formularioInicial })
    setEditandoId(null)
    setErro('')
    setSucesso('')
    setMostrarFormulario(true)
  }

  function editarLivro(livro) {
    setFormulario({
      titulo: livro.titulo,
      autor: livro.autor,
      isbn: livro.isbn,
      quantidadeTotal: livro.quantidadeTotal,
    })

    setEditandoId(livro.id)
    setErro('')
    setSucesso('')
    setMostrarFormulario(true)
  }

  function cancelarFormulario() {
    setMostrarFormulario(false)
    setEditandoId(null)
    setFormulario({ ...formularioInicial })
  }

  async function salvarLivro(event) {
    event.preventDefault()

    setErro('')
    setSucesso('')

    const quantidade = Number(formulario.quantidadeTotal)

    if (
      !formulario.titulo.trim() ||
      !formulario.autor.trim() ||
      !formulario.isbn.trim()
    ) {
      setErro('Preencha todos os campos obrigatórios.')
      return
    }

    if (!Number.isInteger(quantidade) || quantidade < 0) {
      setErro('A quantidade deve ser um número inteiro não negativo.')
      return
    }

    const dados = {
      titulo: formulario.titulo.trim(),
      autor: formulario.autor.trim(),
      isbn: formulario.isbn.trim(),
      quantidadeTotal: quantidade,
    }

    setSalvando(true)

    try {
      if (editandoId !== null) {
        await apiRequest(`/livros/${editandoId}`, {
          method: 'PUT',
          body: JSON.stringify(dados),
        })

        setSucesso('Livro atualizado com sucesso!')
      } else {
        await apiRequest('/livros', {
          method: 'POST',
          body: JSON.stringify(dados),
        })

        setSucesso('Livro cadastrado com sucesso!')
      }

      cancelarFormulario()
      await carregarLivros()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  async function excluirLivro(livro) {
    const confirmado = window.confirm(
      `Deseja realmente excluir o livro "${livro.titulo}"?`
    )

    if (!confirmado) return

    setErro('')
    setSucesso('')

    try {
      await apiRequest(`/livros/${livro.id}`, {
        method: 'DELETE',
      })

      setSucesso('Livro excluído com sucesso!')
      await carregarLivros()
    } catch (error) {
      setErro(error.message)
    }
  }

  const livrosFiltrados = livros.filter((livro) => {
    const termo = pesquisa.toLowerCase()

    return (
      livro.titulo.toLowerCase().includes(termo) ||
      livro.autor.toLowerCase().includes(termo) ||
      livro.isbn.toLowerCase().includes(termo)
    )
  })

  return (
    <section>
      <div className="page-heading page-heading-actions">
        <div>
          <h1>Livros</h1>
          <p>Gerencie o acervo da biblioteca.</p>
        </div>

        <button className="btn btn-primary" onClick={novoLivro}>
          + Novo livro
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
          <h2>
            {editandoId !== null ? 'Editar livro' : 'Cadastrar livro'}
          </h2>

          <form onSubmit={salvarLivro}>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="titulo">Título *</label>
                <input
                  id="titulo"
                  name="titulo"
                  value={formulario.titulo}
                  onChange={atualizarCampo}
                  maxLength={200}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="autor">Autor *</label>
                <input
                  id="autor"
                  name="autor"
                  value={formulario.autor}
                  onChange={atualizarCampo}
                  maxLength={150}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="isbn">ISBN *</label>
                <input
                  id="isbn"
                  name="isbn"
                  value={formulario.isbn}
                  onChange={atualizarCampo}
                  maxLength={20}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="quantidadeTotal">
                  Quantidade total *
                </label>
                <input
                  id="quantidadeTotal"
                  name="quantidadeTotal"
                  type="number"
                  min="0"
                  step="1"
                  value={formulario.quantidadeTotal}
                  onChange={atualizarCampo}
                  required
                />
              </div>
            </div>

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
                disabled={salvando}
              >
                {salvando ? 'Salvando...' : 'Salvar livro'}
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="table-toolbar">
        <input
          type="search"
          placeholder="Pesquisar por título, autor ou ISBN..."
          value={pesquisa}
          onChange={(event) => setPesquisa(event.target.value)}
          aria-label="Pesquisar livros"
        />

        <span>{livrosFiltrados.length} livro(s)</span>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Título</th>
              <th>Autor</th>
              <th>ISBN</th>
              <th>Disponíveis</th>
              <th>Ações</th>
            </tr>
          </thead>

          <tbody>
            {livrosFiltrados.map((livro) => (
              <tr key={livro.id}>
                <td>{livro.titulo}</td>
                <td>{livro.autor}</td>
                <td>{livro.isbn}</td>
                <td>
                  {livro.quantidadeDisponivel}/
                  {livro.quantidadeTotal}
                </td>
                <td>
                  <div className="table-actions">
                    <button
                      className="btn btn-small btn-secondary"
                      onClick={() => editarLivro(livro)}
                    >
                      Editar
                    </button>

                    <button
                      className="btn btn-small btn-danger"
                      onClick={() => excluirLivro(livro)}
                    >
                      Excluir
                    </button>
                  </div>
                </td>
              </tr>
            ))}

            {!carregando && livrosFiltrados.length === 0 && (
              <tr>
                <td colSpan="5">
                  Nenhum livro encontrado.
                </td>
              </tr>
            )}

            {carregando && (
              <tr>
                <td colSpan="5">Carregando livros...</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </section>
  )
}
