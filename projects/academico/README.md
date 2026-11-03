# Sistema Acadêmico

Projeto desenvolvido em Java com Spring Boot para gerenciamento de alunos e disciplinas, utilizando JDBC e SQLite para persistência dos dados.

## Tecnologias utilizadas

- Java 25
- Spring Boot 3.5.5
- Maven
- JDBC
- SQLite

## Estrutura do projeto

- `controller`: contém os controladores responsáveis pelas requisições da API.
- `domain`: contém as classes de domínio, como Aluno e Disciplina.
- `repository`: contém as interfaces dos repositórios.
- `repository/jdbc`: contém as implementações JDBC responsáveis pelo acesso ao banco de dados.
- `resources/database`: contém os scripts SQL utilizados pelo projeto.

## Banco de dados

O projeto utiliza SQLite para armazenar os dados.

O banco é configurado em `application.properties`:

`app.database.url=jdbc:sqlite:./database/sistema-academico.db`

A estrutura das tabelas é definida no arquivo `schema.sql`.

## Funcionalidades

### Alunos

- Cadastrar aluno.
- Consultar aluno pelo identificador acadêmico.
- Verificar se o aluno já está cadastrado.

### Disciplinas

- Cadastrar disciplina.
- Consultar disciplina pelo código.
- Verificar se a disciplina já está cadastrada.

## Endpoints da API

| Método | Endpoint | Funcionalidade |
|---|---|---|
| POST | `/alunos` | Cadastrar aluno |
| GET | `/alunos/{ra}` | Consultar aluno |
| POST | `/disciplinas` | Cadastrar disciplina |
| GET | `/disciplinas/{codigo}` | Consultar disciplina |

## Como executar

1. Abra o projeto no IntelliJ IDEA.
2. Aguarde o Maven carregar as dependências.
3. Execute a classe `SistemaAcademico`.
4. O Spring Boot iniciará a aplicação na porta 8080.
5. O banco de dados SQLite será preparado automaticamente.

Também é possível compilar pelo terminal, dentro da pasta `projects/academico`, utilizando:

```bash
mvn clean compile
```

Para iniciar a aplicação pelo Maven:

```bash
mvn spring-boot:run
```

## Persistência

Os dados cadastrados são armazenados no banco SQLite, permitindo que permaneçam salvos mesmo após encerrar e iniciar novamente a aplicação.