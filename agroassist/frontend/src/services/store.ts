import { useSyncExternalStore } from 'react'
import type { Banco } from '../domain/tipos'
import { ADMIN_ID } from '../data/constantes'
import { requisicao } from './http'

interface EstadoBanco {
  banco: Banco | null
  carregando: boolean
  erro: string | null
}

const ouvintes = new Set<() => void>()
let estado: EstadoBanco = { banco: null, carregando: true, erro: null }
let carregamento: Promise<Banco> | null = null

function publicar(alteracao: Partial<EstadoBanco>): void {
  estado = { ...estado, ...alteracao }
  ouvintes.forEach((ouvinte) => ouvinte())
}

function assinar(ouvinte: () => void): () => void {
  ouvintes.add(ouvinte)
  return () => {
    ouvintes.delete(ouvinte)
  }
}

export function useEstadoBanco(): EstadoBanco {
  return useSyncExternalStore(assinar, () => estado)
}

export function obterBanco(): Banco {
  if (!estado.banco) throw new Error('Aguarde o carregamento dos dados.')
  return estado.banco
}

export function useBanco(): Banco {
  const { banco } = useEstadoBanco()
  if (!banco) throw new Error('Aguarde o carregamento dos dados.')
  return banco
}

// O estado no React é só uma cópia para exibição. A persistência fica no MySQL.
export function carregarBanco(aposAlteracao = false): Promise<Banco> {
  if (carregamento) {
    // Após salvar, precisamos de uma leitura iniciada depois da alteração.
    if (aposAlteracao) return carregamento.catch(() => undefined).then(() => carregarBanco())
    return carregamento
  }
  publicar({ carregando: true, erro: null })
  carregamento = requisicao<Banco>('/dados')
    .then((banco) => {
      publicar({ banco, carregando: false })
      return banco
    })
    .catch((erro: unknown) => {
      publicar({
        carregando: false,
        erro: erro instanceof Error ? erro.message : 'Não foi possível carregar os dados.',
      })
      throw erro
    })
    .finally(() => {
      carregamento = null
    })
  return carregamento
}

export async function reiniciarBanco(): Promise<void> {
  await requisicao<void>('/demo/reiniciar', 'POST', undefined, ADMIN_ID)
}
