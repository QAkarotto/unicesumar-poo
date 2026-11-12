import { useEffect, useMemo, useState } from 'react'
import { PawPrint, LayoutDashboard, CalendarDays, Users, Stethoscope, Dog, Plus, Search, RefreshCw } from 'lucide-react'

const API = 'http://localhost:8080/api'

async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  })
  if (!response.ok) {
    let message = `Erro HTTP ${response.status}`
    try {
      const data = await response.json()
      message = data.erro || data.message || data.erros?.join(', ') || message
    } catch {}
    throw new Error(message)
  }
  if (response.status === 204) return null
  return response.json()
}

function App() {
  const [page, setPage] = useState('Painel')
  const [tutores, setTutores] = useState([])
  const [animais, setAnimais] = useState([])
  const [veterinarios, setVeterinarios] = useState([])
  const [especialidades, setEspecialidades] = useState([])
  const [agendamentos, setAgendamentos] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [search, setSearch] = useState('')
  const [showForm, setShowForm] = useState(false)

  async function loadAll() {
    setError('')
    try {
      const [t, a, v, e, ag] = await Promise.all([
        request('/tutores'), request('/animais'), request('/veterinarios'),
        request('/especialidades'), request('/agendamentos')
      ])
      setTutores(t); setAnimais(a); setVeterinarios(v); setEspecialidades(e); setAgendamentos(ag)
    } catch (err) {
      setError(`${err.message}. Confira se o backend e o MySQL estão funcionando.`)
    }
  }

  useEffect(() => { loadAll() }, [])

  const today = new Date().toISOString().slice(0, 10)
  const todayAppointments = agendamentos.filter(a => a.dataHora?.startsWith(today) && a.status !== 'CANCELADO')
  const upcoming = [...agendamentos].filter(a => a.status !== 'CANCELADO')
    .sort((a,b) => a.dataHora.localeCompare(b.dataHora)).slice(0, 6)

  async function cancel(id) {
    try {
      await request(`/agendamentos/${id}/cancelar`, { method: 'PATCH' })
      setSuccess('Agendamento cancelado.')
      loadAll()
    } catch (err) { setError(err.message) }
  }

  async function submitForm(event) {
    event.preventDefault()
    const formElement = event.currentTarget
    const form = new FormData(formElement)
    const data = Object.fromEntries(form.entries())
    try {
      if (page === 'Tutores') {
        await request('/tutores', { method: 'POST', body: JSON.stringify(data) })
      } else if (page === 'Animais') {
        data.tutorId = Number(data.tutorId)
        data.peso = data.peso ? Number(data.peso) : null
        data.dataNascimento = data.dataNascimento || null
        await request('/animais', { method: 'POST', body: JSON.stringify(data) })
      } else if (page === 'Veterinários') {
        data.especialidadeId = Number(data.especialidadeId)
        await request('/veterinarios', { method: 'POST', body: JSON.stringify(data) })
      } else if (page === 'Especialidades') {
        await request('/especialidades', { method: 'POST', body: JSON.stringify(data) })
      } else if (page === 'Agendamentos') {
        data.animalId = Number(data.animalId)
        data.veterinarioId = Number(data.veterinarioId)
        data.dataHora = data.dataHora
        await request('/agendamentos', { method: 'POST', body: JSON.stringify(data) })
      }
      setSuccess('Cadastro realizado com sucesso!')
      setShowForm(false)
      formElement.reset()
      loadAll()
    } catch (err) { setError(err.message) }
  }

  const menu = [
    ['Painel', LayoutDashboard], ['Agendamentos', CalendarDays], ['Tutores', Users],
    ['Animais', Dog], ['Veterinários', Stethoscope], ['Especialidades', PawPrint]
  ]

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><span className="brand-icon"><PawPrint size={22}/></span><div><b>VetAgenda</b><small>Clínica veterinária</small></div></div>
      <div className="menu-label">MENU PRINCIPAL</div>
      {menu.map(([name, Icon]) => <button key={name} className={`nav-item ${page === name ? 'active' : ''}`} onClick={() => {setPage(name);setShowForm(false);setError('');setSuccess('')}}><Icon size={18}/>{name}</button>)}
      <div className="sidebar-bottom"><div className="avatar">VA</div><div><b>Administrador</b><small>Gestão da clínica</small></div></div>
    </aside>

    <main className="main">
      <header className="topbar"><div><span className="eyebrow">GESTÃO VETERINÁRIA</span><h1>{page}</h1></div><button className="outline-btn" onClick={loadAll}><RefreshCw size={16}/> Atualizar</button></header>
      {error && <div className="alert error">{error}<button onClick={()=>setError('')}>×</button></div>}
      {success && <div className="alert success">{success}<button onClick={()=>setSuccess('')}>×</button></div>}

      {page === 'Painel' ? <Dashboard todayAppointments={todayAppointments} upcoming={upcoming} animals={animais} tutors={tutores} veterinarios={veterinarios} setPage={setPage}/> :
        <section className="panel">
          <div className="panel-heading"><div><h2>{page} cadastrados</h2><p>Gerencie as informações da clínica.</p></div>
            <button className="primary-btn" onClick={()=>setShowForm(!showForm)}><Plus size={17}/> Novo cadastro</button></div>
          {showForm && <Form page={page} tutores={tutores} animais={animais} veterinarios={veterinarios} especialidades={especialidades} onSubmit={submitForm}/>}
          <div className="search-box"><Search size={17}/><input placeholder="Buscar nesta lista..." value={search} onChange={e=>setSearch(e.target.value)}/></div>
          <DataList page={page} data={{'Tutores':tutores,'Animais':animais,'Veterinários':veterinarios,'Especialidades':especialidades,'Agendamentos':agendamentos}[page] || []} search={search} onCancel={cancel}/>
        </section>}
      <footer>VetAgenda • Projeto acadêmico de Engenharia de Software</footer>
    </main>
  </div>
}

function Dashboard({todayAppointments, upcoming, animals, tutors, veterinarios, setPage}) {
  const cards = [
    ['Consultas hoje', todayAppointments.length, CalendarDays, 'agenda'],
    ['Animais cadastrados', animals.length, Dog, 'animais'],
    ['Tutores cadastrados', tutors.length, Users, 'tutores'],
    ['Veterinários', veterinarios.length, Stethoscope, 'veterinarios']
  ]
  return <>
    <div className="welcome"><div><span>SEJA BEM-VINDO(A) 👋</span><h2>Cuidando de quem faz parte da família.</h2><p>Acompanhe a rotina e os atendimentos da sua clínica.</p></div><div className="welcome-paw"><PawPrint size={76}/></div></div>
    <div className="stats-grid">{cards.map(([label,value,Icon])=><div className="stat-card" key={label}><div className="stat-icon"><Icon size={20}/></div><span>{label}</span><strong>{value}</strong></div>)}</div>
    <section className="panel"><div className="panel-heading"><div><h2>Próximos agendamentos</h2><p>Consultas futuras registradas no sistema.</p></div><button className="text-btn" onClick={()=>setPage('Agendamentos')}>Ver todos →</button></div><AppointmentTable data={upcoming}/></section>
  </>
}

function Form({page,tutores,animais,veterinarios,especialidades,onSubmit}) {
  return <form className="form-grid" onSubmit={onSubmit}>
    {page === 'Tutores' && <>
      <Field label="Nome completo" name="nome" required/><Field label="CPF" name="cpf" required/><Field label="Telefone" name="telefone" required/><Field label="E-mail" name="email" type="email"/>
    </>}
    {page === 'Animais' && <>
      <Field label="Nome do animal" name="nome" required/><Field label="Espécie" name="especie" placeholder="Cachorro, gato..." required/><Field label="Raça" name="raca"/><Field label="Sexo" name="sexo" placeholder="Macho ou fêmea"/><Field label="Nascimento" name="dataNascimento" type="date"/><Field label="Peso (kg)" name="peso" type="number" step="0.01"/><SelectField label="Tutor" name="tutorId" required options={tutores.map(t=>[t.id,t.nome])}/>
    </>}
    {page === 'Veterinários' && <>
      <Field label="Nome" name="nome" required/><Field label="CRMV" name="crmv" required/><Field label="Telefone" name="telefone"/><SelectField label="Especialidade" name="especialidadeId" required options={especialidades.map(e=>[e.id,e.nome])}/>
    </>}
    {page === 'Especialidades' && <><Field label="Nome" name="nome" required/><Field label="Descrição" name="descricao"/></>}
    {page === 'Agendamentos' && <>
      <Field label="Data e horário" name="dataHora" type="datetime-local" required/><Field label="Motivo" name="motivo" required/><Field label="Observações" name="observacoes"/><SelectField label="Animal" name="animalId" required options={animais.map(a=>[a.id,a.nome])}/><SelectField label="Veterinário" name="veterinarioId" required options={veterinarios.map(v=>[v.id,v.nome])}/>
    </>}
    <div className="form-actions"><button className="primary-btn" type="submit">Salvar cadastro</button></div>
  </form>
}

function Field({label,name,type='text',required=false,placeholder='',step}) {
  return <label className="field"><span>{label}</span><input name={name} type={type} required={required} placeholder={placeholder} step={step}/></label>
}
function SelectField({label,name,options,required=false}) {
  return <label className="field"><span>{label}</span><select name={name} required={required} defaultValue=""><option value="">Selecione...</option>{options.map(([id,label])=><option key={id} value={id}>{label}</option>)}</select></label>
}

function DataList({page,data,search,onCancel}) {
  const filtered = data.filter(item=>JSON.stringify(item).toLowerCase().includes(search.toLowerCase()))
  if (page === 'Agendamentos') return <AppointmentTable data={filtered} onCancel={onCancel}/>
  const columns = {
    'Tutores': ['nome','cpf','telefone','email'],
    'Animais': ['nome','especie','raca','tutorNome'],
    'Veterinários': ['nome','crmv','telefone','especialidade'],
    'Especialidades': ['nome','descricao']
  }[page] || []
  if (!filtered.length) return <div className="empty"><PawPrint size={30}/><b>Nenhum registro encontrado</b><span>Cadastre o primeiro item para começar.</span></div>
  return <div className="table-wrap"><table><thead><tr>{columns.map(c=><th key={c}>{({nome:'Nome',cpf:'CPF',telefone:'Telefone',email:'E-mail',especie:'Espécie',raca:'Raça',tutorNome:'Tutor',crmv:'CRMV',especialidade:'Especialidade',descricao:'Descrição'})[c] || c}</th>)}</tr></thead><tbody>{filtered.map((item,i)=><tr key={item.id || i}>{columns.map(c=><td key={c}>{item[c] || '—'}</td>)}</tr>)}</tbody></table></div>
}

function AppointmentTable({data,onCancel}) {
  if (!data.length) return <div className="empty"><CalendarDays size={30}/><b>Nenhum agendamento encontrado</b><span>Os próximos atendimentos aparecerão aqui.</span></div>
  return <div className="table-wrap"><table><thead><tr><th>Data e hora</th><th>Animal</th><th>Veterinário</th><th>Motivo</th><th>Status</th>{onCancel&&<th>Ação</th>}</tr></thead><tbody>{data.map(a=><tr key={a.id}><td>{new Date(a.dataHora).toLocaleString('pt-BR')}</td><td><b>{a.animalNome}</b></td><td>{a.veterinarioNome}</td><td>{a.motivo}</td><td><span className={`status ${a.status?.toLowerCase()}`}>{a.status}</span></td>{onCancel&&<td>{['AGENDADO','CONFIRMADO'].includes(a.status)&&<button className="cancel-btn" onClick={()=>onCancel(a.id)}>Cancelar</button>}</td>}</tr>)}</tbody></table></div>
}

export default App
