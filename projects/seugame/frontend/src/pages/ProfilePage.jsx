import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import { Message } from '../components/Feedback.jsx'

export default function ProfilePage({ user, onSubmit, onLogout, busy, logoutBusy, error }) {
  function submit(event) {
    event.preventDefault()
    onSubmit?.(Object.fromEntries(new FormData(event.currentTarget)))
  }
  return <section className="content-section narrow"><div className="page-title-row"><div><span className="eyebrow">Sua conta</span><h1>Meu perfil</h1><p>Gerencie seus dados de organizador.</p></div></div>
    <div className="profile-grid"><div className="form-card"><div className="profile-avatar">{user?.nome?.slice(0, 1).toLocaleUpperCase('pt-BR')}</div>
      <Message message={error?.message} errors={error?.fields} />
      <form onSubmit={submit}><fieldset className="form-fieldset stack-form" disabled={busy}>
        <label>Nome<input name="nome" defaultValue={user?.nome || ''} required minLength={3} maxLength={100} autoComplete="name" /></label>
        <label>Usuário<input value={user?.usuario || ''} disabled readOnly /><small>O nome de usuário não pode ser alterado.</small></label>
        <label>E-mail<input type="email" name="email" defaultValue={user?.email || ''} required maxLength={150} autoComplete="email" /></label>
        <button className="btn btn-primary" type="submit">{busy ? 'Salvando…' : 'Salvar alterações'}</button>
      </fieldset></form>
    </div><aside className="stats-card"><h2>Resumo da arena</h2><div><strong>{user?.totalTorneios ?? '—'}</strong><span>Torneios criados</span></div><div><strong>{user?.finalizados ?? '—'}</strong><span>Torneios finalizados</span></div>
      <Link className="btn btn-ghost btn-block" href="/meus-torneios">Ver meus torneios</Link><button className="btn btn-danger-ghost btn-block" onClick={onLogout} disabled={logoutBusy}><Icon name="logout" />{logoutBusy ? 'Saindo…' : 'Sair da conta'}</button>
    </aside></div>
  </section>
}
