# Roteiro de demonstração e verificação no MySQL

1. Iniciar MySQL, backend e frontend conforme o README. Mostrar `java -version` com versão 26.
2. Mostrar as três tabelas no MySQL e as duas FKs de `emprestimo`. Comparar com as entidades e o diagrama.
3. Cadastrar dois leitores de exemplo e quatro livros, com ISBNs diferentes e um exemplar cada.
4. Editar um título no acervo e conferir a mudança após recarregar a página.
5. Emprestar o primeiro livro ao primeiro leitor e verificar que fica sem disponibilidade.
6. Tentar emprestar o mesmo livro ao segundo leitor pela API: deve responder 409. Na interface, livros sem disponibilidade não aparecem no formulário de retirada.
7. Emprestar mais dois livros ao primeiro leitor; tentar o quarto e mostrar a regra de limite (409).
8. Tentar inativar o primeiro leitor enquanto possui pendências; mostrar o erro.
9. Confirmar uma devolução na interface. Conferir que o exemplar fica disponível e o histórico recebe a situação Devolvido.
10. Repetir a devolução pela API: 409. Realizar nova retirada do exemplar devolvido.
11. Reiniciar o backend e recarregar o frontend, mostrando que o histórico continua salvo no MySQL.
12. Mostrar um formulário inválido e uma consulta a ID inexistente (400/404).
13. Rodar `mvn test` e explicar testes de regra, integração e a disputa por um exemplar.

## Mostrar o caminho de uma requisição

No navegador, abra Network e registre um empréstimo:

```text
React: POST /api/emprestimos, JSON com livroId e leitorId
  → EmprestimoController: valida entrada e devolve 201/Location
  → EmprestimoService: inicia transação, busca e bloqueia leitor/livro
  → Leitor e Livro: validam regras de negócio
  → Emprestimo: associa objetos e define prazo de 14 dias
  → EmprestimoRepository: save
  → Hibernate/JPA: INSERT com livro_id e leitor_id
  → MySQL: persiste e aplica PK, FK e CHECKs
  → DTO: resposta JSON
  → React: recarrega dados e atualiza a tela
```

## Preparar um atraso para a apresentação (opcional)

Use somente o banco local de demonstração. Escolha um empréstimo em aberto e altere suas datas pelo cliente MySQL; troque o ID de exemplo pelo ID real:

```sql
UPDATE emprestimo
SET emprestado_em = DATE_SUB(CURDATE(), INTERVAL 15 DAY),
    previsto_para = DATE_SUB(CURDATE(), INTERVAL 1 DAY)
WHERE id = 1 AND devolvido_em IS NULL;
```

Recarregue o frontend. O empréstimo deve aparecer atrasado e uma nova retirada pelo leitor deve ser bloqueada. Devolva o livro e demonstre o desbloqueio. Não existe endpoint para alterar arbitrariamente datas em uso normal.

## Perguntas que todos devem saber responder

- Por que regras não ficam no controller? Qual é a diferença entre coordenar o fluxo e aplicar uma regra do objeto?
- Por que `Emprestimo` é uma entidade e não apenas um `@ManyToMany`?
- O que cada `@ManyToOne` vira no banco? Por que os dois campos são NOT NULL?
- O que acontece no commit quando uma entidade gerenciada é alterada sem chamar `save` novamente?
- Por que os DTOs não são as próprias entidades? Por que `open-in-view` está desabilitado?
- Como os locks impedem duas retiradas do último exemplar? O que acontece se uma validação lança exceção?
- Quais tarefas JDBC o JPA abstrai? Quais responsabilidades continuam com o desenvolvedor?
- Por que testar em H2 não garante compatibilidade total com MySQL?

## Divisão sugerida entre os quatro integrantes

Esta é uma sugestão para organizar a apresentação, não um registro de quem desenvolveu cada parte. Ajustem os tempos quando o professor informar a duração. Para um ensaio de 8 a 10 minutos:

| Integrante | Parte | Tempo aproximado |
| --- | --- | --- |
| Murilo Antunes da Luz | Apresentar o objetivo do sistema e demonstrar cadastro de livro, leitor, empréstimo e devolução. | 2 a 3 minutos |
| Vitor Bahena Correa | Explicar controller, service, entidades e as regras de disponibilidade, limite e devolução. | 2 minutos |
| João Vitor de Souza Nascimento | Mostrar o modelo do banco, as chaves estrangeiras, os mapeamentos JPA e a diferença para JDBC. | 2 minutos |
| Ryan Andreski | Explicar como o React chama a API, mostrar um erro tratado e executar os testes automatizados. | 2 a 3 minutos |

Todos devem praticar a explicação do fluxo completo e saber responder às perguntas acima. A divisão da fala não substitui o entendimento das outras partes.
