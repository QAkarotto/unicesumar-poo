# Modelo relacional do AgroAssist

O banco usa **MySQL 8.4**, tabelas InnoDB e caracteres `utf8mb4`. O arquivo `schema.sql` define todas as tabelas e restrições. O Hibernate apenas valida a compatibilidade das entidades JPA com essa estrutura.

## Entidades e tabelas

| Conceito | Tabela | Dados principais e restrições |
| --- | --- | --- |
| Usuário | `usuarios` | ID, nome, perfil, telefone, e-mail opcional, município e data de consentimento. Perfil restrito a produtor, técnico ou administrador. |
| Cultura | `culturas` | ID e nome único da cultura. |
| Propriedade | `propriedades` | ID, produtor, nome, município e área `DECIMAL(12,2)` maior que zero. |
| Técnico | `tecnicos` | ID, usuário único, formação, registro profissional único e indicador de ativo. |
| Solicitação | `solicitacoes` | ID, protocolo único, produtor, propriedade, cultura, técnico opcional, estádio, descrição, status e abertura. |
| Atendimento | `atendimentos` | ID, solicitação única, técnico, data, modalidade, diagnóstico e recomendação. |
| Avaliação | `avaliacoes` | ID, solicitação única, nota de 1 a 5, comentário opcional e horário. |

Cada ID principal é uma string de até 40 caracteres. Os exemplos mantêm IDs como `u1`, `p1` e `s5`; os cadastros novos usam um prefixo da entidade seguido de UUID. O protocolo visível usa uma sequência numérica, no formato `AGA-AAAA-0143`.

## Relacionamentos e cardinalidades

| Origem | Relação | Destino | Mapeamento e implementação SQL |
| --- | --- | --- | --- |
| Usuário produtor | 1:N | Propriedade | `Propriedade.produtor`: `@ManyToOne`; FK `propriedades.produtor_id`. |
| Usuário | 1:0..1 | Técnico | `Tecnico.usuario`: `@OneToOne`; FK única `tecnicos.usuario_id`. |
| Propriedade | N:N | Cultura | `@ManyToMany`; tabela `propriedade_culturas`, com PK composta. |
| Técnico | N:N | Cultura | `@ManyToMany`; tabela `tecnico_culturas`, com PK composta. |
| Usuário produtor | 1:N | Solicitação | `@ManyToOne`; FK `solicitacoes.produtor_id`. |
| Propriedade | 1:N | Solicitação | `@ManyToOne`; FK `solicitacoes.propriedade_id`. |
| Cultura | 1:N | Solicitação | `@ManyToOne`; FK `solicitacoes.cultura_id`. |
| Técnico | 1:N | Solicitação | `@ManyToOne`; FK opcional até a atribuição. |
| Solicitação | 1:0..1 | Atendimento | `@OneToOne`; FK única `atendimentos.solicitacao_id`. |
| Técnico | 1:N | Atendimento | `@ManyToOne`; FK `atendimentos.tecnico_id`. |
| Solicitação | 1:0..1 | Avaliação | `@OneToOne`; FK única `avaliacoes.solicitacao_id`. |
| Solicitação | 1:N | Evento do histórico | `@ElementCollection` de `EventoHistorico`; tabela `historico_solicitacao`. |
| Usuário | 1:N | Evento do histórico | `EventoHistorico.autor`: `@ManyToOne`; FK `historico_solicitacao.autor_id`. |
| Solicitação | 1:0..3 | Foto | `@ElementCollection` ordenada; tabela `solicitacao_fotos`. |

Os relacionamentos Java são unidirecionais quando a navegação inversa não é necessária. Por exemplo, `Propriedade` referencia o produtor, sem exigir uma coleção de propriedades dentro de `Usuario`.

## Tabelas auxiliares

- `propriedade_culturas`: associa cada propriedade às suas culturas, sem pares repetidos.
- `tecnico_culturas`: associa o técnico às culturas em que é habilitado, sem pares repetidos.
- `solicitacao_fotos`: usa PK `(solicitacao_id, ordem)`, ordem de 0 a 2 e conteúdo `LONGTEXT`.
- `historico_solicitacao`: usa PK `(solicitacao_id, ordem)` e registra horário, status, autor e descrição. A API oferece apenas consulta; as mudanças de estado acrescentam eventos.
- `sequencia_protocolo`: uma linha com ID 1 e o último número utilizado. A transação bloqueia essa linha ao gerar um protocolo para evitar números repetidos.

São **12 tabelas**: sete principais, duas associativas, duas coleções e uma sequência.

## Estados da solicitação

| Estado atual | Ação permitida | Próximo estado |
| --- | --- | --- |
| Aberta | Coordenação atribui técnico ativo e habilitado | Atribuída |
| Atribuída | Coordenação troca o técnico antes do início | Atribuída, com novo evento |
| Atribuída | Técnico responsável inicia | Em atendimento |
| Em atendimento | Técnico responsável registra diagnóstico e recomendação | Concluída |
| Concluída | Produtor autor avalia uma vez | Avaliada |

O banco armazena os enums em maiúsculas, como `EM_ATENDIMENTO`. Os DTOs traduzem esses valores para os rótulos já usados pelo React, como `Em atendimento`.

## Restrições e regras

Chaves estrangeiras impedem vínculos com registros inexistentes. `NOT NULL` protege os campos obrigatórios; `UNIQUE` protege protocolos, registros profissionais, usuário do técnico e a unicidade de atendimento/avaliação por solicitação. `CHECK` valida área, nota, ordem das fotos, perfis, modalidade e estados possíveis.

As regras que dependem de vários objetos são verificadas no domínio: propriedade do produtor, cultura plantada, habilitação e situação do técnico, responsável pelo atendimento e permissão para avaliar. Os serviços usam transações e bloqueiam a solicitação durante as mudanças para evitar duas conclusões ou avaliações simultâneas. As restrições SQL complementam essas verificações.

A solicitação aberta não possui técnico; nos demais estados ele é obrigatório. E-mail, estádio e comentário opcionais são armazenados como string vazia. A área usa decimal, evitando os erros de representação que ocorreriam com ponto flutuante no banco.

## Dados de demonstração

`seed.sql` contém culturas, produtores, técnicos, propriedades e solicitações em diferentes etapas. O backend o carrega quando não há usuários e também após a restauração confirmada. Essa inicialização não substitui o script da estrutura.

O SQL do seed só atende à demonstração; cadastro, atribuição, atendimento, avaliação e consultas utilizam os repositórios JPA. Para compreender a persistência, acompanhe uma solicitação desde o formulário React até `SolicitacaoService`, os métodos de `Solicitacao` e as tabelas relacionadas.

