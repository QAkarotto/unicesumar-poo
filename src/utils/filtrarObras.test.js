import { describe, expect, it } from 'vitest'
import { filtrarObras } from './filtrarObras'

const obras = [
  {
    id: 'aurora', title: 'Aurora Azul', author: 'Ana Lima', studio: 'Estúdio Norte',
    types: ['anime', 'mangá'], genres: ['Fantasia', 'Aventura'], tropes: ['found family'],
    status: 'Em andamento', rating: 9, popularity: 80, year: 2024,
    platforms: { streaming: [{ name: 'Tela+' }], reading: [] },
  },
  {
    id: 'jardim', title: 'Jardim de Inverno', author: 'Bia Reis', studio: 'Estúdio Sul',
    types: ['manhwa'], genres: ['Romance'], tropes: ['segunda chance'],
    status: 'Concluído', rating: 7, popularity: 95, year: 2022,
    platforms: { streaming: [], reading: [{ name: 'Ler+' }] },
  },
  {
    id: 'cidade', title: 'Cidade Lunar', author: 'Caio Luz', studio: 'Estúdio Norte',
    types: ['manhua'], genres: ['Fantasia'], tropes: ['found family'],
    status: 'Em andamento', rating: 8, popularity: 70, year: 2025,
    platforms: { streaming: [{ name: 'Tela+' }], reading: [{ name: 'Ler+' }] },
  },
]

describe('filtrarObras', () => {
  it('filtra por formato', () => expect(filtrarObras(obras, { formato: 'manhwa' }).map(({ id }) => id)).toEqual(['jardim']))
  it('filtra por gênero', () => expect(filtrarObras(obras, { genero: 'Romance' }).map(({ id }) => id)).toEqual(['jardim']))
  it('filtra por trope', () => expect(filtrarObras(obras, { trope: 'segunda chance' }).map(({ id }) => id)).toEqual(['jardim']))
  it('filtra por status', () => expect(filtrarObras(obras, { status: 'Concluído' }).map(({ id }) => id)).toEqual(['jardim']))
  it('filtra por estúdio', () => expect(filtrarObras(obras, { estudio: 'Estúdio Sul' }).map(({ id }) => id)).toEqual(['jardim']))
  it('filtra por disponibilidade', () => expect(filtrarObras(obras, { onde: 'Ler+' }).map(({ id }) => id)).toEqual(['cidade', 'jardim']))
  it('filtra por nota mínima', () => expect(filtrarObras(obras, { nota: 8 }).map(({ id }) => id)).toEqual(['aurora', 'cidade']))
  it('busca texto sem diferenciar caixa', () => expect(filtrarObras(obras, { q: 'AURORA' }).map(({ id }) => id)).toEqual(['aurora']))
  it('combina grupos com E e valores do mesmo grupo com OU', () => {
    expect(filtrarObras(obras, { formato: ['anime', 'manhwa'], genero: ['Romance', 'Fantasia'], onde: ['Tela+', 'Ler+'] }).map(({ id }) => id)).toEqual(['aurora', 'jardim'])
  })
  it('retorna lista vazia sem alterar o catálogo', () => {
    const snapshot = [...obras]
    expect(filtrarObras(obras, { trope: 'inexistente' })).toEqual([])
    expect(obras).toEqual(snapshot)
  })
})
