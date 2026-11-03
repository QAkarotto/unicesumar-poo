# Sistema Acadêmico

Projeto didático em Java (Spring Boot) para uma pequena parte de um sistema acadêmico.

## Atividade 03 - 2BIM: Persistência com JDBC e SQLite

Alunos e disciplinas agora são gravados em um banco **SQLite** usando **JDBC puro**
(`Connection`, `PreparedStatement`, `ResultSet`), sem JPA nem Hibernate.
Os dados continuam disponíveis depois de reiniciar a aplicação.

> Matrículas continuam em memória (`DadosEmMemoria`), como na Atividade 02.
> O enunciado pede persistência apenas de Aluno e Disciplina.

### Requisitos

- JDK 17 ou superior
- Maven (ou o Maven embutido do IntelliJ)

### Preparar o banco

O banco fica em `database/sistema-academico.db` (caminho configurado em
`src/main/resources/application.properties`, propriedade `app.database.url`).
Esse arquivo não vai para o Git.

**Opção 1 (automática):** basta iniciar a aplicação. Se as tabelas ainda não existirem,
a classe `InicializadorBanco` executa `database/schema.sql` e `database/dados-iniciais.sql`
via JDBC uma única vez. Nas execuções seguintes o banco é apenas aberto, sem apagar nada.

**Opção 2 (manual, com o `sqlite3` instalado):** dentro de `projects/academico`:

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

**Para recomeçar do zero**, apague o arquivo `database/sistema-academico.db` e inicie a aplicação de novo.

### Executar

Sempre a partir da pasta `projects/academico` (os caminhos do banco são relativos a ela):

```bash
mvn spring-boot:run
```

No IntelliJ: execute `SistemaAcademicoApplication` (pacote `br.edu.sistemaacademico.api`) e, em
*Run > Edit Configurations*, defina **Working directory** como a pasta `projects/academico`.

A API sobe em `http://localhost:8080`.

### Endpoints

| Método e rota | Sucesso | Erros |
| --- | --- | --- |
| `POST /api/alunos` | 201 + cabeçalho `Location` | 400 dados inválidos · 409 RA já cadastrado |
| `GET /api/alunos/{ra}` | 200 | 404 não encontrado |
| `POST /api/disciplinas` | 201 + cabeçalho `Location` | 400 dados inválidos · 409 código já cadastrado |
| `GET /api/disciplinas/{codigo}` | 200 | 404 não encontrada |

Erros retornam JSON no formato `{"mensagem": "..."}`.

Exemplos de corpo:

```json
{ "identificadorAcademico": "RA2026100", "nome": "Maria Souza", "email": "maria.souza@email.com" }
```

```json
{ "codigo": "ED", "nome": "Estrutura de Dados", "cargaHoraria": 60 }
```

Dados iniciais: alunos `RA2026001` e `RA2026002`; disciplinas `POO` e `BD`.

### Validação com o Postman

1. Apague `database/sistema-academico.db` (se existir) e inicie a aplicação.
2. No Postman, **Import** → `postman/sistema-academico-atividade03.postman_collection.json`.
3. Rode a pasta **1 - Cadastros e consultas**: cadastro, consulta, inexistente (404), duplicado (409) e inválido (400).
4. **Encerre** a aplicação e **inicie de novo**.
5. Rode a pasta **2 - Após reiniciar a aplicação**: o aluno `RA2026100` e a disciplina `ED` continuam lá (200).

Cada requisição tem testes automáticos que conferem o status e os campos da resposta.

### Testes

```bash
mvn test
```

`RepositoriosJdbcTest` testa os Repositories JDBC com um banco SQLite temporário
(não mexe no banco da aplicação), incluindo duplicidade, registro inexistente e
leitura por uma nova instância (simulando reinício).

### Organização

```
br.edu.sistemaacademico
├── Aluno, Disciplina, ...            domínio: regras de negócio
├── api
│   ├── AlunoController               HTTP de alunos
│   ├── DisciplinaController          HTTP de disciplinas
│   └── TratamentoErros               exceções -> status HTTP (400, 409, 500)
└── repository
    ├── AlunoRepository               interface: O QUE se faz com os dados
    ├── DisciplinaRepository          interface
    ├── RegistroDuplicadoException
    ├── PersistenciaException
    └── jdbc
        ├── AlunoRepositoryJdbc       COMO se faz: SQL + JDBC
        ├── DisciplinaRepositoryJdbc
        ├── ConexaoBanco              abre Connection pelo DriverManager
        └── InicializadorBanco        cria as tabelas na primeira execução
```

Fluxo de um cadastro:

```
requisição HTTP (JSON)
      ↓
AlunoController         converte o JSON em Aluno (o construtor valida)
      ↓
AlunoRepository         interface
      ↓
AlunoRepositoryJdbc     Connection → PreparedStatement (INSERT com ?)
      ↓
SQLite                  database/sistema-academico.db
```

Na consulta, o caminho volta: `SELECT` → `ResultSet` → `new Aluno(...)` com os valores das
colunas → `AlunoResponse` → JSON.

### Perguntas

**1. Onde estão os comandos SQL da aplicação?**
Nas implementações JDBC dos Repositories, em `repository/jdbc`: `AlunoRepositoryJdbc` e
`DisciplinaRepositoryJdbc` (constantes `SQL_INSERIR` e `SQL_BUSCAR_...`). A criação das tabelas
está em `database/schema.sql`, e a carga inicial em `database/dados-iniciais.sql`. Controllers e
classes do domínio não têm SQL.

**2. Por que o Controller não deve executar SQL diretamente?**
Porque a responsabilidade dele é só HTTP: receber a requisição, chamar quem faz o trabalho e
devolver status e JSON. Se o SQL ficasse nele, a mesma classe misturaria protocolo, regras e
banco, ficando difícil de ler, de testar e de reaproveitar (outra tela ou serviço que precisasse
buscar um aluno teria que copiar o SQL). Trocar o banco também obrigaria a mexer nos Controllers.

**3. Qual é o objetivo da interface de Repository?**
Separar *o que* se faz com os dados (`salvar`, `buscarPorRa`) de *como* isso é feito (SQL,
JDBC, SQLite). O Controller depende só da interface; o Spring injeta a implementação
`AlunoRepositoryJdbc`. Assim a implementação pode mudar sem que quem usa precise mudar.

**4. O que precisaria mudar para substituir SQLite por outra tecnologia de persistência?**
- Outro banco relacional (PostgreSQL, MySQL): trocar o driver no `pom.xml`, a URL em
  `application.properties` e ajustar o SQL que for específico do SQLite (o `schema.sql`, a
  consulta à `sqlite_master` no `InicializadorBanco` e a detecção de chave duplicada em
  `ErrosSqlite`).
- Tecnologia diferente (arquivo, memória, JPA, API externa): criar uma nova classe que implemente
  `AlunoRepository` e `DisciplinaRepository` e usá-la no lugar das versões JDBC.

Nos dois casos, Controllers e domínio não mudam, porque dependem apenas das interfaces.
