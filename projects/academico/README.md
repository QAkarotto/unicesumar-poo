# Sistema Acadêmico

Projeto didático em Java, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`. Desde a Atividade 03, os dados de Aluno e Disciplina são lidos e persistidos no SQLite, enquanto Ofertas e Matrículas permanecem em memória.

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

No Postman, use **Import** e selecione `postman/sistema-academico.postman_collection.json`. A variável `baseUrl` aponta para `http://localhost:8080`.

## Atividade 03 - 2BIM: Persistência com JDBC e SQLite

Nesta atividade, a persistência de **Aluno** e **Disciplina** foi implementada utilizando JDBC puro e SQLite, sem o uso de JPA, Hibernate ou outros ORMs.

As interfaces `AlunoRepository` e `DisciplinaRepository` definem o contrato de acesso a dados e ficam no pacote `br.edu.sistemaacademico.persistencia`. As implementações concretas `AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc` utilizam `Connection`, `PreparedStatement` e `ResultSet` para executar comandos SQL e converter registros do banco em instâncias do domínio. O componente `ConexaoBanco` gerencia a obtenção de conexões a partir do `DriverManager` usando a URL configurada em `application.properties`.

| Método e rota | Resultado |
| --- | --- |
| `POST /api/alunos` | 201 Created e cabeçalho `Location`; RA duplicado retorna 409; dados inválidos retornam 400 |
| `GET /api/alunos/{registroAcademico}` | 200 OK com dados do aluno, ou 404 se não encontrado |
| `POST /api/disciplinas` | 201 Created e cabeçalho `Location`; código duplicado retorna 409; dados inválidos retornam 400 |
| `GET /api/disciplinas/{codigo}` | 200 OK com dados da disciplina, ou 404 se não encontrada |

### Exemplos de Requisição

**Cadastro de Aluno (`POST /api/alunos`)**:
```json
{
  "registroAcademico": "RA2026010",
  "nome": "Carla Mendes",
  "email": "carla.mendes@example.com"
}
```

**Cadastro de Disciplina (`POST /api/disciplinas`)**:
```json
{
  "codigo": "ED",
  "nome": "Estrutura de Dados",
  "cargaHoraria": 60
}
```

### Preparação do Banco SQLite

O driver `org.xerial:sqlite-jdbc:3.53.4.0` está configurado no Maven.

- Script de estrutura: `database/schema.sql`.
- Carga de dados iniciais: `database/dados-iniciais.sql`.
- Banco de dados local: `database/sistema-academico.db` (ignorado pelo Git).
- Configuração de URL em `src/main/resources/application.properties`: `app.database.url=jdbc:sqlite:database/sistema-academico.db`.

A partir do diretório `projects/academico`, execute no terminal (PowerShell, CMD ou Bash) para criar e popular o banco:

```bash
sqlite3 database/sistema-academico.db ".read database/schema.sql"
sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
```

*Nota: No Windows, caso não possua o `sqlite3` instalado, pode-se instalar via `winget install SQLite.SQLite`.*

### Validação da Persistência com Postman

A collection com os testes exigidos encontra-se em `postman/atividade-03-persistencia.postman_collection.json`.

1. Inicialize o banco executando os scripts SQL acima.
2. Inicie a API com `mvn spring-boot:run` (ou execute a classe `AplicacaoAcademica` no IntelliJ).
3. Importe a collection no Postman e execute a pasta **1. Cadastro e consulta** (contém cadastro de aluno/disciplina, consultas, testes de recurso inexistente 404 e duplicidade 409).
4. Pare a aplicação (Ctrl+C no terminal ou Stop na IDE).
5. Inicie a aplicação novamente.
6. Execute a pasta **2. Depois de reiniciar a API**. As consultas retornarão 200 OK com os dados criados anteriormente, comprovando a persistência no SQLite.

### Respostas às Perguntas Teóricas

#### 1. Onde estão os comandos SQL da aplicação?
Os comandos SQL estão isolados exclusivamente nas classes de implementação dos Repositories JDBC (`AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc`), dentro do pacote `br.edu.sistemaacademico.persistencia`. Nem os Controllers, nem as classes de domínio conhecem ou contêm código SQL.

#### 2. Por que o Controller não deve executar SQL diretamente?
O Controller pertence à camada de apresentação/comunicação HTTP; sua única responsabilidade é receber requisições HTTP, delegar as ações para o domínio/repositories e formatar as respostas com os códigos de status adequados. Se o Controller executasse comandos SQL diretamente, haveria forte acoplamento entre a camada web e o mecanismo de persistência, violando o Princípio da Responsabilidade Única (SRP), dificultando testes unitários e impedindo a reutilização ou evolução do armazenamento de dados.

#### 3. Qual é o objetivo da interface de Repository?
O objetivo da interface de Repository é definir um contrato abstrato para as operações de recuperação e salvamento de dados, desacoplando o restante da aplicação dos detalhes tecnológicos de como esses dados são de fato gravados ou consultados. Isso permite trocar o mecanismo de persistência (por exemplo, de SQLite para PostgreSQL ou memória) sem alterar nenhuma linha dos Controllers ou das regras de negócio.

#### 4. O que precisaria mudar para substituir SQLite por outra tecnologia de persistência (ex: PostgreSQL, MySQL ou JPA)?
Graças ao padrão Repository e à injeção de dependências:
- A interface `AlunoRepository`, a classe `Aluno` e o `AcademicoController` permaneceriam **inalterados**.
- Seria necessário atualizar as dependências no `pom.xml` (trocar o driver JDBC).
- Alterar as propriedades de conexão no `application.properties` (URL JDBC, usuário, senha).
- Criar ou ajustar uma nova classe de implementação do Repository (ou ajustar a existente para os dialetos e peculiaridades do novo SGBD/tecnologia) e registrá-la no Spring.

## Testes e cobertura

Execute a partir de `projects/academico`:

```bash
mvn clean test
```

Executa os testes de domínio, os testes dos repositórios JDBC e os testes HTTP de integração.

Para verificar a cobertura mínima de 80% do domínio (JaCoCo):

```bash
mvn verify -DskipTests
```

O relatório HTML é gerado em `target/site/jacoco/index.html`.
