# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Matrículas e ofertas continuam em memória e são reiniciadas a cada execução. A partir da Atividade 03, alunos e disciplinas são lidos e gravados no SQLite (veja abaixo).

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

Alunos e disciplinas são persistidos em SQLite por meio de JDBC puro (sem JPA, Hibernate, Spring Data ou H2).

### Como executar

Todos os comandos e caminhos são relativos a `projects/academico`.

**1. Preparar o banco (uma vez, ou sempre que quiser voltar ao estado inicial).** Com o executável `sqlite3` no PATH:

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

Sem o `sqlite3`, execute pela IDE a classe `br.edu.sistemaacademico.PrepararBanco`, com o diretório de trabalho em `projects/academico`. Ela roda os mesmos dois scripts via JDBC no banco configurado em `app.database.url`.

O schema **apaga e recria** as tabelas. Iniciar a API não cria nem carrega o banco: se ele não foi preparado, as rotas de alunos e disciplinas respondem 500 com uma mensagem pedindo para seguir este README.

**2. Iniciar a API.**

```bash
mvn spring-boot:run
```

O banco fica em `database/sistema-academico.db` (ignorado pelo Git), com URL `jdbc:sqlite:database/sistema-academico.db` em `src/main/resources/application.properties` (`app.database.url`). Por ser um caminho relativo, inicie a aplicação a partir de `projects/academico`.

**3. Validar no Postman.** Importe `postman/atividade-03-persistencia.postman_collection.json` (variável `baseUrl` = `http://localhost:8080`):

1. com o banco recém-preparado e a API iniciada, execute a pasta **1 - Cadastro e consulta**;
2. encerre a aplicação (Ctrl+C) e inicie novamente;
3. execute a pasta **2 - Após reiniciar a aplicação** — os registros cadastrados continuam lá.

Para repetir a Collection do início, prepare o banco novamente (senão os cadastros da pasta 1 retornam 409).

### Endpoints

| Método e rota | Corpo de exemplo | Respostas |
| --- | --- | --- |
| `POST /api/alunos` | `{"registroAcademico":"RA2026100","nome":"Carla Mendes","email":"carla.mendes@example.com"}` | 201 + `Location`; 400 dados inválidos; 409 RA já cadastrado |
| `GET /api/alunos/{registroAcademico}` | — | 200; 404 inexistente |
| `POST /api/disciplinas` | `{"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}` | 201 + `Location`; 400 dados inválidos; 409 código já cadastrado |
| `GET /api/disciplinas/{codigo}` | — | 200; 404 inexistente |

Erros retornam JSON com `mensagem`. As validações (campos obrigatórios, e-mail, carga horária positiva) continuam nos construtores de `Aluno` e `Disciplina`.

### Organização

```
requisição HTTP → AlunoController / DisciplinaController   (api)
                → AlunoRepository / DisciplinaRepository   (repository — interfaces)
                → JdbcAlunoRepository / JdbcDisciplinaRepository (repository.jdbc)
                → FabricaConexoes → DriverManager → SQLite
```

- **Controllers** (`api`): recebem JSON, criam o objeto de domínio, chamam o Repository e montam a resposta HTTP. Não contêm SQL.
- **Domínio** (`domain`): sem alterações; continua sem saber que existe banco.
- **Interfaces de Repository** (`repository`): `salvar` e `buscarPor...`, retornando `Optional` quando o registro pode não existir.
- **Implementações JDBC** (`repository.jdbc`): todo o SQL da aplicação está aqui, em `PreparedStatement` com parâmetros `?`. A conversão volta para objetos em `paraAluno`/`paraDisciplina`, que leem as colunas do `ResultSet` e chamam o construtor do domínio.
- **Erros**: chave primária repetida (`SQLITE_CONSTRAINT_PRIMARYKEY`) vira `RegistroDuplicadoException` → 409; qualquer outra `SQLException` vira `PersistenciaException` → 500, com o detalhe registrado no log.

`registro_academico` corresponde a `Aluno.registroAcademico`, e `carga_horaria` a `Disciplina.cargaHoraria`. O e-mail é obrigatório, mas não é único no domínio. Matrículas e demais relacionamentos ainda não possuem tabelas e permanecem em memória.

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

`mvn clean test` executa os testes de domínio, os testes dos Repositories JDBC e os testes HTTP com servidor real em porta aleatória. Cada teste usa um banco SQLite temporário criado a partir de `database/schema.sql` e `database/dados-iniciais.sql`, sem tocar em `database/sistema-academico.db`; `PersistenciaHttpTest` também reinicia a aplicação para verificar a persistência e executa a Collection da atividade. `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
