export type ChaveTela = 'entrada' | 'lista' | 'nova' | 'detalhe' | 'propriedades' | 'perfil'

interface Nota {
  titulo: string
  descricao: string
  rf: string[]
  rn: string[]
  experimente: string[]
}

export const NOTAS: Record<ChaveTela, Nota> = {
  entrada: {
    titulo: 'Cadastro de usuário',
    descricao:
      'O produtor cria a conta pelo celular. O cadastro só é concluído com o consentimento para uso dos dados, e o sistema não pede CPF nem documentos.',
    rf: ['RF01'],
    rn: ['RN07'],
    experimente: [
      'Crie uma conta nova e veja o registro aparecer em Modelo e regras, na entidade Usuário.',
      'Ou entre como Alexandre ou João para usar contas já prontas.',
    ],
  },
  lista: {
    titulo: 'Acompanhamento das solicitações',
    descricao:
      'O produtor vê todas as suas demandas com o status atual. Quando um atendimento é concluído, aparece o aviso para avaliar.',
    rf: ['RF04'],
    rn: [],
    experimente: [
      'Abra a AGA-2026-0136: o atendimento já foi registrado e está esperando a avaliação de Alexandre.',
      'Use o botão A+ no topo do app para aumentar o texto.',
    ],
  },
  nova: {
    titulo: 'Abertura da solicitação',
    descricao:
      'O produtor escolhe a propriedade, a cultura e descreve o problema, podendo anexar até 3 fotos. A lista de culturas muda conforme a propriedade escolhida.',
    rf: ['RF03'],
    rn: ['RN01'],
    experimente: [
      'Troque a propriedade e veja as culturas disponíveis mudarem.',
      'Sem foto de lavoura em mãos? Use o botão Foto de exemplo.',
    ],
  },
  detalhe: {
    titulo: 'Status, histórico e avaliação',
    descricao:
      'A barra mostra em que etapa a demanda está. O histórico registra cada mudança com data e autor e não pode ser editado. A avaliação só é liberada depois que o técnico conclui o atendimento.',
    rf: ['RF04', 'RF07'],
    rn: ['RN04', 'RN05'],
    experimente: ['Se o status for Concluída, envie a avaliação e veja a solicitação passar para Avaliada.'],
  },
  propriedades: {
    titulo: 'Cadastro de propriedades',
    descricao:
      'Cada propriedade guarda município, área em hectares e as culturas plantadas. Uma conta pode ter várias propriedades.',
    rf: ['RF02'],
    rn: ['RN01'],
    experimente: ['Cadastre uma propriedade com Milho e depois abra uma solicitação para ela.'],
  },
  perfil: {
    titulo: 'Dados pessoais e LGPD',
    descricao:
      'O produtor consulta os dados que o sistema guarda e quando deu o consentimento. A coleta é mínima: nome, telefone, município e e-mail opcional.',
    rf: ['RF01'],
    rn: ['RN06', 'RN07'],
    experimente: ['Saia para trocar de usuário ou criar uma conta nova.'],
  },
}

export function chaveDaTela(caminho: string): ChaveTela {
  if (caminho.startsWith('/produtor/entrar')) return 'entrada'
  if (caminho.startsWith('/produtor/nova')) return 'nova'
  if (caminho.startsWith('/produtor/solicitacoes/')) return 'detalhe'
  if (caminho.startsWith('/produtor/propriedades')) return 'propriedades'
  if (caminho.startsWith('/produtor/perfil')) return 'perfil'
  return 'lista'
}
