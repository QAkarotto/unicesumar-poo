import { useEffect, useState } from 'react'
import { api } from './api.js'

function Disciplinas() {
  const [disciplinas, setDisciplinas] = useState([])
  const [codigo, setCodigo] = useState('')
  const [nome, setNome] = useState('')
  const [cargaHoraria, setCargaHoraria] = useState('')
  const [erro, setErro] = useState('')

  async function carregar() {
    try {
      setDisciplinas(await api('/disciplinas'))
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  async function cadastrar(evento) {
    evento.preventDefault()
    setErro('')
    try {
      await api('/disciplinas', {
        method: 'POST',
        body: JSON.stringify({ codigo, nome, cargaHoraria: Number(cargaHoraria) }),
      })
      setCodigo('')
      setNome('')
      setCargaHoraria('')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <div>
      <h2>Disciplinas</h2>

      <form onSubmit={cadastrar}>
        <input placeholder="Código" value={codigo} onChange={(e) => setCodigo(e.target.value)} />
        <input placeholder="Nome" value={nome} onChange={(e) => setNome(e.target.value)} />
        <input
          type="number"
          placeholder="Carga horária"
          value={cargaHoraria}
          onChange={(e) => setCargaHoraria(e.target.value)}
        />
        <button type="submit">Cadastrar</button>
      </form>

      {erro && <p className="erro">{erro}</p>}

      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Nome</th>
            <th>Carga horária</th>
          </tr>
        </thead>
        <tbody>
          {disciplinas.map((disciplina) => (
            <tr key={disciplina.codigo}>
              <td>{disciplina.codigo}</td>
              <td>{disciplina.nome}</td>
              <td>{disciplina.cargaHoraria}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default Disciplinas
