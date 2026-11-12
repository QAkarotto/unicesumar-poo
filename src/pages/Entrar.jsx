import { useState } from 'react'
import { Link, Navigate, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import './Entrar.css'

function Entrar() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [nome, setNome] = useState('')
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { enter, user } = useAuth()
  const navigate = useNavigate()
  const isRegistering = searchParams.get('modo') === 'cadastro'

  function changeMode(registering) {
    const next = new URLSearchParams(searchParams)
    if (registering) next.set('modo', 'cadastro')
    else next.delete('modo')
    setSearchParams(next)
    setError('')
    setShowPassword(false)
  }

  function safeReturnPath() {
    const requested = searchParams.get('volta') || '/'
    return requested.startsWith('/') && !requested.startsWith('//') && !requested.includes('\\')
      ? requested
      : '/'
  }

  if (user) return <Navigate to={safeReturnPath()} replace />

  async function submit(event) {
    event.preventDefault()
    setError('')
    if (isRegistering && nome.trim().length < 2) {
      setError('Digite seu nome para criar a conta.')
      return
    }
    if (senha.length < 6) {
      setError('A senha precisa ter pelo menos 6 caracteres.')
      return
    }

    setSubmitting(true)
    try {
      await enter({ nome, email, senha }, isRegistering)
      navigate(safeReturnPath(), { replace: true })
    } catch (cause) {
      setError(isRegistering ? cause instanceof Error ? cause.message : 'Não foi possível criar a conta.' : 'Esses dados não bateram. Confere seu e-mail e sua senha?')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="account-page">
      <div className="account-main">
        <header className="account-heading">
          <p className="account-kicker">Sua conta</p>
          <h1>{isRegistering ? 'Criar conta' : 'Entrar'}</h1>
          <p>{isRegistering ? 'Crie sua conta para guardar obras e acompanhar sua lista.' : 'Entre para guardar suas obras, organizar sua lista e deixar suas avaliações.'}</p>
        </header>

        <div className="account-layout">
          <form className="account-form" onSubmit={submit}>
            <div className="account-mode" role="group" aria-label="Entrar ou criar conta">
              <button type="button" aria-pressed={!isRegistering} onClick={() => changeMode(false)}>Entrar</button>
              <button type="button" aria-pressed={isRegistering} onClick={() => changeMode(true)}>Criar conta</button>
            </div>
            {isRegistering ? (
              <label className="account-field">
                <span>Nome</span>
                <input autoComplete="name" value={nome} onChange={(event) => setNome(event.target.value)} required minLength={2} />
              </label>
            ) : null}
            <label className="account-field">
              <span>E-mail</span>
              <input type="email" autoComplete="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
            </label>
            <div className="account-field">
              <label htmlFor="account-password">Senha</label>
              <input id="account-password" type={showPassword ? 'text' : 'password'} autoComplete={isRegistering ? 'new-password' : 'current-password'} value={senha} onChange={(event) => setSenha(event.target.value)} required minLength={6} />
              <button className="account-password-toggle" type="button" aria-pressed={showPassword} onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? 'Ocultar senha' : 'Mostrar senha'}
              </button>
            </div>
            {error ? <p className="account-error" role="alert">{error}</p> : null}
            <button className="account-submit" type="submit" disabled={submitting}>
              {submitting ? 'Aguarde…' : isRegistering ? 'Criar conta' : 'Entrar'}
            </button>
          </form>

        </div>
        <Link className="account-back" to="/">Voltar ao início</Link>
      </div>
    </div>
  )
}

export default Entrar
