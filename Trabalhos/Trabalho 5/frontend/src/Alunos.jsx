import { useEffect, useState } from 'react'
import { api } from './api.js'

function Alunos() {
  const [alunos, setAlunos] = useState([])
  const [ra, setRa] = useState('')
  const [nome, setNome] = useState('')
  const [email, setEmail] = useState('')
  const [erro, setErro] = useState('')

  async function carregar() {
    try {
      setAlunos(await api('/alunos'))
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
      await api('/alunos', {
        method: 'POST',
        body: JSON.stringify({ ra, nome, email }),
      })
      setRa('')
      setNome('')
      setEmail('')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <div>
      <h2>Alunos</h2>

      <form onSubmit={cadastrar}>
        <input placeholder="RA" value={ra} onChange={(e) => setRa(e.target.value)} />
        <input placeholder="Nome" value={nome} onChange={(e) => setNome(e.target.value)} />
        <input placeholder="E-mail" value={email} onChange={(e) => setEmail(e.target.value)} />
        <button type="submit">Cadastrar</button>
      </form>

      {erro && <p className="erro">{erro}</p>}

      <table>
        <thead>
          <tr>
            <th>RA</th>
            <th>Nome</th>
            <th>E-mail</th>
          </tr>
        </thead>
        <tbody>
          {alunos.map((aluno) => (
            <tr key={aluno.ra}>
              <td>{aluno.ra}</td>
              <td>{aluno.nome}</td>
              <td>{aluno.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default Alunos
