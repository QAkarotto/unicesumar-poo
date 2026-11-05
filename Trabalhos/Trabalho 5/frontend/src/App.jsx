import { useState } from 'react'
import Alunos from './Alunos.jsx'
import Disciplinas from './Disciplinas.jsx'
import Matriculas from './Matriculas.jsx'

function App() {
  const [tela, setTela] = useState('alunos')

  return (
    <div>
      <h1>Sistema Acadêmico</h1>
      <nav>
        <button className={tela === 'alunos' ? 'ativo' : ''} onClick={() => setTela('alunos')}>Alunos</button>
        <button className={tela === 'disciplinas' ? 'ativo' : ''} onClick={() => setTela('disciplinas')}>Disciplinas</button>
        <button className={tela === 'matriculas' ? 'ativo' : ''} onClick={() => setTela('matriculas')}>Matrículas</button>
      </nav>

      {tela === 'alunos' && <Alunos />}
      {tela === 'disciplinas' && <Disciplinas />}
      {tela === 'matriculas' && <Matriculas />}
    </div>
  )
}

export default App
