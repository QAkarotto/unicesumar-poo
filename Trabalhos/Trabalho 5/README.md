# Sistema Acadêmico — Atividade Final 2BIM

Aplicação full stack com **Spring Boot + Spring Data JPA + MySQL** no backend e **ReactJS** no frontend.

## Integrantes

- Kauã Morais
- João Vitor Passos

## Tema e objetivo

Gerenciar alunos, disciplinas e matrículas de uma instituição de ensino. Uma disciplina é ofertada em uma turma (oferta de disciplina) e o aluno se matricula nessa oferta. Depois, a matrícula é concluída como aprovada ou reprovada.

O projeto é a evolução do Sistema Acadêmico das atividades anteriores. A Atividade 03, feita com JDBC e SQLite, continua na pasta `Trabalho 4`, para comparação.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 26 |
| Backend | Spring Boot 4.1.2 (Spring Web MVC) |
| Persistência | Spring Data JPA / Hibernate |
| Banco | MySQL 8 |
| Testes | JUnit 5 |
| Frontend | ReactJS 19 + Vite, em JavaScript |

## Banco escolhido: MySQL

Escolhemos o MySQL porque é o banco recomendado pela atividade, já estava instalado na nossa máquina e o MySQL Workbench facilita ver as tabelas e rodar comandos SQL.

## Modelagem

```
ALUNO 1 ──── N MATRICULA N ──── 1 OFERTA_DISCIPLINA N ──── 1 DISCIPLINA
                                        │
                                        N
                                        │
                                        1
                                      TURMA
```

| Tabela | Chave primária | Chaves estrangeiras | Restrições |
|---|---|---|---|
| `aluno` | `ra` | — | `email` único |
| `disciplina` | `codigo` | — | `carga_horaria > 0` |
| `turma` | `codigo` | — | `ano` e `semestre` obrigatórios |
| `oferta_disciplina` | `id` | `turma_codigo` → `turma`, `disciplina_codigo` → `disciplina` | a mesma disciplina não se repete na mesma turma |
| `matricula` | `id` | `aluno_ra` → `aluno`, `oferta_id` → `oferta_disciplina` | o mesmo aluno não se repete na mesma oferta |

O script completo está em [`database/schema.sql`](database/schema.sql), e os dados iniciais em [`database/dados-iniciais.sql`](database/dados-iniciais.sql).

### Relacionamento entre objetos, JPA e banco

| Relacionamento no Java | Mapeamento JPA | No banco |
|---|---|---|
| `Matricula` tem um `Aluno` | `@ManyToOne @JoinColumn(name = "aluno_ra")` | chave estrangeira `matricula.aluno_ra` |
| `Matricula` tem uma `OfertaDisciplina` | `@ManyToOne @JoinColumn(name = "oferta_id")` | chave estrangeira `matricula.oferta_id` |
| `OfertaDisciplina` tem uma `Turma` e uma `Disciplina` | `@ManyToOne` | chaves estrangeiras `turma_codigo` e `disciplina_codigo` |
| `Aluno` tem várias `Matricula` | `@OneToMany(mappedBy = "aluno")` | nenhuma coluna nova (lado inverso) |
| `Turma` tem um `PeriodoLetivo` | `@Embedded` / `@Embeddable` | colunas `ano` e `semestre` na própria tabela `turma` |

## Como configurar o banco

### 1. Criar um usuário para o projeto

No **MySQL Workbench**, conectado como `root`, rode os comandos abaixo (botão ⚡). Troque `sua_senha` por uma senha só para este projeto:

```sql
CREATE USER IF NOT EXISTS 'sistema_academico'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON sistema_academico.* TO 'sistema_academico'@'localhost';
```

### 2. Informar usuário e senha ao Spring Boot

A senha **não fica no código**. O `application.properties` lê as variáveis de ambiente `DB_USER` e `DB_PASSWORD`, e usa `root`/`root` quando elas não existem.

No IntelliJ: **Run → Edit Configurations → SistemaAcademicoApplication → Environment variables**:

```
DB_USER=sistema_academico;DB_PASSWORD=sua_senha
```

### 3. Criação do banco e das tabelas

Não é preciso criar nada à mão. Ao iniciar a aplicação:

1. a URL de conexão tem `createDatabaseIfNotExist=true`, então o banco `sistema_academico` é criado se ainda não existir;
2. o Spring roda o `database/schema.sql` (`CREATE TABLE IF NOT EXISTS`) e o `database/dados-iniciais.sql` (`INSERT IGNORE`). Por isso os dados não são apagados nem duplicados a cada reinício;
3. o Hibernate, com `ddl-auto=validate`, **não cria tabelas**: ele só confere se as entidades Java batem com o `schema.sql`.

**Para rodar o `schema.sql` manualmente**, se preferir: no Workbench, `CREATE DATABASE sistema_academico;`, depois `USE sistema_academico;`, depois *File → Open SQL Script* → `database/schema.sql` → ⚡.

## Como executar o backend

1. Abra a pasta `Trabalho 5` no IntelliJ como projeto Maven.
2. Em *File → Project Structure → SDK*, escolha o **JDK 26** (se não tiver: *Add SDK → Download JDK*).
3. Em *Run → Edit Configurations*, confira:
   - **Working directory** = a pasta `Trabalho 5`, porque o Spring procura a pasta `database` a partir dela;
   - as **variáveis de ambiente** do passo 2 acima.
4. Rode `SistemaAcademicoApplication`. A API fica em `http://localhost:8080`.

Para rodar os testes: `mvn test`, ou clique com o botão direito em `src/test` → *Run Tests*.

## Como executar o frontend

Com o backend rodando, em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

Abra `http://localhost:5173`. O Vite repassa as chamadas `/api/...` para o `localhost:8080`, então não é preciso configurar CORS.

## Funcionalidades

- **Alunos:** cadastrar e listar.
- **Disciplinas:** cadastrar e listar.
- **Matrículas:** matricular um aluno numa oferta, listar, e **aprovar ou reprovar** uma matrícula em andamento.
- Mensagens de erro da API aparecem na tela, em vermelho.

## Endpoints

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/alunos` | 201 | 400 dado inválido, 409 RA ou e-mail já existe |
| GET | `/api/alunos` | 200 | — |
| GET | `/api/alunos/{ra}` | 200 | 404 |
| POST | `/api/disciplinas` | 201 | 400, 409 código já existe |
| GET | `/api/disciplinas` | 200 | — |
| GET | `/api/disciplinas/{codigo}` | 200 | 404 |
| GET | `/api/ofertas` | 200 | — |
| POST | `/api/matriculas` | 201 | 400, 404 aluno ou oferta não existe, 409 regra violada |
| GET | `/api/matriculas` | 200 | — |
| GET | `/api/matriculas/{id}` | 200 | 400 id inválido, 404 |
| PATCH | `/api/matriculas/{id}/conclusao` | 200 | 400 resultado inválido, 404, 409 matrícula já concluída |

Exemplos de corpo:

```json
POST /api/alunos          { "ra": "RA2026010", "nome": "Maria Souza", "email": "maria@email.com" }
POST /api/disciplinas     { "codigo": "LP", "nome": "Lógica de Programação", "cargaHoraria": 60 }
POST /api/matriculas      { "raAluno": "RA2026001", "idOferta": 1 }
PATCH /api/matriculas/1/conclusao   { "resultado": "APROVADO" }
```

Toda resposta de erro tem o formato `{ "mensagem": "..." }`.

## Regras de negócio

As regras ficam nas classes do domínio, não nos controllers:

1. Uma disciplina não pode ser ofertada duas vezes na mesma turma (`Turma.ofertarDisciplina`).
2. Um aluno não pode ter duas matrículas na mesma oferta (`OfertaDisciplina.matricular`).
3. Um aluno aprovado numa disciplina não pode se matricular nela de novo, mesmo em outra turma (`Aluno.jaFoiAprovadoEm`).
4. Só uma matrícula `CURSANDO` pode ser concluída, e só como `APROVADO` ou `REPROVADO` (`Matricula.concluir`).

Os testes dessas regras estão em `src/test/java/.../domain/sistemaacademicoTest.java`.

## Camadas do backend

```
ReactJS → Controller → Service → Domain / Entity → Repository → JPA / Hibernate → MySQL
```

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Controller | `controller` | Rotas HTTP, corpo da requisição, status da resposta |
| Service | `service` | Coordena o caso de uso: busca, chama o domínio, salva |
| Domain / Entity | `domain` | Dados e regras de negócio, mapeados com JPA |
| Repository | `repository` | Acesso ao banco com `JpaRepository`, sem SQL escrito à mão |
| DTO | `dto` | Formato do JSON que entra e sai da API |
| Exception | `exception` | `GlobalExceptionHandler` transforma erros em 400, 404 ou 409 |

## JDBC (Atividade 03) × JPA (Atividade Final)

| Tarefa | JDBC (`Trabalho 4`) | JPA (`Trabalho 5`) |
|---|---|---|
| Conexão | `BancoDeDados.abrirConexao()` em cada operação | `DataSource` gerenciado pelo Spring |
| Comandos SQL | `INSERT` e `SELECT` escritos à mão no repositório | Métodos prontos do `JpaRepository` (`save`, `findById`, `findAll`, `existsById`) |
| Parâmetros | `PreparedStatement` + `setString(1, ...)` | Gerados pelo Hibernate |
| Linha → objeto | `ResultSet` + método `paraAluno` | O Hibernate monta a entidade sozinho |
| Relacionamentos | Só por código (texto) | `@ManyToOne` / `@OneToMany`, com chave estrangeira no banco |
| Transação | Cada comando separado | `@Transactional` no service |
| Erros de banco | `catch (SQLException)` + código de erro do SQLite | Exceções do domínio e do service tratadas no `GlobalExceptionHandler` |
