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

## API REST (Spring Boot) com persistência JDBC + SQLite

Alunos e disciplinas são gravados em um banco SQLite (`database/academico.db`) com JDBC puro: `Connection`, `PreparedStatement` e `ResultSet`. Não há JPA, Hibernate, Spring Data nem H2. Turmas e matrículas ainda ficam em memória.

### Preparando o banco

Os scripts SQL ficam em `database/`:

- `schema.sql`: cria as tabelas `aluno` e `disciplina`;
- `dados-iniciais.sql`: insere os alunos `RA2026001` (Paola) e `RA2026002` (Bruno) e as disciplinas `POO` e `BD`.

Ao subir a API, o `BancoDeDados` executa os dois scripts automaticamente. Eles podem rodar várias vezes sem apagar nem duplicar dados. O caminho do banco e dos scripts está em `src/main/resources/application.properties`, relativo a `projects/academico`.

Para preparar o banco manualmente (opcional, exige o `sqlite3` instalado):

```bash
cd projects/academico
sqlite3 database/academico.db < database/schema.sql
sqlite3 database/academico.db < database/dados-iniciais.sql
```

Para recomeçar do zero, apague `database/academico.db`; ele é recriado na próxima execução.

### Executando

No diretório `projects/academico`, suba a API em `http://localhost:8080`:

```bash
mvn spring-boot:run
```

| Método | Rota | Resposta |
|--------|------|----------|
| POST | `/api/alunos` | 201, 400 (dados inválidos) ou 409 (RA já cadastrado) |
| GET | `/api/alunos/{registroAcademico}` | 200, ou 404 se não existir |
| POST | `/api/disciplinas` | 201, 400 (dados inválidos) ou 409 (código já cadastrado) |
| GET | `/api/disciplinas/{codigo}` | 200, ou 404 se não existir |
| GET | `/api/matriculas/{id}` | 200, ou 404 se não existir |
| POST | `/api/matriculas` | 201, 400 (campos faltando), 404 (aluno, turma ou disciplina inexistente) ou 409 (regra do domínio) |

Exemplos de corpo:

- `POST /api/alunos`: `{"registroAcademico": "RA2026010", "nome": "Ana Lima", "email": "ana@email.com"}`
- `POST /api/disciplinas`: `{"codigo": "ALG", "nome": "Algoritmos", "cargaHoraria": 60}`
- `POST /api/matriculas`: `{"registroAcademico": "RA2026002", "turma": "ADSIS4S", "disciplina": "BD"}` (`codigo` é opcional)

### Verificando a persistência

1. Suba a API e cadastre um aluno e uma disciplina pelo Postman.
2. Encerre a aplicação (Ctrl+C) e suba de novo com `mvn spring-boot:run`.
3. Consulte `GET /api/alunos/RA2026010` e `GET /api/disciplinas/ALG`: os dois continuam retornando 200.

### Arquitetura

```
requisição HTTP → Controller → Repository (interface) → JDBC (Jdbc*Repository) → SQLite
```

- `api`: controllers, requests e responses. Só tratam HTTP.
- `domain`: regras de negócio (`Aluno`, `Disciplina`, `Matricula`...).
- `repository`: interfaces `AlunoRepository` e `DisciplinaRepository`.
- `repository.jdbc`: `JdbcAlunoRepository`, `JdbcDisciplinaRepository` e `BancoDeDados`. É o único lugar com SQL no código Java, além dos scripts de `database/`.

### Collection do Postman

Link da Collection: https://documenter.getpostman.com/view/58935716/2sBYHVxfbD

O arquivo [`postman-collection.json`](postman-collection.json) pode ser importado no Postman.
