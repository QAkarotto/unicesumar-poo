# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. A partir da Atividade 03, alunos e disciplinas ficam no SQLite; matrículas continuam em memória.

| Método e rota | Exemplo / resultado |
| --- | --- |
| `GET /api/alunos/{registroAcademico}` | `RA2026001` ou `RA2026002` — 200 |
| `GET /api/matriculas/{codigo}` | `MAT-001` — 200 |
| `POST /api/matriculas` | Cria matrícula — 201 e cabeçalho `Location` |

Exemplo de corpo para criar uma matrícula:

```json
{"registroAcademico":"RA2026002","ofertaId":"POO-2026-2"}
```

Ofertas de exemplo: `POO-2026-2` e `BD-2026-2`, ambas da turma `ESOFT4S-NA`, período 2026/2. `ofertaId` é a chave em memória da oferta; o código da matrícula é gerado pela aplicação. `MAT-001` já vincula `RA2026001` à oferta de POO.

Recurso inexistente retorna 404; JSON/campos inválidos ou matrícula duplicada na oferta retornam 400; impedimento por estado do domínio (como aprovação anterior) retorna 409. Erros retornam JSON com `mensagem`. As regras de matrícula permanecem nas classes do domínio.

No Postman, use **Import** e selecione `postman/sistema-academico.postman_collection.json`. A variável `baseUrl` aponta para `http://localhost:8080`. Execute a Collection na ordem, com a aplicação recém-iniciada; reinicie para repetir o cenário de criação. A Collection verifica status e campos das respostas, incluindo consulta da matrícula criada e casos inválidos.

## Atividade 03 - 2BIM: Persistência com JDBC e SQLite

Alunos e disciplinas são salvos no SQLite usando JDBC.

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | Cadastra aluno — 201 |
| `GET /api/alunos/{registroAcademico}` | Consulta aluno — 200 |
| `POST /api/disciplinas` | Cadastra disciplina — 201 |
| `GET /api/disciplinas/{codigo}` | Consulta disciplina — 200 |

Exemplos de corpo:

```json
{"registroAcademico":"RA2026003","nome":"Aluno Exemplo Três","email":"aluno3@example.com"}
```

```json
{"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":80}
```

Registro inexistente retorna 404, cadastro duplicado retorna 409 e dados inválidos retornam 400.

Classes criadas (pacote `repository`):

- `AlunoRepository` e `DisciplinaRepository`: interfaces.
- `AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc`: implementações com JDBC, onde fica o SQL.
- `InicializadorBanco`: cria o banco com `database/schema.sql` e `database/dados-iniciais.sql` quando ele ainda não existe.

### Como executar

Execute a partir de `projects/academico`:

```bash
mvn spring-boot:run
```

Na primeira execução o arquivo `database/sistema-academico.db` é criado automaticamente. Nas próximas, os dados salvos são mantidos. Para voltar ao estado inicial, pare a aplicação e apague esse arquivo.

### Validação no Postman

1. Importe `postman/atividade-03-jdbc-sqlite.postman_collection.json`.
2. Execute a Collection (com o banco no estado inicial).
3. Pare e inicie a aplicação novamente.
4. Envie de novo "Consultar aluno" e "Consultar disciplina": os dados continuam lá.

## Testes e cobertura

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória. `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
