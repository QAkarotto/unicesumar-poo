# Atividade Final 2BIM — Sistema Acadêmico Full Stack (Spring Boot + JPA + MySQL + ReactJS)

## Objetivo

Evoluir o Sistema Acadêmico da Atividade 03 (`Trabalhos/Trabalho 4`, JDBC + SQLite) para uma aplicação full stack que atenda ao enunciado da Atividade Final: backend em camadas com Spring Data JPA, banco MySQL modelado pelo grupo e frontend em ReactJS. O tema e as regras de negócio existentes são mantidos ao máximo; só é acrescentado o que o enunciado exige.

Integrantes: Kauã Morais e João Vitor Passos (o enunciado pede trio; o grupo confirma com o professor).

## Restrições

- Os integrantes são iniciantes: o frontend deve ser o mais simples possível, sem bibliotecas além do React.
- Java 26 (obrigatório no enunciado), instalado pelo IntelliJ.
- MySQL 8 (já instalado na máquina).
- Nenhuma senha real no repositório.
- Código sem comentários (preferência do usuário); as explicações ficam no README e no chat.
- O `Trabalho 4` não é alterado: ele serve de comparação JDBC × JPA na apresentação.

## Localização

- Nova pasta `Trabalhos/Trabalho 5`, criada a partir de uma cópia do `Trabalho 4`.
- Backend na raiz de `Trabalho 5` (mesmo layout Maven do `Trabalho 4`).
- Frontend em `Trabalho 5/frontend`.

## Backend

### Tecnologias

- Java 26.
- Spring Boot 4.x, na versão mais recente que suporte Java 26 (a versão exata é fixada no plano, depois de verificar a compatibilidade).
- `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, driver `mysql-connector-j`, `spring-boot-starter-test`.
- Saem do `pom.xml`: `sqlite-jdbc` e `spring-boot-starter-jdbc`.

### Camadas

```
controller/   AlunoController, DisciplinaController, OfertaController, MatriculaController
service/      AlunoService, DisciplinaService, OfertaService, MatriculaService
domain/       Aluno, Disciplina, Turma, PeriodoLetivo, OfertaDisciplina, Matricula, ResultadoAcademico
repository/   AlunoRepository, DisciplinaRepository, OfertaDisciplinaRepository, MatriculaRepository
dto/          records de entrada e saída
exception/    RecursoNaoEncontradoException, RegistroDuplicadoException, GlobalExceptionHandler
```

- **Controller**: rotas, corpo da requisição, status HTTP. Recebe e devolve DTOs. Não contém regra de negócio.
- **Service**: coordena o caso de uso (buscar entidades, chamar o método do domínio, salvar). Métodos que alteram dados são `@Transactional`. Lança `RecursoNaoEncontradoException` e `RegistroDuplicadoException`.
- **Domain / Entity**: as classes atuais recebem anotações JPA e mantêm as regras.
- **Repository**: interfaces que estendem `JpaRepository`, sem SQL escrito à mão. `AlunoRepository` tem também `existsByEmail(String email)`, usado pelo `AlunoService` para responder 409 quando o e-mail já está cadastrado.

### Removido do `Trabalho 4`

`BancoDeDados`, `AlunoRepositoryJdbc`, `DisciplinaRepositoryJdbc`, `RepositorioException`, `DadosEmMemoria`, `SistemaAcademico` (classe `main` de demonstração), `Semestre` (vira um `enum`), os DTOs antigos (substituídos por records), `API.md` (o conteúdo atualizado vai para o README) e os `@JsonIgnore` (a API passa a devolver DTOs, então o JSON em loop não acontece mais).

### Modelo de domínio e mapeamento JPA

| Classe | Mapeamento | Observações |
|---|---|---|
| `Aluno` | `@Entity`, `@Id ra` | `@OneToMany(mappedBy = "aluno") matriculas`. Mantém o construtor atual com validações. |
| `Disciplina` | `@Entity`, `@Id codigo` | Validações atuais mantidas. |
| `Turma` | `@Entity`, `@Id codigo` | `@Embedded PeriodoLetivo`; `@OneToMany(mappedBy = "turma") ofertas`; método `ofertarDisciplina` mantido. |
| `PeriodoLetivo` | `@Embeddable` | `ano` (int) e `semestre` (`enum Semestre`: `PRIMEIRO`, `SEGUNDO`, gravado como texto). |
| `OfertaDisciplina` | `@Entity`, `@Id @GeneratedValue Long id` | `@ManyToOne turma`, `@ManyToOne disciplina`, `@OneToMany(mappedBy = "oferta") matriculas`; método `matricular` mantido. |
| `Matricula` | `@Entity`, `@Id @GeneratedValue Long id` | `@ManyToOne aluno`, `@ManyToOne oferta`, `@Enumerated(STRING) resultado`; método `concluir` com a nova regra. |

- Toda entidade ganha um construtor `protected` sem argumentos, exigido pelo JPA.
- `Aluno` e `Disciplina` implementam `equals`/`hashCode` pela chave (`ra` e `codigo`), para que as regras que comparam objetos funcionem com instâncias carregadas do banco.
- O id da matrícula deixa de ser o texto `MAT-{RA}-{SIGLA}`, que se repetiria quando um aluno reprovado cursa a mesma disciplina em outra turma.

### Regras de negócio

1. Uma disciplina não pode ser ofertada duas vezes na mesma turma (`Turma.ofertarDisciplina`, já existe).
2. Um aluno não pode ter duas matrículas na mesma oferta (`OfertaDisciplina.matricular`, já existe).
3. Um aluno aprovado em uma disciplina não pode se matricular nela de novo (`Aluno.jaFoiAprovadoEm`, já existe).
4. **Nova:** só uma matrícula com resultado `CURSANDO` pode ser concluída, e o resultado da conclusão deve ser `APROVADO` ou `REPROVADO` (`Matricula.concluir`).

### Banco MySQL

Arquivo `database/schema.sql`, escrito à mão:

```sql
CREATE TABLE IF NOT EXISTS aluno (
    ra VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS disciplina (
    codigo VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL CHECK (carga_horaria > 0)
);

CREATE TABLE IF NOT EXISTS turma (
    codigo VARCHAR(20) PRIMARY KEY,
    ano INT NOT NULL,
    semestre VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS oferta_disciplina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    turma_codigo VARCHAR(20) NOT NULL,
    disciplina_codigo VARCHAR(20) NOT NULL,
    FOREIGN KEY (turma_codigo) REFERENCES turma (codigo),
    FOREIGN KEY (disciplina_codigo) REFERENCES disciplina (codigo),
    UNIQUE (turma_codigo, disciplina_codigo)
);

CREATE TABLE IF NOT EXISTS matricula (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_ra VARCHAR(20) NOT NULL,
    oferta_id BIGINT NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    FOREIGN KEY (aluno_ra) REFERENCES aluno (ra),
    FOREIGN KEY (oferta_id) REFERENCES oferta_disciplina (id),
    UNIQUE (aluno_ra, oferta_id)
);
```

Arquivo `database/dados-iniciais.sql`, com `INSERT IGNORE`:
- alunos `RA2026001` (Paola Oliveira) e `RA2026002` (Bruno Santos);
- disciplinas `POO` e `BD`;
- turmas `ESOFT4S-NB` (2026, `PRIMEIRO`) e `ADSIS4S` (2026, `SEGUNDO`);
- ofertas: `ESOFT4S-NB` × `POO`, `ESOFT4S-NB` × `BD`, `ADSIS4S` × `POO`.

As ofertas recebem `id` fixo no `INSERT` (1, 2, 3) para que o `INSERT IGNORE` não as duplique a cada inicialização.

### Configuração (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_academico?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.sql.init.mode=always
spring.sql.init.schema-locations=file:database/schema.sql
spring.sql.init.data-locations=file:database/dados-iniciais.sql
spring.jpa.hibernate.ddl-auto=validate
```

- Os scripts rodam antes de o Hibernate validar as entidades (comportamento padrão do Spring Boot), então as tabelas já existem quando a validação acontece.

- O banco `sistema_academico` é criado sozinho na primeira conexão.
- `ddl-auto=validate`: o Hibernate não cria as tabelas; ele só confere se as entidades batem com o `schema.sql`. Isso mostra que a modelagem do banco foi feita pelo grupo, como o enunciado exige.
- Usuário e senha vêm das variáveis de ambiente `DB_USER` e `DB_PASSWORD`, com `root` como padrão. O README explica como configurar no IntelliJ.

### DTOs (records)

Entrada:
- `CriarAlunoRequest(String ra, String nome, String email)`
- `CriarDisciplinaRequest(String codigo, String nome, int cargaHoraria)`
- `CriarMatriculaRequest(String raAluno, Long idOferta)`
- `ConcluirMatriculaRequest(ResultadoAcademico resultado)`

Saída:
- `AlunoResponse(String ra, String nome, String email)`
- `DisciplinaResponse(String codigo, String nome, int cargaHoraria)`
- `OfertaResponse(Long id, String turma, int ano, String semestre, String disciplina, String nomeDisciplina)`
- `MatriculaResponse(Long id, String raAluno, String nomeAluno, Long idOferta, String turma, String disciplina, String resultado)`

Cada response tem um método estático `de(Entidade)` que faz a conversão.

### Endpoints

| Método | URL | Sucesso | Erros |
|---|---|---|---|
| POST | `/api/alunos` | 201 + `AlunoResponse` | 400 dado inválido, 409 RA ou e-mail já existe |
| GET | `/api/alunos` | 200 + lista | — |
| GET | `/api/alunos/{ra}` | 200 + `AlunoResponse` | 404 |
| POST | `/api/disciplinas` | 201 + `DisciplinaResponse` | 400, 409 código já existe |
| GET | `/api/disciplinas` | 200 + lista | — |
| GET | `/api/disciplinas/{codigo}` | 200 + `DisciplinaResponse` | 404 |
| GET | `/api/ofertas` | 200 + lista | — |
| POST | `/api/matriculas` | 201 + `MatriculaResponse` | 400, 404 aluno ou oferta não existe, 409 regra 2 ou 3 |
| GET | `/api/matriculas` | 200 + lista | — |
| GET | `/api/matriculas/{id}` | 200 + `MatriculaResponse` | 404 |
| PATCH | `/api/matriculas/{id}/conclusao` | 200 + `MatriculaResponse` | 400 resultado inválido, 404, 409 regra 4 |

### Tratamento de erros (`GlobalExceptionHandler`, `@RestControllerAdvice`)

Todas as respostas de erro têm o corpo `{"mensagem": "..."}`.

| Exceção | Status |
|---|---|
| `IllegalArgumentException` (validação do domínio, como nome vazio ou RA inválido) | 400 |
| `HttpMessageNotReadableException` (JSON malformado ou valor de enum inválido) | 400 |
| `RecursoNaoEncontradoException` | 404 |
| `RegistroDuplicadoException` | 409 |
| `IllegalStateException` (regras 2, 3 e 4) | 409 |

Para que a regra 2 responda 409, a exceção de `OfertaDisciplina.matricular` para matrícula duplicada passa de `IllegalArgumentException` para `IllegalStateException`, e o teste correspondente é ajustado.

## Frontend

### Tecnologias

Vite + React em JavaScript (sem TypeScript). Nenhuma dependência além de `react` e `react-dom`. Sem React Router, sem biblioteca de estado, sem framework de CSS, sem Axios.

### Arquivos

```
frontend/
├── package.json
├── vite.config.js     proxy de /api para http://localhost:8080
├── index.html
└── src/
    ├── main.jsx
    ├── App.jsx        três botões (Alunos, Disciplinas, Matrículas) e a tela escolhida, via useState
    ├── api.js         função que chama fetch, converte JSON e lança Error com a "mensagem" da API
    ├── Alunos.jsx
    ├── Disciplinas.jsx
    ├── Matriculas.jsx
    └── index.css      estilo mínimo
```

### Padrão de cada tela

1. `useState` para a lista, os campos do formulário e a mensagem de erro.
2. `useEffect` carrega a lista com `GET` quando a tela abre.
3. Ao enviar o formulário: `POST`; se der certo, limpa o formulário e recarrega a lista; se der erro, mostra a mensagem da API em vermelho.

### Telas

- **Alunos**: formulário (RA, nome, e-mail) e tabela com os alunos.
- **Disciplinas**: formulário (código, nome, carga horária) e tabela com as disciplinas.
- **Matrículas**: formulário com dois `<select>` (aluno e oferta, carregados de `/api/alunos` e `/api/ofertas`) e tabela com as matrículas. Cada matrícula `CURSANDO` tem os botões **Aprovar** e **Reprovar**, que chamam o `PATCH` de conclusão e recarregam a lista.

## Testes

JUnit 5, só com as classes do domínio, sem banco e sem Spring:
- os testes atuais de `SistemaAcademicoTest`, ajustados às mudanças (enum `Semestre`, exceção da regra 2);
- novos testes da regra 4: concluir uma matrícula `CURSANDO` funciona; concluir uma matrícula já concluída lança `IllegalStateException`; concluir com `CURSANDO` como resultado lança `IllegalArgumentException`.

Rodados com `mvn test`.

## README (`Trabalho 5/README.md`)

- integrantes;
- tema e objetivo;
- tecnologias;
- banco escolhido (MySQL) e por quê;
- como configurar usuário e senha (variáveis `DB_USER` e `DB_PASSWORD` na configuração de execução do IntelliJ);
- como o banco e as tabelas são criados (`createDatabaseIfNotExist` e `schema.sql`), e como rodar o `schema.sql` manualmente, se preferir;
- como executar o backend (IntelliJ, *Working directory* = `Trabalho 5`) e o frontend (`npm install`, depois `npm run dev`);
- tabela de endpoints;
- regras de negócio;
- tabela "JDBC × JPA", com o que o JPA passou a abstrair (conexão, `PreparedStatement`, `ResultSet`, conversão linha → objeto, SQL de `INSERT`/`SELECT`).

## Fora do escopo

Telas de Turma e Oferta (os dados vêm do `dados-iniciais.sql`), login e autenticação, edição e exclusão de aluno ou disciplina, paginação, testes de frontend e testes de integração com banco.

## Critérios de sucesso

- `mvn test` passa.
- Com o MySQL rodando, o backend sobe sem erro e cria o banco e as tabelas.
- Pelo frontend dá para cadastrar e listar alunos e disciplinas, matricular um aluno, aprovar ou reprovar a matrícula e ver as mensagens de erro das regras.
- Os dados continuam depois de reiniciar o backend.
- Nenhuma senha real nos arquivos commitados.
