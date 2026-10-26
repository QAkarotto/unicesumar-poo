# Sistema Acadêmico

Projeto didático em Java 21, Spring Boot, JDBC e SQLite para praticar orientação a objetos, API REST e persistência.

O domínio representa alunos, turmas, ofertas de disciplinas e matrículas. As regras permanecem nos próprios objetos, sem frameworks ou camadas adicionais.

## Executando a API

No diretório `projects/academico`, execute:

```bash
mvn spring-boot:run
```

O banco persistente é criado em `database/sistema-academico.db`. A estrutura e os dados iniciais são aplicados a partir de `src/main/resources/database/` toda vez que a aplicação inicia, sem apagar registros existentes.

## Endpoints

| Método | Endpoint | Resultado |
| --- | --- | --- |
| POST | `/api/alunos` | Cria um aluno (`201`) |
| GET | `/api/alunos/{registroAcademico}` | Consulta aluno (`200` ou `404`) |
| POST | `/api/disciplinas` | Cria uma disciplina (`201`) |
| GET | `/api/disciplinas/{codigo}` | Consulta disciplina (`200` ou `404`) |

Exemplo de aluno:

```json
{
  "registroAcademico": "RA2026999",
  "nome": "Ana Silva",
  "email": "ana.silva@email.com"
}
```

Exemplo de disciplina:

```json
{
  "codigo": "ENG",
  "nome": "Engenharia de Software",
  "cargaHoraria": 80
}
```

Registros duplicados retornam `409 Conflict`; dados inválidos retornam `422 Unprocessable Entity`.

## Organização da persistência

Os comandos SQL ficam exclusivamente nas implementações `AlunoJdbcRepository` e `DisciplinaJdbcRepository`. Os controllers apenas recebem a requisição e produzem a resposta HTTP; a interface Repository desacopla a API da tecnologia de persistência. Para trocar SQLite por outra tecnologia, basta fornecer novas implementações dessas interfaces e ajustar a configuração/inicialização do banco.

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
