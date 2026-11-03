# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Matrículas são reiniciadas a cada execução; Aluno e Disciplina ficam no SQLite (Atividade 03).

Link Postman: https://www.postman.com/dmachadobaitel-4417484/workspace/atividade-03-2bim-poo/collection/58688141-8c147ae3-ea3e-43c7-a496-17c0cb9b13ba?action=share&creator=58688141

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

Aluno e Disciplina agora são gravados no SQLite por meio de Repositories com JDBC (`Connection`, `PreparedStatement` e `ResultSet`). Matrículas continuam em memória.

Antes de iniciar a API, crie o banco (a partir de `projects/academico`, com `sqlite3` no PATH):

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

O primeiro comando recria as tabelas e apaga os dados anteriores; o segundo deve ser executado uma vez após o schema. A URL do banco está em `src/main/resources/application.properties` (`app.database.url`).

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | 201 e `Location`; 409 se o RA já existir; 400 se os dados forem inválidos |
| `GET /api/alunos/{registroAcademico}` | 200; 404 se não existir |
| `POST /api/disciplinas` | 201 e `Location`; 409 se o código já existir; 400 se os dados forem inválidos |
| `GET /api/disciplinas/{codigo}` | 200; 404 se não existir |

Exemplos de corpo:

```json
{"registroAcademico":"RA2026003","nome":"Aluno Novo","email":"novo@example.com"}
{"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}
```

Estrutura: `AcademicoController` (HTTP) → `AlunoRepository` / `DisciplinaRepository` (interfaces) → `AlunoRepositoryJdbc` / `DisciplinaRepositoryJdbc` (SQL). Os SQLs ficam apenas nas implementações JDBC; as regras de validação permanecem no domínio. Falhas inesperadas do banco retornam 500.

Fluxo conceitual: **Connection → PreparedStatement → ResultSet**.

- `Connection` representa a conexão com o banco, obtida por meio do `DriverManager`.
- `PreparedStatement` representa um comando SQL parametrizado a ser executado.
- `ResultSet` permite percorrer os registros retornados por uma consulta.

A implementação desse fluxo e a conversão entre registros e objetos são responsabilidade dos alunos.

Referências:

- [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/)
- [DriverManager — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/DriverManager.html)
- [PreparedStatement — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/PreparedStatement.html)
- [ResultSet — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/ResultSet.html)
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)

## Testes e cobertura

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória.`mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
