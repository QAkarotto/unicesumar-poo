import type { Banco } from '../../domain/tipos'

export type ChaveEntidade =
  | 'usuarios'
  | 'propriedades'
  | 'culturas'
  | 'solicitacoes'
  | 'tecnicos'
  | 'atendimentos'
  | 'avaliacoes'

export interface Entidade {
  chave: ChaveEntidade
  nome: string
  atributos: Array<[nome: string, detalhe: string]>
}

export const ENTIDADES: Entidade[] = [
  {
    chave: 'usuarios',
    nome: 'Usuário',
    atributos: [
      ['id', 'PK'],
      ['nome', ''],
      ['perfil', 'produtor | técnico | admin'],
      ['telefone', ''],
      ['email', 'opcional'],
      ['municipio', ''],
      ['consentimento_em', 'data e hora'],
    ],
  },
  {
    chave: 'propriedades',
    nome: 'Propriedade',
    atributos: [
      ['id', 'PK'],
      ['produtor_id', 'FK Usuário'],
      ['nome', ''],
      ['municipio', ''],
      ['area_ha', 'decimal'],
      ['culturas', 'N:N Cultura'],
    ],
  },
  {
    chave: 'culturas',
    nome: 'Cultura',
    atributos: [
      ['id', 'PK'],
      ['nome', 'Soja, Milho, Trigo...'],
    ],
  },
  {
    chave: 'solicitacoes',
    nome: 'Solicitação',
    atributos: [
      ['id', 'PK'],
      ['protocolo', 'único'],
      ['produtor_id', 'FK Usuário'],
      ['propriedade_id', 'FK Propriedade'],
      ['cultura_id', 'FK Cultura'],
      ['tecnico_id', 'FK Técnico, nulo até atribuir'],
      ['estadio', 'opcional'],
      ['descricao', ''],
      ['fotos', 'até 3'],
      ['status', '5 etapas'],
      ['historico', 'só inclusão'],
    ],
  },
  {
    chave: 'tecnicos',
    nome: 'Técnico',
    atributos: [
      ['id', 'PK'],
      ['usuario_id', 'FK Usuário'],
      ['formacao', ''],
      ['registro', 'CREA ou CRT'],
      ['ativo', 'sim | não'],
      ['culturas', 'N:N Cultura'],
    ],
  },
  {
    chave: 'atendimentos',
    nome: 'Atendimento',
    atributos: [
      ['id', 'PK'],
      ['solicitacao_id', 'FK Solicitação, 1:1'],
      ['tecnico_id', 'FK Técnico'],
      ['data', ''],
      ['modalidade', 'visita | remota'],
      ['diagnostico', ''],
      ['recomendacao', ''],
    ],
  },
  {
    chave: 'avaliacoes',
    nome: 'Avaliação',
    atributos: [
      ['id', 'PK'],
      ['solicitacao_id', 'FK Solicitação, 1:1'],
      ['nota', '1 a 5'],
      ['comentario', 'opcional'],
      ['em', 'data e hora'],
    ],
  },
]

export const RELACIONAMENTOS: Array<[cardinalidade: string, descricao: string]> = [
  ['1 : N', 'Usuário (produtor) possui Propriedades'],
  ['1 : 0..1', 'Usuário é Técnico (dados profissionais à parte)'],
  ['N : N', 'Propriedade cultiva Culturas'],
  ['N : N', 'Técnico é habilitado em Culturas'],
  ['1 : N', 'Usuário (produtor) abre Solicitações'],
  ['1 : N', 'Propriedade e Cultura classificam Solicitações'],
  ['1 : N', 'Técnico é responsável por Solicitações'],
  ['1 : 0..1', 'Solicitação gera um Atendimento'],
  ['1 : 0..1', 'Solicitação recebe uma Avaliação'],
]

export const REQUISITOS: Array<{ id: string; texto: string; onde: string; mvp: boolean }> = [
  {
    id: 'RF01',
    texto: 'Cadastrar usuários (produtor pelo app, técnico pela coordenação) com consentimento',
    onde: 'Produtor › entrada · Administrador',
    mvp: true,
  },
  {
    id: 'RF02',
    texto: 'Cadastrar propriedades rurais com município, área e culturas',
    onde: 'Produtor › Propriedades',
    mvp: true,
  },
  {
    id: 'RF03',
    texto: 'Abrir solicitação informando cultura e problema, com fotos',
    onde: 'Produtor › Nova',
    mvp: true,
  },
  {
    id: 'RF04',
    texto: 'Acompanhar o status e o histórico da solicitação',
    onde: 'Produtor · Técnico · Administrador',
    mvp: true,
  },
  { id: 'RF05', texto: 'Atribuir o técnico responsável', onde: 'Administrador', mvp: false },
  { id: 'RF06', texto: 'Registrar o atendimento (diagnóstico e recomendação)', onde: 'Técnico', mvp: true },
  { id: 'RF07', texto: 'Avaliar o serviço concluído', onde: 'Produtor › detalhe', mvp: true },
]

export const REGRAS: Array<{ id: string; texto: string; onde: string }> = [
  {
    id: 'RN01',
    texto:
      'A solicitação exige propriedade cadastrada, e a cultura precisa estar entre as culturas dessa propriedade.',
    onde: 'Produtor › Nova',
  },
  {
    id: 'RN02',
    texto:
      'Só técnicos com registro ativo e habilitados na cultura podem ser atribuídos. Depois que o atendimento começa, não há reatribuição.',
    onde: 'Administrador',
  },
  {
    id: 'RN03',
    texto: 'Só o técnico responsável pode iniciar, registrar o atendimento e mudar o status.',
    onde: 'Técnico',
  },
  {
    id: 'RN04',
    texto:
      'A avaliação só é liberada com a solicitação concluída, uma única vez e apenas para o produtor que abriu.',
    onde: 'Produtor › detalhe',
  },
  {
    id: 'RN05',
    texto: 'O histórico de status não pode ser editado nem apagado. Cada mudança guarda data, hora e autor.',
    onde: 'Telas de detalhe',
  },
  {
    id: 'RN06',
    texto: 'Nome completo, propriedade e telefone do produtor ficam visíveis só para o técnico responsável.',
    onde: 'Técnico',
  },
  {
    id: 'RN07',
    texto: 'O cadastro só é concluído com o consentimento para uso dos dados (LGPD).',
    onde: 'Produtor › entrada',
  },
]

export const QUALIDADE: Array<[titulo: string, texto: string]> = [
  [
    'Usabilidade',
    'Abrir uma solicitação cabe em uma tela, com linguagem do produtor e botões grandes no celular.',
  ],
  [
    'Acessibilidade',
    'Todo campo tem rótulo, o contraste funciona nos temas claro e escuro, tudo pode ser usado pelo teclado e o app tem o botão A+ para texto maior.',
  ],
  [
    'Segurança',
    'Cada perfil só enxerga as ações que lhe cabem. As regras RN02, RN03 e RN04 são verificadas pelo backend. A troca de perfis é demonstrativa, sem autenticação real.',
  ],
  [
    'Privacidade (LGPD)',
    'Consentimento no cadastro, coleta mínima sem CPF e contato do produtor oculto para quem não atende.',
  ],
  ['Confiabilidade e rastreabilidade', 'Histórico só de inclusão, com data, hora e autor de cada mudança.'],
  [
    'Manutenibilidade',
    'Regras de negócio no domínio Java, operações coordenadas pelos serviços e persistência com JPA/MySQL. O React acessa os dados pela API REST.',
  ],
]

export const EVOLUCOES = [
  'Aviso por WhatsApp ou SMS quando o status mudar',
  'Modo offline com envio quando o sinal voltar, pensando na conectividade no campo',
  'Mapa das propriedades atendidas',
  'Relatórios por região e por cultura',
  'Atribuição automática por cultura e distância',
]

export function tabelaDaEntidade(b: Banco, chave: ChaveEntidade): { colunas: string[]; linhas: string[][] } {
  const nomeUsuario = (id: string) => b.usuarios.find((u) => u.id === id)?.nome ?? ''
  const nomeCultura = (id: string) => b.culturas.find((c) => c.id === id)?.nome ?? ''
  const protocolo = (id: string) => b.solicitacoes.find((s) => s.id === id)?.protocolo ?? ''
  switch (chave) {
    case 'usuarios':
      return {
        colunas: ['id', 'nome', 'perfil', 'município', 'consentimento'],
        linhas: b.usuarios.map((u) => [
          u.id,
          u.nome,
          u.perfil,
          u.municipio,
          u.consentimentoEm.replace('T', ' ').slice(0, 16),
        ]),
      }
    case 'propriedades':
      return {
        colunas: ['id', 'produtor_id', 'nome', 'município', 'área (ha)', 'culturas'],
        linhas: b.propriedades.map((p) => [
          p.id,
          `${p.produtorId} · ${nomeUsuario(p.produtorId)}`,
          p.nome,
          p.municipio,
          String(p.areaHa).replace('.', ','),
          p.culturaIds.map(nomeCultura).join(', '),
        ]),
      }
    case 'culturas':
      return { colunas: ['id', 'nome'], linhas: b.culturas.map((c) => [c.id, c.nome]) }
    case 'solicitacoes':
      return {
        colunas: [
          'id',
          'protocolo',
          'produtor_id',
          'cultura_id',
          'tecnico_id',
          'status',
          'eventos no histórico',
        ],
        linhas: [...b.solicitacoes]
          .sort((x, y) => y.criadaEm.localeCompare(x.criadaEm))
          .map((s) => [
            s.id,
            s.protocolo,
            s.produtorId,
            `${s.culturaId} · ${nomeCultura(s.culturaId)}`,
            s.tecnicoId ?? 'nulo',
            s.status,
            String(s.historico.length),
          ]),
      }
    case 'tecnicos':
      return {
        colunas: ['id', 'usuario_id', 'registro', 'ativo', 'culturas'],
        linhas: b.tecnicos.map((t) => [
          t.id,
          `${t.usuarioId} · ${nomeUsuario(t.usuarioId)}`,
          t.registro,
          t.ativo ? 'sim' : 'não',
          t.culturaIds.map(nomeCultura).join(', '),
        ]),
      }
    case 'atendimentos':
      return {
        colunas: ['id', 'solicitacao_id', 'tecnico_id', 'data', 'modalidade'],
        linhas: b.atendimentos.map((a) => [
          a.id,
          `${a.solicitacaoId} · ${protocolo(a.solicitacaoId)}`,
          a.tecnicoId,
          a.data.split('-').reverse().join('/'),
          a.modalidade,
        ]),
      }
    case 'avaliacoes':
      return {
        colunas: ['id', 'solicitacao_id', 'nota', 'comentário'],
        linhas: b.avaliacoes.map((a) => [
          a.id,
          `${a.solicitacaoId} · ${protocolo(a.solicitacaoId)}`,
          String(a.nota),
          a.comentario,
        ]),
      }
  }
}
