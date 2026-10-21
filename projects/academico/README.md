# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Matrículas são reiniciadas a cada execução; desde a Atividade 03, alunos e disciplinas ficam no SQLite (veja abaixo).

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

Aluno e Disciplina são gravados no SQLite por JDBC puro (`DriverManager`, `PreparedStatement`, `ResultSet`), sem JPA, Hibernate, Spring Data ou H2. Matrículas, turmas e ofertas continuam em memória, porque ainda não têm tabela.

### Como executar

A partir de `projects/academico`, com JDK 26 e Maven no PATH:

```bash
mvn spring-boot:run
```

A API precisa ser iniciada **de dentro de `projects/academico`**, porque o caminho do banco (`database/sistema-academico.db`) e dos scripts é relativo a essa pasta. Na IDE, configure o diretório de trabalho para ela.

### Preparação do banco

Não é preciso fazer nada: se o arquivo do banco não existir ou não tiver as tabelas, a API executa `database/schema.sql` e `database/dados-iniciais.sql` ao iniciar (`InicializadorBanco`). Se as tabelas já existem, os scripts **não** são executados de novo. O `schema.sql` começa com `DROP TABLE`, então rodá-lo a cada inicialização apagaria tudo o que foi cadastrado.

Para começar do zero, apague `database/sistema-academico.db` com a API parada, ou recrie o banco manualmente com o `sqlite3`:

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

### Endpoints

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | `{"registroAcademico","nome","email"}` — 201 com `Location` |
| `GET /api/alunos/{registroAcademico}` | 200, ou 404 se não existir |
| `POST /api/disciplinas` | `{"codigo","nome","cargaHoraria"}` — 201 com `Location` |
| `GET /api/disciplinas/{codigo}` | 200, ou 404 se não existir |

Cadastro com RA ou código já existente retorna **409**. Dados inválidos para o domínio (campo vazio, e-mail inválido, carga horária ausente ou não positiva) e JSON malformado retornam **400**. Erro inesperado no banco retorna 500 com mensagem genérica; o detalhe fica no log. O e-mail não é único: dois alunos podem ter o mesmo, como no domínio.

Um aluno cadastrado pela API também pode ser matriculado em `POST /api/matriculas`: a matrícula busca o aluno pelo Repository.

### Organização

```
requisição HTTP → AlunoController / DisciplinaController → AlunoRepository / DisciplinaRepository (interfaces)
                → AlunoRepositoryJdbc / DisciplinaRepositoryJdbc → JDBC → SQLite
```

- `api`: Controllers. Recebem o JSON, criam o objeto do domínio e devolvem a resposta HTTP. Não têm SQL.
- `domain`: regras e validações, sem alteração nesta atividade.
- `persistencia`: interfaces de Repository, implementações JDBC, `ConexaoBanco` e `InicializadorBanco`.

Na volta, cada linha do `ResultSet` vira objeto pelo construtor do domínio (`new Aluno(...)`, `new Disciplina(...)`), passando pelas mesmas validações de um cadastro novo.

### Validação no Postman

Importe `postman/atividade-03-jdbc-sqlite.postman_collection.json`.

1. Com a API no ar, execute a pasta **1. Antes de reiniciar**. Ela cadastra um aluno e uma disciplina com RA e código gerados na hora, consulta os dois, e testa duplicado (409), inválido (400) e inexistente (404).
2. Encerre a API e inicie de novo.
3. Execute a pasta **2. Depois de reiniciar a API**. Ela consulta os mesmos registros, que devem continuar lá.

Como o RA e o código mudam a cada execução da pasta 1, a Collection pode ser repetida sem apagar o banco.

Os mesmos cenários estão automatizados em `PersistenciaHttpTest`. Cada teste usa um banco SQLite numa pasta temporária, então `mvn test` não mexe no banco local.

### Perguntas

**Onde estão os comandos SQL da aplicação?** Nas classes `AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc`, do pacote `persistencia`, como constantes usadas com `PreparedStatement`. A estrutura das tabelas fica em `database/schema.sql`. Controllers e domínio não têm SQL.

**Por que o Controller não deve executar SQL diretamente?** Porque o papel dele é traduzir HTTP: ler a requisição e montar a resposta com o status certo. Com SQL dentro, ele passaria a depender de tabela, coluna e conexão. Qualquer mudança no banco obrigaria a mexer na camada web, e o mesmo SQL acabaria repetido em vários endpoints.

**Qual é o objetivo da interface de Repository?** Separar *o que* se precisa dos dados (`salvar`, `buscarPorRa`) de *como* isso é feito. O Controller depende só da interface e trabalha com objetos do domínio, sem saber que existe JDBC ou SQLite por trás.

**O que precisaria mudar para substituir SQLite por outra tecnologia de persistência?** Criar novas implementações das interfaces (por exemplo `AlunoRepositoryPostgres`, ou uma versão que grave em arquivo) e registrá-las no lugar das atuais. Controllers e domínio continuam iguais. Para outro banco relacional, bastaria trocar o driver no `pom.xml` e a `app.database.url`, além de ajustar o SQL específico do SQLite (a detecção de chave duplicada em `ErrosSqlite` e a consulta à `sqlite_master` no `InicializadorBanco`).

## Testes e cobertura

`mvn clean test` executa os testes de domínio e os testes HTTP com servidor real em porta aleatória. `mvn -Dtest=MatriculaTest test` executa apenas os testes de matrícula.

`mvn verify` verifica também a cobertura mínima de 80% de linhas do domínio, com relatório em `target/site/jacoco/index.html`. A demonstração `SistemaAcademico` permanece excluída do relatório. O workflow **Java Tests** mantém Java 26, executa `mvn --batch-mode clean test` e verifica a cobertura sem repetir os testes.
