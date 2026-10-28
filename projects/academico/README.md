# Sistema Acadêmico

Projeto didático em Java 26 para praticar orientação a objetos e testes unitários com JUnit 5.

O domínio representa alunos, turmas, ofertas de disciplinas e matrículas. As regras permanecem nos próprios objetos, sem frameworks ou camadas adicionais.

## Executando a aplicação

No diretório `projects/academico`, compile com:

```bash
mvn compile
```

Depois, execute `br.edu.sistemaacademico.SistemaAcademico` pela IDE.

## Testes unitários

Para executar todos os testes:

```bash
mvn test
```

Para executar apenas os testes de matrícula:

```bash
mvn -Dtest=MatriculaTest test
```

Para executar os testes e verificar a cobertura de código:

```bash
mvn verify
```

O JaCoCo exige no mínimo 80% de cobertura de linhas das classes de domínio. O relatório HTML é gerado em `target/site/jacoco/index.html`. A classe `SistemaAcademico` não entra nessa métrica porque representa o fluxo de demonstração da aplicação.

Os testes ficam em `src/test/java` e cobrem oferta de disciplinas, proteção das coleções, matrículas, mudanças de estado e as regras de aprovação e reprovação do histórico acadêmico.

O workflow **Java Tests** do GitHub Actions executa os testes e verifica a cobertura a cada `push` e `pull_request`.

## API REST com persistência em SQLite (Atividade 03)

`Aluno` e `Disciplina` são gravados em um banco SQLite usando JDBC puro (sem JPA, Hibernate ou ORM). As matrículas continuam em memória.

### Camadas

```
requisição HTTP
      ↓
Controller            (api/AlunoController, api/DisciplinaController)
      ↓
Repository            (repository/AlunoRepository, DisciplinaRepository: interfaces)
      ↓
JDBC                  (repository/jdbc/*RepositoryJdbc: Connection, PreparedStatement, ResultSet)
      ↓
SQLite                (database/academico.db)
```

- **Controller:** recebe e responde HTTP; não tem SQL.
- **Domínio:** `Aluno` e `Disciplina` validam seus próprios dados.
- **Repository:** interface que define `salvar` e `buscar...`, sem mencionar SQL.
- **Implementação JDBC:** único lugar com comandos SQL; converte cada linha do `ResultSet` em objeto (`new Aluno(...)`, `new Disciplina(...)`).

### Preparando o banco

Os scripts ficam em `database/`. A partir do diretório `projects/academico`:

```bash
sqlite3 database/academico.db ".read database/schema.sql"
sqlite3 database/academico.db ".read database/dados-iniciais.sql"
```

Sem o `sqlite3` instalado, abra/crie `database/academico.db` em uma ferramenta como o DB Browser for SQLite e execute o conteúdo dos dois arquivos, nessa ordem. Os scripts podem ser repetidos sem duplicar dados.

Para recomeçar do zero, apague `database/academico.db` e repita os comandos acima.

O caminho do banco é `academico.database.url` em `application.properties` (padrão `jdbc:sqlite:database/academico.db`, relativo ao diretório de execução, por isso execute sempre a partir de `projects/academico`). Também pode ser trocado pela variável de ambiente `ACADEMICO_DATABASE_URL`.

### Executando a API

```bash
mvn spring-boot:run
```

Ou execute `br.edu.sistemaacademico.AcademicoApplication` pela IDE, com o diretório de trabalho em `projects/academico`. A API sobe em `http://localhost:8080`.

Se o banco não tiver sido preparado, as rotas respondem `500` e o log do servidor informa a causa (`no such table`).

### Endpoints

| Método | Rota | Sucesso | Erros |
|--------|------|---------|-------|
| POST | `/api/alunos` | `201` + `Location` | `400` dados inválidos, `409` RA já cadastrado |
| GET | `/api/alunos/{ra}` | `200` | `404` não encontrado |
| POST | `/api/disciplinas` | `201` + `Location` | `400` dados inválidos, `409` código já cadastrado |
| GET | `/api/disciplinas/{codigo}` | `200` | `404` não encontrada |

Qualquer falha inesperada do banco responde `500` com uma mensagem genérica; os detalhes ficam só no log.

Exemplos de corpo:

```json
{ "registroAcademico": "RA2026100", "nome": "Maria Teste", "email": "maria.teste@email.com" }
```

```json
{ "codigo": "ALG", "nome": "Algoritmos e Estruturas de Dados", "cargaHoraria": 60 }
```

Mantidos da Atividade 02: `GET /api/matriculas/{id}` e `POST /api/matriculas`. Alunos cadastrados pela API também podem ser matriculados.

### Validando a persistência

1. Importe `postman/Sistema Academico - Atividade 03.postman_collection.json` no Postman (detalhes em `POSTMAN.md`).
2. Envie as 8 requisições soltas da Collection, uma por vez e na ordem (cadastro, consulta, duplicado e inexistente, de aluno e de disciplina).
3. Encerre a aplicação e suba-a novamente.
4. Envie as requisições da pasta **Executar DEPOIS de reiniciar a aplicação**: o aluno e a disciplina cadastrados antes do reinício continuam disponíveis.

Os testes de `Repository` (`mvn test`) usam um arquivo SQLite temporário e também verificam que os dados sobrevivem a uma nova conexão com o mesmo arquivo.

> Em versões recentes do JDK, o driver `sqlite-jdbc` pode exibir um aviso sobre acesso nativo ao carregar a biblioteca. O aviso é inofensivo.