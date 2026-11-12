import Icon from './Icon.jsx'
import Link from './Link.jsx'

export function Message({ message, type = 'erro', errors, onClose }) {
  if (!message) return null
  return <div className={`alert alert-${type} alert-inline`} role={type === 'erro' ? 'alert' : 'status'}>
    <Icon name={type === 'sucesso' ? 'check' : 'info'} />
    <div className="feedback-copy"><span>{message}</span>{errors?.length > 0 ? <ul>{errors.map((error) => <li key={error}>{error}</li>)}</ul> : null}</div>
    {onClose ? <button type="button" className="alert-close" aria-label="Fechar mensagem" onClick={onClose}>×</button> : null}
  </div>
}
export function Loading({ message = 'Carregando…' }) {
  return <div className="loading-state" role="status"><Icon name="refresh" />{message}</div>
}
export function Empty({ title, children, action }) {
  return <div className="empty-state"><Icon name="trophy" /><h2>{title}</h2><p>{children}</p>{action}</div>
}
export function PageError({ error, retry }) {
  return <section className="content-section"><Empty title={error?.status === 404 ? 'Página não encontrada' : 'Não foi possível carregar'}
    action={<div className="hero-actions">{retry && error?.status !== 404 ? <button className="btn btn-primary" onClick={retry}>Tentar novamente</button> : null}<Link className="btn btn-ghost" href="/">Voltar ao início</Link></div>}>
    {error?.message || 'O endereço pode estar incorreto ou o recurso foi removido.'}
  </Empty></section>
}
