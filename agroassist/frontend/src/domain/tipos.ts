export type Perfil = 'produtor' | 'tecnico' | 'admin'

export type Status = 'Aberta' | 'Atribuída' | 'Em atendimento' | 'Concluída' | 'Avaliada'

export const ETAPAS: Status[] = ['Aberta', 'Atribuída', 'Em atendimento', 'Concluída', 'Avaliada']

export type Modalidade = 'Visita à propriedade' | 'Orientação remota'

export const MODALIDADES: Modalidade[] = ['Visita à propriedade', 'Orientação remota']

export interface Usuario {
  id: string
  nome: string
  perfil: Perfil
  telefone: string
  email: string
  municipio: string
  consentimentoEm: string
}

export interface Cultura {
  id: string
  nome: string
}

export interface Propriedade {
  id: string
  produtorId: string
  nome: string
  municipio: string
  areaHa: number
  culturaIds: string[]
}

export interface Tecnico {
  id: string
  usuarioId: string
  formacao: string
  registro: string
  culturaIds: string[]
  ativo: boolean
}

export interface EventoHistorico {
  em: string
  status: Status
  autorId: string
  descricao: string
}

export interface Solicitacao {
  id: string
  protocolo: string
  produtorId: string
  propriedadeId: string
  culturaId: string
  tecnicoId: string | null
  estadio: string
  descricao: string
  fotos: string[]
  status: Status
  criadaEm: string
  historico: EventoHistorico[]
}

export interface Atendimento {
  id: string
  solicitacaoId: string
  tecnicoId: string
  data: string
  modalidade: Modalidade
  diagnostico: string
  recomendacao: string
}

export interface Avaliacao {
  id: string
  solicitacaoId: string
  nota: number
  comentario: string
  em: string
}

export interface Banco {
  usuarios: Usuario[]
  culturas: Cultura[]
  propriedades: Propriedade[]
  tecnicos: Tecnico[]
  solicitacoes: Solicitacao[]
  atendimentos: Atendimento[]
  avaliacoes: Avaliacao[]
}
