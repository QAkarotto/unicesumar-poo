import { useEffect, useState } from 'react'
import { api } from './api.js'

function Matriculas() {
  const [matriculas, setMatriculas] = useState([])
  const [alunos, setAlunos] = useState([])
  const [ofertas, setOfertas] = useState([])
  const [raAluno, setRaAluno] = useState('')
  const [idOferta, setIdOferta] = useState('')
  const [erro, setErro] = useState('')

  async function carregar() {
    try {
      setMatriculas(await api('/matriculas'))
      setAlunos(await api('/alunos'))
      setOfertas(await api('/ofertas'))
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  async function matricular(evento) {
    evento.preventDefault()
    setErro('')
    try {
      await api('/matriculas', {
        method: 'POST',
        body: JSON.stringify({ raAluno: raAluno || null, idOferta: idOferta ? Number(idOferta) : null }),
      })
      setRaAluno('')
      setIdOferta('')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  async function concluir(id, resultado) {
    setErro('')
    try {
      await api('/matriculas/' + id + '/conclusao', {
        method: 'PATCH',
        body: JSON.stringify({ resultado }),
      })
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <div>
      <h2>Matrículas</h2>

      <form onSubmit={matricular}>
        <select value={raAluno} onChange={(e) => setRaAluno(e.target.value)}>
          <option value="">Escolha o aluno</option>
          {alunos.map((aluno) => (
            <option key={aluno.ra} value={aluno.ra}>
              {aluno.ra} - {aluno.nome}
            </option>
          ))}
        </select>
        <select value={idOferta} onChange={(e) => setIdOferta(e.target.value)}>
          <option value="">Escolha a oferta</option>
          {ofertas.map((oferta) => (
            <option key={oferta.id} value={oferta.id}>
              {oferta.turma} - {oferta.nomeDisciplina} ({oferta.ano}/{oferta.semestre})
            </option>
          ))}
        </select>
        <button type="submit">Matricular</button>
      </form>

      {erro && <p className="erro">{erro}</p>}

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Aluno</th>
            <th>Turma</th>
            <th>Disciplina</th>
            <th>Resultado</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {matriculas.map((matricula) => (
            <tr key={matricula.id}>
              <td>{matricula.id}</td>
              <td>{matricula.nomeAluno}</td>
              <td>{matricula.turma}</td>
              <td>{matricula.disciplina}</td>
              <td>{matricula.resultado}</td>
              <td>
                {matricula.resultado === 'CURSANDO' && (
                  <>
                    <button onClick={() => concluir(matricula.id, 'APROVADO')}>Aprovar</button>
                    <button onClick={() => concluir(matricula.id, 'REPROVADO')}>Reprovar</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default Matriculas
