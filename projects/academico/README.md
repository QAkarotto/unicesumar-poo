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

Os testes ficam em `src/test/java` e cobrem oferta de disciplinas, proteção das coleções, matrículas, mudanças de estado e as regras de aprovação e reprovação do histórico acadêmico.

Sugestão de demonstração em aula:

1. Execute `mvn test` e observe todos os testes passando.
2. Remova temporariamente a validação de matrícula duplicada em `OfertaDisciplina`.
3. Execute `mvn test` novamente e observe a falha.
4. Restaure a validação e confirme que os testes voltam a passar.

O workflow **Java Tests** do GitHub Actions executa os mesmos testes a cada `push` e `pull_request`.


## Atividade 03 - Persistência com JDBC e SQLite

### Como executar
No diretório `projects/academico`:

```bash
mvn spring-boot:run
```

Ao subir, o `InicializadorBanco` cria `database/academico.db` (se não existir), executa `database/schema.sql` e `database/dados-iniciais.sql`. Os scripts são idempotentes: reiniciar a aplicação **não apaga** os dados. Para zerar o banco, apague `database/academico.db`.

O caminho do banco é configurado em `src/main/resources/application.properties` (`app.database.url`).

### Rotas
| Método | Rota | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/alunos` | 201 | 400, 409 (RA duplicado), 422 |
| GET | `/api/alunos/{ra}` | 200 | 404 |
| POST | `/api/disciplinas` | 201 | 400, 409 (código duplicado), 422 |
| GET | `/api/disciplinas/{codigo}` | 200 | 404 |

Collection do Postman: `postman/atividade-03-persistencia.postman_collection.json`.
Teste de persistência: execute 1, 2, 6 e 7, encerre a aplicação, execute-a de novo e rode 2 e 7 novamente (200).

### Perguntas
**Onde estão os comandos SQL?** Apenas em `repository/jdbc/JdbcAlunoRepository` e `JdbcDisciplinaRepository` (constantes `INSERT`/`SELECT_*`) e nos scripts de `database/`.

**Por que o Controller não deve executar SQL?** Ele só deve cuidar de HTTP. Misturar SQL o acopla ao banco, impede reaproveitar a lógica, dificulta testes e espalha mudanças de schema pela API.

**Qual o objetivo da interface de Repository?** Definir o contrato de acesso aos dados (`salvar`, `buscarPor...`) sem expor como são armazenados, de modo que Controller e domínio dependam da abstração e não do JDBC.

**O que mudar para trocar o SQLite?** Criar uma nova implementação das interfaces (ex.: PostgreSQL ou JPA) e trocar a dependência/configuração. Controllers, domínio e interfaces permanecem iguais.
