import { carregarBanco, obterBanco } from './store'
import { requisicao } from './http'
import { ADMIN_ID } from '../data/constantes'
import type { Modalidade, Solicitacao, Usuario } from '../domain/tipos'

export interface DadosConta {
  nome: string
  telefone: string
  municipio: string
  email: string
  consentimento: boolean
}

export interface DadosPropriedade {
  nome: string
  municipio: string
  areaHa: number
  culturaIds: string[]
}

export interface DadosSolicitacao {
  propriedadeId: string
  culturaId: string
  estadio: string
  descricao: string
  fotos: string[]
}

export interface DadosAtendimento {
  data: string
  modalidade: Modalidade
  diagnostico: string
  recomendacao: string
}

export interface DadosTecnico {
  nome: string
  telefone: string
  municipio: string
  formacao: string
  registro: string
  culturaIds: string[]
}

async function salvar<T>(caminho: string, metodo: string, dados?: unknown, usuarioId?: string): Promise<T> {
  const resultado = await requisicao<T>(caminho, metodo, dados, usuarioId)
  try {
    await carregarBanco(true)
  } catch {
    throw new Error(
      'A operação foi salva, mas não foi possível atualizar a tela. Tente carregar os dados novamente.',
    )
  }
  return resultado
}

export const api = {
  async criarConta(dados: DadosConta): Promise<string> {
    const usuario = await salvar<Usuario>('/produtores', 'POST', dados)
    return usuario.id
  },

  async cadastrarPropriedade(produtorId: string, dados: DadosPropriedade): Promise<void> {
    await salvar('/propriedades', 'POST', dados, produtorId)
  },

  async abrirSolicitacao(produtorId: string, dados: DadosSolicitacao): Promise<Solicitacao> {
    return salvar<Solicitacao>('/solicitacoes', 'POST', dados, produtorId)
  },

  async atribuirTecnico(adminId: string, solicitacaoId: string, tecnicoId: string): Promise<string> {
    await salvar(
      `/solicitacoes/${encodeURIComponent(solicitacaoId)}/atribuicao`,
      'PATCH',
      { tecnicoId },
      adminId,
    )
    const banco = obterBanco()
    const tecnico = banco.tecnicos.find((t) => t.id === tecnicoId)
    return banco.usuarios.find((u) => u.id === tecnico?.usuarioId)?.nome ?? 'Técnico'
  },

  async iniciarAtendimento(usuarioId: string, solicitacaoId: string): Promise<void> {
    await salvar(`/solicitacoes/${encodeURIComponent(solicitacaoId)}/inicio`, 'PATCH', undefined, usuarioId)
  },

  async registrarAtendimento(
    usuarioId: string,
    solicitacaoId: string,
    dados: DadosAtendimento,
  ): Promise<void> {
    await salvar(`/solicitacoes/${encodeURIComponent(solicitacaoId)}/atendimento`, 'POST', dados, usuarioId)
  },

  async avaliar(usuarioId: string, solicitacaoId: string, nota: number, comentario: string): Promise<void> {
    await salvar(
      `/solicitacoes/${encodeURIComponent(solicitacaoId)}/avaliacao`,
      'POST',
      { nota, comentario },
      usuarioId,
    )
  },

  async cadastrarTecnico(dados: DadosTecnico): Promise<void> {
    await salvar('/tecnicos', 'POST', dados, ADMIN_ID)
  },

  async alterarSituacaoTecnico(tecnicoId: string, ativo: boolean): Promise<void> {
    await salvar(`/tecnicos/${encodeURIComponent(tecnicoId)}/situacao`, 'PATCH', { ativo }, ADMIN_ID)
  },
}
