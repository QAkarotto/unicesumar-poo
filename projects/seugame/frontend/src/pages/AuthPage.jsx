import { useState } from 'react'
import Icon from '../components/Icon.jsx'
import Link from '../components/Link.jsx'
import PasswordField from '../components/PasswordField.jsx'
import { Message } from '../components/Feedback.jsx'

export default function AuthPage({ signup = false, onSubmit, busy, error, returnTo = '' }) {
  const [validation, setValidation] = useState('')
  function submit(event) {
    event.preventDefault()
    const values = Object.fromEntries(new FormData(event.currentTarget))
    if (signup && values.senha !== values.confirmarSenha) { setValidation('As senhas não coincidem.'); return }
    if (signup && new TextEncoder().encode(values.senha).length > 72) { setValidation('A senha é muito longa. Use uma senha mais curta.'); return }
    setValidation('')
    onSubmit?.(values)
  }
  return <section className="auth-layout"><div className="auth-copy"><span className="eyebrow">{signup ? 'Comece por aqui' : 'Acesse sua conta'}</span>
    <h1>{signup ? 'Crie seu primeiro torneio.' : 'Bom ter você de volta.'}</h1><p>{signup ? 'Cadastre os participantes e escolha o formato. O restante fica organizado para você.' : 'Entre para continuar organizando seus torneios e registrando os resultados.'}</p>
    {signup ? <ul className="feature-list"><li><Icon name="check" />Tabela atualizada após cada placar</li><li><Icon name="check" />Chaveamento com avanço automático</li><li><Icon name="check" />Controle exclusivo do organizador</li></ul> : null}
  </div><div className={`form-card${signup ? '' : ' compact'}`}><div className="form-card-heading"><span className="brand-mark">SG</span><div><h1>{signup ? 'Criar sua conta' : 'Entrar no SeuGame'}</h1><p>{signup ? 'Comece a organizar em poucos passos.' : 'Use seu usuário ou e-mail.'}</p></div></div>
    <Message message={validation || error?.message} errors={error?.fields} />
    <form onSubmit={submit}><fieldset className="form-fieldset stack-form" disabled={busy}>
      {signup ? <><label>Nome completo<input name="nome" required minLength={3} maxLength={100} autoComplete="name" /></label>
        <label>Usuário<input name="usuario" required minLength={3} maxLength={50} pattern="[a-zA-Z0-9_.\-]{3,50}" title="De 3 a 50 letras, números, ponto, hífen ou sublinhado." autoComplete="username" /></label>
        <label>E-mail<input type="email" name="email" required maxLength={150} autoComplete="email" /></label>
        <div className="form-row"><PasswordField minLength={8} maxLength={72} autoComplete="new-password" /><PasswordField label="Confirmar senha" name="confirmarSenha" minLength={8} maxLength={72} autoComplete="new-password" /></div>
        <small className="form-note">Use pelo menos 8 caracteres na senha.</small></>
        : <><label>Usuário ou e-mail<input name="identificador" required autoComplete="username" /></label><PasswordField /></>}
      <button className="btn btn-primary btn-block" type="submit">{busy ? 'Aguarde…' : signup ? 'Criar conta' : 'Entrar'}</button>
    </fieldset></form>
    <p className="form-switch">{signup ? 'Já possui conta? ' : 'Ainda não tem conta? '}<Link href={`${signup ? '/login' : '/cadastro'}${returnTo ? `?voltar=${encodeURIComponent(returnTo)}` : ''}`}>{signup ? 'Entrar' : 'Criar conta'}</Link></p>
  </div></section>
}
