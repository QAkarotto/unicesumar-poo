# Atividade 03 — Persistência JDBC e SQLite

## Requisitos
- JDK compatível com `maven.compiler.release` definido no `pom.xml`.
- Maven.
- Postman (para validar a API).

## Executar
1. Abra a pasta `projects/academico` como projeto Maven.
2. Confira `src/main/resources/application.properties`. O banco é configurado por `app.database.url`.
3. Execute `mvn clean test`.
4. Inicie a aplicação pela classe `br.edu.sistemaacademico.AplicacaoAcademica` ou execute `mvn spring-boot:run`.
5. Importe `postman/sistema-academico.postman_collection.json` no Postman e execute os requests.

## Banco de dados
O SQLite usa o arquivo indicado em `app.database.url` (por padrão `database/sistema-academico.db`). Na inicialização, `ConexaoSQLite` cria as tabelas apenas se elas não existirem e insere os registros de demonstração com `INSERT OR IGNORE`. Não execute `database/schema.sql` a cada inicialização: esse script contém `DROP TABLE` e apagaria os dados existentes. Para preparar um banco vazio manualmente, faça backup do arquivo atual antes de removê-lo e inicie a aplicação novamente.

## Endpoints da Atividade 03
- `POST /api/alunos`
- `GET /api/alunos/{registroAcademico}`
- `POST /api/disciplinas`
- `GET /api/disciplinas/{codigo}`

Cadastros retornam `201`, consultas existentes `200`, registros inexistentes `404` e chaves duplicadas `409`.

## Responsabilidades
- Controller: recebe requisições HTTP e escolhe a resposta.
- Domínio: representa Aluno e Disciplina e valida suas regras.
- Interfaces Repository: definem as operações disponíveis.
- Implementações JDBC: usam `Connection`, `PreparedStatement` e `ResultSet`; os comandos SQL ficam aqui.
- SQLite: guarda os dados no arquivo persistente.

## Respostas conceituais
1. Os comandos SQL ficam nas implementações `AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc` (e na inicialização do banco em `ConexaoSQLite`).
2. O Controller não deve executar SQL porque isso mistura HTTP com persistência e dificulta testes e manutenção.
3. A interface Repository define um contrato independente da tecnologia concreta de banco.
4. Para trocar SQLite, seria necessário criar outra implementação das interfaces e adaptar a configuração/conexão; Controllers e domínio idealmente continuariam iguais.

## Verificação de persistência
Cadastre um aluno e uma disciplina pelo Postman, encerre a aplicação, inicie-a novamente e consulte os registros pelos respectivos GET. Não apague o arquivo `.db` entre as duas etapas.
