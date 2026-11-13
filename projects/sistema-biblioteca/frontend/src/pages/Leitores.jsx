
import { useEffect, useState } from 'react'
import { apiRequest } from '../services/api'

const formularioInicial = {
  nome: '',
  email: '',
  telefone: '',
}

export default function Leitores() {
  const [leitores, setLeitores] = useState([])
  const [pesquisa, setPesquisa] = useState('')
  const [formulario, setFormulario] = useState(formularioInicial)
  const [editandoId, setEditandoId] = useState(null)
  const [mostrarFormulario, setMostrarFormulario] = useState(false)
  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [excluindoId, setExcluindoId] = useState(null)
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregarLeitores() {
    setCarregando(true)

    try {
      const dados = await apiRequest('/leitores')
      setLeitores(dados)
      return true
    } catch (error) {
      setErro(error.message)
      return false
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarLeitores()
  }, [])

  function atualizarCampo(event) {
    const { name, value } = event.target

    setFormulario((anterior) => ({
      ...anterior,
      [name]: value,
    }))
  }

  function novoLeitor() {
    setFormulario({ ...formularioInicial })
    setEditandoId(null)
    setErro('')
    setSucesso('')
    setMostrarFormulario(true)
  }

  function editarLeitor(leitor) {
    setFormulario({
      nome: leitor.nome,
      email: leitor.email,
      telefone: leitor.telefone || '',
    })

    setEditandoId(leitor.id)
    setErro('')
    setSucesso('')
    setMostrarFormulario(true)
  }

  function cancelarFormulario() {
    setMostrarFormulario(false)
    setEditandoId(null)
    setFormulario({ ...formularioInicial })
  }

  async function salvarLeitor(event) {
    event.preventDefault()

    setErro('')
    setSucesso('')

    const dados = {
      nome: formulario.nome.trim(),
      email: formulario.email.trim(),
      telefone: formulario.telefone.trim() || null,
    }

    if (!dados.nome || !dados.email) {
      setErro('Preencha o nome e o e-mail do leitor.')
      return
    }

    setSalvando(true)

    try {
      if (editandoId !== null) {
        await apiRequest(`/leitores/${editandoId}`, {
          method: 'PUT',
          body: JSON.stringify(dados),
        })

        setSucesso('Leitor atualizado com sucesso!')
      } else {
        await apiRequest('/leitores', {
          method: 'POST',
          body: JSON.stringify(dados),
        })

        setSucesso('Leitor cadastrado com sucesso!')
      }

      cancelarFormulario()
      await carregarLeitores()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  async function excluirLeitor(leitor) {
    if (excluindoId !== null) return

    const confirmado = window.confirm(
      `Deseja realmente excluir o leitor "${leitor.nome}"?`
    )

    if (!confirmado) return

    setErro('')
    setSucesso('')
    setExcluindoId(leitor.id)

    try {
      await apiRequest(`/leitores/${leitor.id}`, {
        method: 'DELETE',
      })

      setSucesso('Leitor excluído com sucesso!')
      await carregarLeitores()
    } catch (error) {
      setErro(error.message)
    } finally {
      setExcluindoId(null)
    }
  }

  const leitoresFiltrados = leitores.filter((leitor) => {
    const termo = pesquisa.toLocaleLowerCase('pt-BR').trim()

    return (
      leitor.nome.toLocaleLowerCase('pt-BR').includes(termo) ||
      leitor.email.toLocaleLowerCase('pt-BR').includes(termo) ||
      (leitor.telefone || '').includes(termo)
    )
  })

  return (
    <section>
      <div className="page-heading page-heading-actions">
        <div>
          <h1>Leitores</h1>
          <p>Gerencie os leitores cadastrados na biblioteca.</p>
        </div>

        <button
          type="button"
          className="btn btn-primary"
          onClick={novoLeitor}
        >
          + Novo leitor
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
            {editandoId !== null
              ? 'Editar leitor'
              : 'Cadastrar leitor'}
          </h2>

          <form onSubmit={salvarLeitor}>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="nome">Nome completo *</label>
                <input
                  id="nome"
                  name="nome"
                  type="text"
                  value={formulario.nome}
                  onChange={atualizarCampo}
                  maxLength={150}
                  placeholder="Nome do leitor"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">E-mail *</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  value={formulario.email}
                  onChange={atualizarCampo}
                  maxLength={150}
                  placeholder="exemplo@email.com"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="telefone">Telefone</label>
                <input
                  id="telefone"
                  name="telefone"
                  type="tel"
                  value={formulario.telefone}
                  onChange={atualizarCampo}
                  maxLength={20}
                  placeholder="(41) 99999-9999"
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
                {salvando
                  ? 'Salvando...'
                  : editandoId !== null
                    ? 'Salvar alterações'
                    : 'Cadastrar leitor'}
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="table-toolbar">
        <input
          type="search"
          placeholder="Pesquisar por nome, e-mail ou telefone..."
          value={pesquisa}
          onChange={(event) => setPesquisa(event.target.value)}
          aria-label="Pesquisar leitores"
        />

        <span>
          {leitoresFiltrados.length} leitor(es)
        </span>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>E-mail</th>
              <th>Telefone</th>
              <th>Ações</th>
            </tr>
          </thead>

          <tbody>
            {!carregando && leitoresFiltrados.map((leitor) => (
              <tr key={leitor.id}>
                <td>{leitor.nome}</td>
                <td>{leitor.email}</td>
                <td>{leitor.telefone || 'Não informado'}</td>
                <td>
                  <div className="table-actions">
                    <button
                      type="button"
                      className="btn btn-small btn-secondary"
                      onClick={() => editarLeitor(leitor)}
                    >
                      Editar
                    </button>

                    <button
                      type="button"
                      className="btn btn-small btn-danger"
                      onClick={() => excluirLeitor(leitor)}
                      disabled={excluindoId !== null}
                    >
                      {excluindoId === leitor.id
                        ? 'Excluindo...'
                        : 'Excluir'}
                    </button>
                  </div>
                </td>
              </tr>
            ))}

            {carregando && (
              <tr>
                <td colSpan="4">Carregando leitores...</td>
              </tr>
            )}

            {!carregando && leitoresFiltrados.length === 0 && (
              <tr>
                <td colSpan="4">
                  Nenhum leitor encontrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </section>
  )
}
