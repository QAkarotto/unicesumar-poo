# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Desde a Atividade 03, alunos e disciplinas são lidos do SQLite, então prepare o banco antes de subir a API (ver abaixo). Ofertas e matrículas continuam em memória e são reiniciadas a cada execução.

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

Aluno e Disciplina são gravados no SQLite. As interfaces `AlunoRepository` e `DisciplinaRepository` ficam no pacote `persistencia`, junto das implementações com JDBC (`AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc`), que são as únicas classes com SQL. A conexão sai do `DriverManager` em `ConexaoBanco`.

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | 201 e `Location`; RA repetido dá 409; dados inválidos, 400 |
| `GET /api/alunos/{registroAcademico}` | 200, ou 404 se não existir |
| `POST /api/disciplinas` | 201 e `Location`; código repetido dá 409; dados inválidos, 400 |
| `GET /api/disciplinas/{codigo}` | 200, ou 404 se não existir |

```json
{"registroAcademico":"RA2026010","nome":"Carla Mendes","email":"carla.mendes@example.com"}
{"codigo":"ED","nome":"Estrutura de Dados","cargaHoraria":60}
```

O driver `org.xerial:sqlite-jdbc:3.53.4.0` está configurado no Maven.

- Estrutura: `database/schema.sql`.
- Dois alunos e duas disciplinas fictícias: `database/dados-iniciais.sql`.
- Banco local: `database/sistema-academico.db` (ignorado pelo Git).
- URL: `jdbc:sqlite:database/sistema-academico.db`, declarada em `src/main/resources/application.properties` como `app.database.url`.

Os caminhos são relativos a `projects/academico`. Com o executável `sqlite3` instalado e disponível no PATH, execute nesse diretório (PowerShell, Bash ou terminal equivalente):

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

O primeiro comando recria as duas tabelas e apaga seus dados anteriores. O segundo deve ser executado uma vez após o schema. A inicialização é manual; iniciar a API não cria nem carrega o banco. Sem o `sqlite3`, no Windows dá pra instalar com `winget install SQLite.SQLite`.

Para validar a persistência pelo Postman, importe `postman/atividade-03-persistencia.postman_collection.json` e, com o banco recém-preparado:

1. suba a API com `mvn spring-boot:run` e rode a pasta **1. Cadastro e consulta** (cadastros, consultas, inexistentes e duplicados);
2. pare a API (Ctrl+C), suba de novo e rode a pasta **2. Depois de reiniciar a API**, que consulta o aluno e a disciplina cadastrados antes.

Para repetir do zero, rode os dois comandos do `sqlite3` de novo.

`registro_academico` corresponde a `Aluno.registroAcademico`, e `carga_horaria` a `Disciplina.cargaHoraria`. O e-mail é obrigatório, mas não é único no domínio. Matrículas e demais relacionamentos ainda não possuem tabelas.

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

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória. `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
