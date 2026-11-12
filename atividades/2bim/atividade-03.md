# Atividade 03 - 2BIM: Persistência com JDBC e SQLite

## Objetivo

Evoluir a API REST do **Sistema Acadêmico**, desenvolvida na **Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot**, para persistir dados em um banco **SQLite utilizando JDBC**.

O foco da atividade é compreender a integração entre **API, objetos Java e banco de dados**, mantendo separadas as responsabilidades de comunicação HTTP, regras de negócio e acesso aos dados.

## Contexto

Na atividade anterior, os dados da aplicação permaneciam apenas em memória e eram perdidos quando a aplicação era encerrada.

Nesta atividade, parte desses dados deverá ser armazenada de forma persistente.

O projeto-base fornecerá:

- driver JDBC do SQLite;
- `database/schema.sql`;
- `database/dados-iniciais.sql`;
- configuração básica do caminho do banco;
- solução concluída da Atividade 02 - 2BIM.

A implementação dos Repositories e das operações JDBC deverá ser realizada pelos alunos.

## Materiais de apoio

Utilize como referência:

- JDBC Basics: https://docs.oracle.com/javase/tutorial/jdbc/basics/
- `DriverManager`: https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/DriverManager.html
- `PreparedStatement`: https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/PreparedStatement.html
- `ResultSet`: https://docs.oracle.com/en/java/javase/26/docs/api/java.sql/java/sql/ResultSet.html
- SQLite JDBC: https://github.com/xerial/sqlite-jdbc

## Atividade

Implemente a persistência de **Aluno** e **Disciplina** utilizando JDBC e SQLite.

A API deve continuar permitindo as operações equivalentes a:

```http
POST /api/alunos
GET /api/alunos/{ra}

POST /api/disciplinas
GET /api/disciplinas/{codigo}
```

Caso o projeto já utilize rotas equivalentes, elas podem ser mantidas.

## Repository

Separe a responsabilidade de acesso ao banco por meio de interfaces de Repository.

Exemplo conceitual:

```java
public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorRa(String ra);
}
```

Crie uma implementação concreta utilizando JDBC.

Estrutura equivalente deve ser criada para `Disciplina`.

## JDBC

A implementação deve utilizar:

- `Connection`;
- `PreparedStatement`;
- `ResultSet`;
- tratamento adequado de `SQLException`.

Utilize `PreparedStatement` para as consultas e comandos SQL.

Os comandos SQL **não devem ficar nos Controllers nem nas classes do domínio**.

## Responsabilidades

Mantenha a separação:

- **Controller:** recebe e responde requisições HTTP;
- **Domínio:** mantém as regras de negócio;
- **Repository:** define as operações de acesso aos dados;
- **Implementação JDBC:** executa SQL e transforma registros em objetos.

## Persistência

Após cadastrar um aluno ou uma disciplina:

1. encerre a aplicação;
2. execute-a novamente;
3. consulte o registro cadastrado.

O dado deve continuar disponível após a reinicialização.

## Cenários obrigatórios

Valide pelo menos:

- cadastro de aluno;
- consulta de aluno;
- cadastro de disciplina;
- consulta de disciplina;
- consulta de registro inexistente;
- tentativa de cadastro duplicado;
- persistência dos dados após reiniciar a aplicação.

Utilize status HTTP coerentes com cada situação.

## Restrições

Nesta atividade, não utilize:

- JPA;
- Hibernate;
- Spring Data JPA;
- H2;
- geração automática de tabelas por ORM.

O objetivo é compreender a comunicação direta entre **Java e banco de dados utilizando JDBC**.

## Validação

Utilize o **Postman** para executar as requisições da API.

A validação deve demonstrar que os dados cadastrados permanecem disponíveis mesmo após reiniciar a aplicação.

## Entrega

Realize o desenvolvimento em uma branch própria e abra um **Pull Request**.

O Pull Request deve conter:

- implementação dos Repositories;
- integração JDBC com SQLite;
- API funcionando com persistência;
- Collection do Postman utilizada para validação;
- instruções necessárias para executar o projeto e preparar o banco.

Responda também brevemente:

1. Onde estão os comandos SQL da aplicação?
2. Por que o Controller não deve executar SQL diretamente?
3. Qual é o objetivo da interface de Repository?
4. O que precisaria mudar para substituir SQLite por outra tecnologia de persistência?

## Apresentação

Demonstre pelo Postman pelo menos um cadastro e uma consulta.

Esteja preparado para explicar o fluxo:

```text
requisição HTTP
      ↓
Controller
      ↓
Repository
      ↓
JDBC
      ↓
SQLite
```

e como os dados do banco são convertidos novamente em objetos Java.
