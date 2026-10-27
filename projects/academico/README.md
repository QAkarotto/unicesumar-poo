# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Os dados são reiniciados a cada execução; SQLite não é utilizado pelos Controllers.

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

Esta implementação persiste Aluno e Disciplina em SQLite por meio de JDBC. Os comandos SQL estão nas implementações JDBC dos Repositories, mantendo Controllers e domínio separados do acesso ao banco.

O driver `org.xerial:sqlite-jdbc:3.53.4.0` está configurado no Maven. O banco é inicializado automaticamente quando a API inicia, a partir de `database/schema.sql` e `database/dados-iniciais.sql`, sem apagar registros existentes.

- Estrutura: `src/main/resources/database/schema.sql`.
- Dois alunos e duas disciplinas fictícias: `src/main/resources/database/dados-iniciais.sql`.
- Banco local: `database/sistema-academico.db` (ignorado pelo Git).
- URL: `jdbc:sqlite:database/sistema-academico.db`, declarada em `src/main/resources/application.properties` como `app.database.url`, somente para uso futuro.

Os caminhos do banco são relativos a `projects/academico`; o arquivo gerado é ignorado pelo Git.

`registro_academico` corresponde a `Aluno.registroAcademico`, e `carga_horaria` a `Disciplina.cargaHoraria`. O e-mail é obrigatório, mas não é único no domínio. Matrículas e demais relacionamentos ainda não possuem tabelas.

Fluxo conceitual: **Connection → PreparedStatement → ResultSet**.

- `Connection` representa a conexão com o banco, obtida por meio do `DriverManager`.
- `PreparedStatement` representa um comando SQL parametrizado a ser executado.
- `ResultSet` permite percorrer os registros retornados por uma consulta.

A implementação JDBC transforma cada `ResultSet` em um objeto Java. Os endpoints disponíveis são `POST` e `GET` para `/api/alunos` e `/api/disciplinas`; cadastro duplicado retorna `409`, dado inválido retorna `422` e registro inexistente retorna `404`.

Referências:

- [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/)
- [DriverManager — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/DriverManager.html)
- [PreparedStatement — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/PreparedStatement.html)
- [ResultSet — Java 26](https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/ResultSet.html)
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)

## Testes e cobertura

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória. `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
