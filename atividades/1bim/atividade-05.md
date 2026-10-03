# Atividade 05: Responsabilidades e Colaboração

## Objetivo

Evoluir o **Sistema Acadêmico** para representar turmas com diferentes disciplinas ofertadas, matrículas de alunos e histórico acadêmico.

O foco da atividade é distribuir adequadamente as responsabilidades entre os objetos, mantendo o encapsulamento do modelo.

## Estrutura

Mantenha as classes de domínio no pacote:

```text
br.edu.sistemaacademico.domain
```

Não crie nesta atividade pacotes como `service`, `repository` ou `controller`.

A classe:

```text
br.edu.sistemaacademico.SistemaAcademico
```

será fornecida pronta e **não deve ser alterada**.

A implementação deverá evoluir as classes de domínio para que essa classe compile e execute integralmente.

## Evolução do Modelo

Atualmente, uma `Turma` possui uma única `Disciplina`.

Refatore o modelo para que uma turma represente um grupo acadêmico e possa possuir **várias disciplinas ofertadas em determinado período letivo**.

Exemplo:

```text
ESOFT4S-NA — 2026/2

- Programação Orientada a Objetos
- Banco de Dados
- Sistemas Operacionais
- Engenharia de Requisitos
```

Utilize uma coleção para representar as disciplinas ofertadas e impeça a inclusão duplicada da mesma disciplina.

Considere a criação da classe:

```text
OfertaDisciplina
```

para representar uma `Disciplina` sendo ofertada para uma determinada `Turma`.

## Matrículas

A matrícula de um aluno deve estar relacionada a uma **oferta de disciplina**.

O sistema deve permitir:

- matricular um aluno em uma disciplina ofertada;
- consultar as matrículas de uma oferta;
- consultar o histórico de matrículas de um aluno;
- impedir matrícula duplicada na mesma oferta;
- registrar o resultado de uma matrícula.

Considere os seguintes resultados acadêmicos:

```text
APROVADO
REPROVADO
```

Um aluno reprovado poderá cursar novamente a disciplina.

Um aluno que já tenha sido aprovado em uma disciplina **não poderá realizar uma nova matrícula na mesma disciplina**, mesmo que seja em outra turma ou período letivo.

A forma de representar e verificar essa regra faz parte da implementação.

## Responsabilidades

Distribua as regras entre os objetos do domínio de forma coerente.

Considere:

- quem conhece as disciplinas ofertadas por uma turma;
- quem mantém as matrículas de uma oferta;
- quem mantém o histórico de matrículas do aluno;
- quem possui as informações necessárias para validar uma matrícula;
- quais objetos precisam colaborar para realizar cada operação.

Evite concentrar as regras em uma única classe.

## Tratamento de Exceções

Operações inválidas devem lançar exceções adequadas, como:

```java
IllegalArgumentException
IllegalStateException
```

Considere situações como:

- disciplina duplicada em uma turma;
- matrícula duplicada na mesma oferta;
- nova matrícula em disciplina já concluída com aprovação;
- alteração inválida no estado de uma matrícula.

A classe `SistemaAcademico` fornecida realizará o tratamento dessas exceções com `try/catch`.

## Classe SistemaAcademico

A classe principal fornecida será utilizada para validar a implementação.

Ela demonstrará, entre outros cenários:

- criação de diferentes turmas e disciplinas;
- oferta de várias disciplinas para uma turma;
- tentativa de oferta duplicada;
- matrícula de alunos;
- consulta das matrículas;
- reprovação e nova matrícula em período posterior;
- aprovação;
- tentativa de nova matrícula após aprovação;
- tentativa de matrícula duplicada.

A classe `SistemaAcademico` **não deve ser modificada para adaptar-se à sua solução**.

Sua solução deve adaptar o modelo de domínio ao fluxo fornecido.

## Restrições

Nesta atividade, não utilize:

- `Service`;
- `Repository`;
- `Controller`;
- Spring Boot;
- JPA;
- banco de dados;
- API REST;
- padrões de projeto.

O foco permanece em:

- responsabilidades;
- colaboração entre objetos;
- coleções;
- encapsulamento;
- tratamento de regras do domínio.

## Entrega

Realize a atividade em uma branch própria e entregue por **Pull Request**.

Na descrição do Pull Request, responda brevemente:

1. Como as responsabilidades foram distribuídas entre os objetos?
2. Como o sistema representa as disciplinas ofertadas por uma turma?
3. Como sua implementação impede uma nova matrícula após aprovação na mesma disciplina?
4. Qual decisão de design da implementação você considera mais importante?

## Apresentação

Na aula seguinte, apresente a solução implementada.

Não é necessário explicar o código linha a linha.

A apresentação deve focar:

- na ideia utilizada para resolver o problema;
- na organização dos objetos;
- na distribuição das responsabilidades;
- em como as principais regras foram implementadas;
- nas decisões tomadas durante o desenvolvimento.

Durante a apresentação, serão feitas perguntas sobre a implementação e sobre as decisões de design adotadas.
