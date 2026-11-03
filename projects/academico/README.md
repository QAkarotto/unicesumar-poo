# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`.

| Método e rota | Exemplo / resultado |
| --- | --- |
| `GET /api/matriculas/{codigo}` | `MAT-001` — 200 |
| `POST /api/matriculas` | Cria matrícula — 201 e cabeçalho `Location` |

Exemplo de corpo para criar uma matrícula:

```json
{"registroAcademico":"RA2026002","ofertaId":"POO-2026-2"}
```

Ofertas de exemplo: `POO-2026-2` e `BD-2026-2`, ambas da turma `ESOFT4S-NA`, período 2026/2. `ofertaId` é a chave em memória da oferta; o código da matrícula é gerado pela aplicação. `MAT-001` já vincula `RA2026001` à oferta de POO.

Recurso inexistente retorna 404; JSON/campos inválidos retornam 400; impedimento por estado do domínio ou cadastro duplicado retorna 409. Erros retornam JSON com `mensagem`.

## Atividade 03 - 2BIM: Persistência com JDBC e SQLite

`Aluno` e `Disciplina` agora são gravados e consultados em um banco SQLite por meio de JDBC puro (sem JPA, Hibernate ou Spring Data). As matrículas continuam em memória.

Fluxo: **HTTP → Controller → Repository (interface) → RepositoryJdbc → SQLite**.

### Onde está cada parte

- `api/AcademicoController`: recebe o HTTP e chama os Repositories. Não contém SQL.
- `repository/AlunoRepository` e `repository/DisciplinaRepository`: interfaces (contrato).
- `repository/AlunoRepositoryJdbc` e `repository/DisciplinaRepositoryJdbc`: implementações JDBC, onde está todo o SQL (`Connection`, `PreparedStatement`, `ResultSet`, tratamento de `SQLException`).
- `domain`: classes de negócio, sem SQL.
- `database/schema.sql` e `database/dados-iniciais.sql`: estrutura e dados de exemplo.

### Preparar o banco (uma vez)

Os caminhos são relativos a `projects/academico`. Com o `sqlite3` instalado e no PATH, execute nesse diretório:

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

O primeiro comando recria as tabelas e apaga os dados anteriores; use-o apenas para preparar ou resetar o banco. O arquivo `database/sistema-academico.db` é ignorado pelo Git. A API não cria o banco sozinha, então ele precisa estar preparado antes de executar a API ou os testes.

### Executar

A partir de `projects/academico` (a URL do banco é relativa a essa pasta):

```bash
mvn clean test
mvn spring-boot:run
```

### Rotas

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | Cadastra aluno — 201; RA repetido — 409; dados inválidos — 400 |
| `GET /api/alunos/{ra}` | 200 se existe; 404 se não existe |
| `POST /api/disciplinas` | Cadastra disciplina — 201; código repetido — 409; dados inválidos — 400 |
| `GET /api/disciplinas/{codigo}` | 200 se existe; 404 se não existe |

Exemplos de corpo:

```json
{"registroAcademico":"RA2026010","nome":"Maria Souza","email":"maria@example.com"}
```

```json
{"codigo":"ALG","nome":"Algoritmos","cargaHoraria":60}
```

### Persistência após reiniciar

1. Inicie a API e cadastre um aluno com `POST /api/alunos`.
2. Pare a API e inicie novamente.
3. Consulte `GET /api/alunos/{ra}`: o aluno continua lá, pois está no arquivo SQLite e não na memória.

### Postman

Use **Import** e selecione `postman/atividade-03-jdbc.postman_collection.json`. A variável `baseUrl` aponta para `http://localhost:8080`. Execute a Collection na ordem, com o banco recém-preparado (os comandos de preparação acima). Ela cobre cadastro e consulta de aluno e disciplina, consulta de registro inexistente (404) e cadastro duplicado (409).

## Testes e cobertura

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória (o banco deve estar preparado). `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`.
