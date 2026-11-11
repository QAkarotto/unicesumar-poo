const pad = (n: number) => String(n).padStart(2, '0')

export function agoraISO(): string {
  const d = new Date()
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

export function hojeISO(): string {
  return agoraISO().slice(0, 10)
}

export function formatarData(iso: string): string {
  const [ano, mes, dia] = iso.slice(0, 10).split('-')
  return `${dia}/${mes}/${ano}`
}

export function formatarDataHora(iso: string): string {
  return `${formatarData(iso)} ${iso.slice(11, 16)}`
}

export function formatarDecimal(n: number): string {
  return String(n).replace('.', ',')
}

export function formatarNota(n: number | null): string {
  return n === null ? 'sem notas' : n.toFixed(1).replace('.', ',')
}

export function media(valores: number[]): number | null {
  return valores.length ? valores.reduce((a, b) => a + b, 0) / valores.length : null
}

export function mascararNome(nome: string): string {
  const partes = nome.trim().split(/\s+/)
  const ultimo = partes.length > 1 ? ` ${partes[partes.length - 1][0]}.` : ''
  return `${partes[0][0]}***${ultimo}`
}

export function primeiroNome(nome: string): string {
  return nome.trim().split(/\s+/)[0]
}
