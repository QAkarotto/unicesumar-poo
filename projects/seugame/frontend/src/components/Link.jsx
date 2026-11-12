import { navigate } from '../lib/router.js'

export default function Link({ href, children, onClick, ...props }) {
  return <a href={href} {...props} onClick={(event) => {
    onClick?.(event)
    if (event.defaultPrevented || event.button !== 0 || event.metaKey || event.ctrlKey || event.shiftKey || event.altKey
      || props.target || href.startsWith('#')) return
    event.preventDefault()
    navigate(href)
  }}>{children}</a>
}
