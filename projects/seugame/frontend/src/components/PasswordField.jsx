import { useId, useState } from 'react'
import Icon from './Icon.jsx'

export default function PasswordField({ label = 'Senha', name = 'senha', autoComplete = 'current-password', ...props }) {
  const [visible, setVisible] = useState(false)
  const id = useId()
  return <label htmlFor={id}>{label}<span className="password-field"><input id={id} name={name} type={visible ? 'text' : 'password'} autoComplete={autoComplete} required {...props} />
    <button type="button" className="toggle-password" aria-label={`${visible ? 'Ocultar' : 'Mostrar'} ${label.toLowerCase()}`} aria-pressed={visible} onClick={() => setVisible((value) => !value)}><Icon name={visible ? 'eyeOff' : 'eye'} /></button>
  </span></label>
}
