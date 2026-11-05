# Atividade Final — Sistema Acadêmico Full Stack — Plano de Implementação

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Criar `Trabalhos/Trabalho 5`: o Sistema Acadêmico com Spring Boot + Spring Data JPA + MySQL e um frontend ReactJS mínimo.

**Architecture:** Cópia do `Trabalho 4`, com as classes de domínio virando entidades JPA (as regras continuam nelas), uma camada Service nova, repositórios `JpaRepository`, DTOs em records e um `GlobalExceptionHandler`. O frontend é Vite + React em JavaScript, com três telas trocadas por `useState`, e fala com a API por um proxy do Vite.

**Tech Stack:** Java 26, Spring Boot 4.1.2 (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-test`), `com.mysql:mysql-connector-j` (versão gerenciada pelo Spring Boot), MySQL 8, JUnit 5, Vite, React, Node 24.

**Spec:** `docs/superpowers/specs/2026-11-05-atividade-final-fullstack-design.md`

## Global Constraints

- Nenhum comentário no código (Java, SQL, JS, CSS). Explicações vão no README.
- O conteúdo de `Trabalhos/Trabalho 4` não é alterado. A mudança não commitada em `Trabalho 4/.../AlunoController.java` (só espaço em branco) fica como está, fora dos commits.
- Branch de trabalho: `feature/ads4s/kaua-atividade-final`, criada a partir da branch atual.
- Pacote base: `br.edu.sistemaacademico`. Classe principal: `SistemaAcademicoApplication`.
- Frontend: só `react` e `react-dom` como dependências de execução; JavaScript (`.jsx`), sem TypeScript, sem React Router, sem Axios, sem biblioteca de CSS.
- Corpo de toda resposta de erro da API: `{"mensagem": "..."}`.
- Nenhuma senha real em arquivo commitado. `application.properties` usa `${DB_USER:root}` e `${DB_PASSWORD:root}`.
- Mensagens de commit em português, terminando com `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`.
- Maven: usar o do IntelliJ (`/c/Program Files/JetBrains/IntelliJ IDEA 2026.2.3/plugins/maven-plugin/lib/maven3/bin/mvn`), com `JAVA_HOME` apontando para o JDK 26 em `~/.jdks`.
- Subir o servidor (Tomcat ou Vite) e chamar com `curl` exige rodar o comando fora do sandbox: dentro dele, a porta local não abre.

## Review Focus

1. `POST /api/matriculas` sem `raAluno` ou sem `idOferta` → 400 com mensagem (não 500). Teste: Task 3, Step 5, caso 7.
2. `PATCH /api/matriculas/{id}/conclusao` com `{"resultado":"CURSANDO"}`, `{"resultado":"XYZ"}` ou corpo vazio → 400. Teste: Task 3, Step 5, casos 11 e 12.
3. `GET /api/matriculas/abc` (id não numérico) → 400, não 500. Teste: Task 3, Step 5, caso 13.
4. `POST /api/alunos` com e-mail de outro aluno → 409, não 500 por violação da restrição `UNIQUE`. Teste: Task 3, Step 5, caso 3.
5. Frontend com o backend desligado → a tela mostra "Não foi possível conectar ao servidor." em vez de quebrar ou ficar em branco. Teste: Task 4, Step 6.

---

### Task 1: Criar o Trabalho 5 e transformar o domínio em entidades JPA

**Files:**
- Create: `Trabalhos/Trabalho 5/` (cópia de `Trabalhos/Trabalho 4/`, sem `target/`, `database/*.db` e `.idea/`)
- Delete em `Trabalho 5`: `API.md`, `readme.md`, `src/main/java/br/edu/sistemaacademico/repository/` (pasta inteira), `src/main/java/br/edu/sistemaacademico/controller/` (pasta inteira, refeita na Task 3), `src/main/java/br/edu/sistemaacademico/dto/` (pasta inteira, refeita na Task 3), `domain/DadosEmMemoria.java`, `domain/SistemaAcademico.java`, `database/schema.sql`, `database/dados-iniciais.sql` (refeitos na Task 2), `src/main/resources/application.properties` (refeito na Task 2)
- Modify: `Trabalho 5/pom.xml`
- Modify: `Trabalho 5/src/main/java/br/edu/sistemaacademico/domain/{Aluno,Disciplina,Turma,PeriodoLetivo,Semestre,OfertaDisciplina,Matricula}.java`
- Test: `Trabalho 5/src/test/java/br/edu/sistemaacademico/domain/sistemaacademicoTest.java`
- Create: `docs/superpowers/specs/...` e `docs/superpowers/plans/...` entram no primeiro commit

**Interfaces:**
- Produces (usado pelas Tasks 2 e 3):
  - `Aluno(String ra, String nome, String email)` (mantém a troca automática de ordem atual), `getRa()`, `getNome()`, `getEmail()`, `getMatriculas()`, `boolean jaFoiAprovadoEm(Disciplina)`
  - `Disciplina(String codigo, String nome, int cargaHoraria)`, `getCodigo()`, `getNome()`, `getCargaHoraria()`
  - `enum Semestre { PRIMEIRO, SEGUNDO }`
  - `PeriodoLetivo(int ano, Semestre semestre)`, `getAno()`, `getSemestre()`
  - `Turma(String codigo, PeriodoLetivo periodoLetivo)`, `getCodigo()`, `getPeriodoLetivo()`, `getOfertas()`, `OfertaDisciplina ofertarDisciplina(Disciplina)`
  - `OfertaDisciplina`: `Long getId()`, `getTurma()`, `getDisciplina()`, `getMatriculas()`, `Matricula matricular(Aluno)`
  - `Matricula(Aluno aluno, OfertaDisciplina oferta)`, `Long getId()`, `getAluno()`, `getOferta()`, `getResultado()`, `void concluir(ResultadoAcademico)`

- [ ] **Step 1: Pré-requisitos**

Run: `ls ~/.jdks` → deve listar um JDK 26. Se não houver, pare e peça ao usuário para instalar pelo IntelliJ (*File → Project Structure → SDK → Add SDK → Download JDK*, versão 26).
Run: `git switch -c feature/ads4s/kaua-atividade-final`
Expected: `Switched to a new branch 'feature/ads4s/kaua-atividade-final'`

- [ ] **Step 2: Copiar e limpar**

Copie `Trabalho 4` para `Trabalho 5`, sem `target/`, `.idea/` e `*.db`, e apague os arquivos listados em **Files**.

- [ ] **Step 3: Atualizar o `pom.xml`**

Parent `spring-boot-starter-parent` `4.1.2`; `<java.version>26</java.version>`; dependências: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `com.mysql:mysql-connector-j` (`<scope>runtime</scope>`), `spring-boot-starter-test` (`<scope>test</scope>`). Remova `spring-boot-starter-web`, `spring-boot-starter-jdbc` e `sqlite-jdbc`.

- [ ] **Step 4: Escrever os testes novos e ajustar o da regra 2**

No `sistemaacademicoTest.java`:
- o teste de matrícula duplicada na mesma oferta passa a esperar `IllegalStateException` (mesma mensagem que contém "já está matriculado");
- acrescente:

```java
@Test
@DisplayName("Deve concluir matrícula em andamento")
void deveConcluirMatriculaEmAndamento() {
    Matricula matricula = turmaA.ofertarDisciplina(disciplinaPoo).matricular(aluno);
    matricula.concluir(ResultadoAcademico.APROVADO);
    assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
}

@Test
@DisplayName("Não deve concluir matrícula já concluída")
void naoDeveConcluirMatriculaJaConcluida() {
    Matricula matricula = turmaA.ofertarDisciplina(disciplinaPoo).matricular(aluno);
    matricula.concluir(ResultadoAcademico.REPROVADO);
    IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> matricula.concluir(ResultadoAcademico.APROVADO));
    assertEquals("Somente matrículas em andamento podem ser concluídas.", e.getMessage());
}

@Test
@DisplayName("Não deve concluir matrícula com resultado CURSANDO")
void naoDeveConcluirComResultadoCursando() {
    Matricula matricula = turmaA.ofertarDisciplina(disciplinaPoo).matricular(aluno);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> matricula.concluir(ResultadoAcademico.CURSANDO));
    assertEquals("O resultado da conclusão deve ser APROVADO ou REPROVADO.", e.getMessage());
}
```

- [ ] **Step 5: Rodar os testes e ver falhar**

Run: `JAVA_HOME=~/.jdks/<jdk-26> "$MVN" -q test` dentro de `Trabalho 5`
Expected: falha de compilação ou nos 3 testes novos e no da regra 2.

- [ ] **Step 6: Transformar o domínio em entidades**

- `Semestre` vira `public enum Semestre { PRIMEIRO, SEGUNDO }`.
- `PeriodoLetivo`: `@Embeddable`; `int ano`; `@Enumerated(EnumType.STRING) Semestre semestre`; construtor `protected` vazio.
- `Aluno`: `@Entity`; `@Id String ra`; `@OneToMany(mappedBy = "aluno") List<Matricula> matriculas`; `equals`/`hashCode` por `ra`; remove o `@JsonIgnore`.
- `Disciplina`: `@Entity`; o campo `sigla` passa a se chamar `codigo` (`@Id`); `@Column(name = "carga_horaria") int cargaHoraria`; remove `getSigla()`; `equals`/`hashCode` por `codigo`.
- `Turma`: `@Entity`; `@Id String codigo`; `@Embedded PeriodoLetivo periodoLetivo`; `@OneToMany(mappedBy = "turma") List<OfertaDisciplina> ofertas`; acrescenta `getPeriodoLetivo()`.
- `OfertaDisciplina`: `@Entity @Table(name = "oferta_disciplina")`; `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id`; `@ManyToOne @JoinColumn(name = "turma_codigo") Turma turma`; `@ManyToOne @JoinColumn(name = "disciplina_codigo") Disciplina disciplina`; `@OneToMany(mappedBy = "oferta") List<Matricula> matriculas`; remove `getCodigo()` e o id em texto; `matricular` lança `IllegalStateException` na matrícula duplicada e cria `new Matricula(aluno, this)`.
- `Matricula`: `@Entity`; `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id`; `@ManyToOne @JoinColumn(name = "aluno_ra") Aluno aluno`; `@ManyToOne @JoinColumn(name = "oferta_id") OfertaDisciplina oferta`; `@Enumerated(EnumType.STRING) ResultadoAcademico resultado`. `concluir(r)`: se `r` não for `APROVADO` nem `REPROVADO` (inclui `null`) → `IllegalArgumentException` com a mensagem do teste; se `resultado != CURSANDO` → `IllegalStateException` com a mensagem do teste.
- Todas as entidades ganham um construtor `protected` sem argumentos.

- [ ] **Step 7: Rodar os testes**

Run: `JAVA_HOME=~/.jdks/<jdk-26> "$MVN" -q test`
Expected: `BUILD SUCCESS`, todos os testes passando.

- [ ] **Step 8: Commit**

```bash
git add docs/superpowers "Trabalhos/Trabalho 5"
git commit -m "Atividade Final: Trabalho 5 com domínio mapeado em JPA e regra de conclusão de matrícula"
```

---

### Task 2: Banco MySQL, configuração e repositórios

**Files:**
- Create: `Trabalho 5/database/schema.sql`, `Trabalho 5/database/dados-iniciais.sql`
- Create: `Trabalho 5/src/main/resources/application.properties`
- Create: `Trabalho 5/src/main/java/br/edu/sistemaacademico/repository/{AlunoRepository,DisciplinaRepository,OfertaDisciplinaRepository,MatriculaRepository}.java`

**Interfaces:**
- Consumes: as entidades da Task 1.
- Produces:
  - `AlunoRepository extends JpaRepository<Aluno, String>` com `boolean existsByEmail(String email)`
  - `DisciplinaRepository extends JpaRepository<Disciplina, String>`
  - `OfertaDisciplinaRepository extends JpaRepository<OfertaDisciplina, Long>`
  - `MatriculaRepository extends JpaRepository<Matricula, Long>`

- [ ] **Step 1: Escrever o `schema.sql`**

Exatamente como a seção "Banco MySQL" do spec.

- [ ] **Step 2: Escrever o `dados-iniciais.sql`**

Com `INSERT IGNORE`: alunos `('RA2026001','Paola Oliveira','paola.oliveira@email.com')` e `('RA2026002','Bruno Santos','bruno.santos@email.com')`; disciplinas `('POO','Programação Orientada a Objetos',80)` e `('BD','Banco de Dados',80)`; turmas `('ESOFT4S-NB',2026,'PRIMEIRO')` e `('ADSIS4S',2026,'SEGUNDO')`; ofertas `(1,'ESOFT4S-NB','POO')`, `(2,'ESOFT4S-NB','BD')`, `(3,'ADSIS4S','POO')`.

- [ ] **Step 3: Escrever o `application.properties` e os repositórios**

O `application.properties` é o do spec (sem a linha `defer-datasource-initialization`), mais `spring.sql.init.encoding=UTF-8`.

- [ ] **Step 4: Preparar o MySQL para o teste**

Run (PowerShell): `Get-Service MySQL80` → `Running`. Se estiver parado, peça ao usuário para iniciá-lo.
Peça ao usuário para criar, no MySQL Workbench ou MySQL Shell, um usuário só de teste (é uma credencial local de desenvolvimento, não a senha dele):

```sql
CREATE USER IF NOT EXISTS 'sistema_academico'@'localhost' IDENTIFIED BY 'sistema123';
GRANT ALL PRIVILEGES ON sistema_academico.* TO 'sistema_academico'@'localhost';
```

Nunca peça nem digite a senha de `root` do usuário.

- [ ] **Step 5: Subir a aplicação e ver o Hibernate validar o schema**

Run (fora do sandbox, dentro de `Trabalho 5`): `"$MVN" -q package -DskipTests`, depois `DB_USER=sistema_academico DB_PASSWORD=sistema123 java -jar target/sistemaacademico-1.0-SNAPSHOT.jar --spring.main.web-application-type=none`
Expected: o log contém `Started SistemaAcademicoApplication` e nenhum `SchemaManagementException`; o processo termina com código 0.

- [ ] **Step 6: Rodar de novo para provar que os scripts não duplicam nada**

Repita o Step 5. Expected: sobe sem erro. Confira pelo MySQL Shell (ou peça ao usuário) que `SELECT COUNT(*) FROM oferta_disciplina` = 3.

- [ ] **Step 7: Commit**

```bash
git add "Trabalhos/Trabalho 5"
git commit -m "Atividade Final: schema MySQL, dados iniciais e repositórios JPA"
```

---

### Task 3: DTOs, services, controllers e tratamento de erros

**Files:**
- Create: `Trabalho 5/src/main/java/br/edu/sistemaacademico/dto/` — `CriarAlunoRequest`, `CriarDisciplinaRequest`, `CriarMatriculaRequest`, `ConcluirMatriculaRequest`, `AlunoResponse`, `DisciplinaResponse`, `OfertaResponse`, `MatriculaResponse`, `ErroResponse`
- Create: `.../exception/RecursoNaoEncontradoException.java`, `.../exception/RegistroDuplicadoException.java`, `.../exception/GlobalExceptionHandler.java`
- Create: `.../service/{AlunoService,DisciplinaService,OfertaService,MatriculaService}.java`
- Create: `.../controller/{AlunoController,DisciplinaController,OfertaController,MatriculaController}.java`

**Interfaces:**
- Consumes: os repositórios da Task 2 e as entidades da Task 1.
- Produces (o contrato que o frontend da Task 4 usa):
  - Records com os campos exatos do spec (seção "DTOs"); `ErroResponse(String mensagem)`.
  - `AlunoResponse.de(Aluno)`, `DisciplinaResponse.de(Disciplina)`, `OfertaResponse.de(OfertaDisciplina)`, `MatriculaResponse.de(Matricula)`. Em `OfertaResponse`: `turma` = código da turma, `semestre` = `name()` do enum, `disciplina` = código e `nomeDisciplina` = nome. Em `MatriculaResponse`: `turma` = código da turma, `disciplina` = nome da disciplina e `resultado` = `name()` do enum.
  - `RecursoNaoEncontradoException(String)` e `RegistroDuplicadoException(String)`, ambas `extends RuntimeException`.
  - `AlunoService`: `AlunoResponse cadastrar(CriarAlunoRequest)`, `List<AlunoResponse> listar()`, `AlunoResponse buscarPorRa(String)`
  - `DisciplinaService`: `DisciplinaResponse cadastrar(CriarDisciplinaRequest)`, `List<DisciplinaResponse> listar()`, `DisciplinaResponse buscarPorCodigo(String)`
  - `OfertaService`: `List<OfertaResponse> listar()`
  - `MatriculaService`: `MatriculaResponse matricular(CriarMatriculaRequest)`, `List<MatriculaResponse> listar()`, `MatriculaResponse buscarPorId(Long)`, `MatriculaResponse concluir(Long id, ConcluirMatriculaRequest)`

- [ ] **Step 1: DTOs e exceções**

- [ ] **Step 2: Services**

Classes `@Service` e `@Transactional`; conversão para DTO dentro do service. Mensagens fixas:
- cadastrar aluno: crie o `Aluno` primeiro (ele valida); depois `existsById` → `RegistroDuplicadoException("Já existe aluno com o RA: " + ra)`; `existsByEmail` → `RegistroDuplicadoException("Já existe aluno com o e-mail: " + email)`.
- cadastrar disciplina: crie a `Disciplina` primeiro; depois `existsById` → `RegistroDuplicadoException("Já existe disciplina com o código: " + codigo)`.
- buscas: `RecursoNaoEncontradoException("Aluno não encontrado: " + ra)`, `("Disciplina não encontrada: " + codigo)`, `("Matrícula não encontrada: " + id)`, `("Oferta não encontrada: " + id)`.
- matricular: se `raAluno` ou `idOferta` for `null` → `IllegalArgumentException("Aluno e oferta são obrigatórios.")`; busca aluno e oferta; `oferta.matricular(aluno)`; `save`.
- concluir: busca a matrícula; `matricula.concluir(request.resultado())`; `save`.

- [ ] **Step 3: Controllers e `GlobalExceptionHandler`**

Rotas e status exatamente como a tabela "Endpoints" do spec; os `POST` devolvem `201` com `Location`. O `@RestControllerAdvice` devolve `ErroResponse`:
- 400: `IllegalArgumentException`, `HttpMessageNotReadableException` (mensagem `"Dados da requisição inválidos."`), `MethodArgumentTypeMismatchException` (mensagem `"Parâmetro inválido: " + nome do parâmetro`);
- 404: `RecursoNaoEncontradoException`;
- 409: `RegistroDuplicadoException`, `IllegalStateException`.

- [ ] **Step 4: Subir a API**

Run (fora do sandbox): `"$MVN" -q package -DskipTests`, depois `DB_USER=sistema_academico DB_PASSWORD=sistema123 java -jar target/sistemaacademico-1.0-SNAPSHOT.jar` em segundo plano.
Expected: `Started SistemaAcademicoApplication`, porta 8080.

- [ ] **Step 5: Rodar os cenários com `curl`**

Mostre o status de cada um (`-w " [%{http_code}]"`):

| # | Requisição | Esperado |
|---|---|---|
| 1 | `POST /api/alunos` `{"ra":"RA2026090","nome":"Teste","email":"teste90@email.com"}` | 201 |
| 2 | o mesmo de novo | 409 |
| 3 | `POST /api/alunos` `{"ra":"RA2026091","nome":"Outro","email":"teste90@email.com"}` | 409, mensagem do e-mail |
| 4 | `POST /api/alunos` `{"ra":"RA2026092","email":"x@email.com"}` | 400 |
| 5 | `GET /api/alunos/RA9999999` | 404 com `mensagem` |
| 6 | `POST /api/disciplinas` `{"codigo":"ZZ","nome":"Zero","cargaHoraria":0}` | 400 |
| 7 | `POST /api/matriculas` `{"raAluno":"RA2026090"}` | 400 |
| 8 | `POST /api/matriculas` `{"raAluno":"RA2026090","idOferta":1}` | 201, `resultado` = `CURSANDO` |
| 9 | o mesmo de novo | 409 |
| 10 | `PATCH /api/matriculas/{id do 8}/conclusao` `{"resultado":"APROVADO"}` | 200; repetir → 409 |
| 11 | `PATCH .../conclusao` `{"resultado":"CURSANDO"}` numa matrícula nova | 400 |
| 12 | `PATCH .../conclusao` `{"resultado":"XYZ"}` | 400 |
| 13 | `GET /api/matriculas/abc` | 400 |
| 14 | `POST /api/matriculas` `{"raAluno":"RA2026090","idOferta":3}` (POO, outra turma, aluno já aprovado) | 409 |
| 15 | `GET /api/alunos`, `/api/disciplinas`, `/api/ofertas`, `/api/matriculas` | 200, listas JSON sem aninhamento circular |

Depois pare a aplicação. Os dados de teste ficam no banco local; isso é aceitável.

- [ ] **Step 6: Rodar os testes de domínio de novo**

Run: `"$MVN" -q test` → `BUILD SUCCESS`.

- [ ] **Step 7: Commit**

```bash
git add "Trabalhos/Trabalho 5"
git commit -m "Atividade Final: services, controllers, DTOs e tratamento global de erros"
```

---

### Task 4: Frontend React

**Files:**
- Create: `Trabalho 5/frontend/` (Vite, template `react`)
- Keep/Modify: `package.json`, `vite.config.js`, `index.html`, `src/main.jsx`
- Create: `src/App.jsx`, `src/api.js`, `src/Alunos.jsx`, `src/Disciplinas.jsx`, `src/Matriculas.jsx`, `src/index.css`
- Delete: tudo o mais que o template gerar (`src/App.css`, `src/assets/`, `public/vite.svg`, ESLint e suas dependências de desenvolvimento)
- Modify: `.gitignore` da raiz do repositório, acrescentando `node_modules/` e `dist/`

**Interfaces:**
- Consumes: as rotas e os campos JSON da Task 3.
- Produces: `export async function api(caminho, opcoes)` em `api.js`. Ela chama `fetch('/api' + caminho)` com `Content-Type: application/json`. Se a rede falhar, lança `Error("Não foi possível conectar ao servidor.")`. Se a resposta não for ok, lança `Error` com o `mensagem` do JSON ou, sem JSON, `"Erro " + status`. Devolve `response.json()`.

- [ ] **Step 1: Gerar e limpar o projeto**

Run (em `Trabalho 5`): `npm create vite@latest frontend -- --template react`, depois `npm install` em `frontend`. Apague os arquivos listados acima e tire as dependências de ESLint do `package.json`.

- [ ] **Step 2: Configurar o proxy**

Em `vite.config.js`: `server: { proxy: { '/api': 'http://localhost:8080' } }`.

- [ ] **Step 3: `api.js`, `App.jsx` e `index.css`**

`App.jsx`: `useState('alunos')`; três `<button>` (Alunos, Disciplinas, Matrículas); o botão ativo ganha a classe `ativo`; renderiza o componente da tela escolhida. `index.css`: fonte do sistema, largura máxima de 800px centralizada, tabela com bordas, classes `.erro` (texto vermelho) e `.ativo`.

- [ ] **Step 4: As três telas**

Todas seguem o padrão do spec: estado da lista, estado do formulário, estado do `erro`; uma função `carregar()` chamada no `useEffect`; no `onSubmit`, limpa o erro, faz o `POST`, limpa o formulário e chama `carregar()`; no `catch`, guarda `e.message` e mostra num `<p className="erro">`.
- `Alunos.jsx`: campos `ra`, `nome`, `email`; tabela RA, Nome, E-mail.
- `Disciplinas.jsx`: campos `codigo`, `nome`, `cargaHoraria` (`type="number"`, enviado com `Number(...)`); tabela Código, Nome, Carga horária.
- `Matriculas.jsx`: `<select>` de alunos (`/api/alunos`, texto `ra - nome`) e de ofertas (`/api/ofertas`, texto `turma - nomeDisciplina (ano/semestre)`), os dois começando com uma opção vazia; envia `{ raAluno, idOferta: Number(...) }`; tabela ID, Aluno, Turma, Disciplina, Resultado, Ações; nas linhas `CURSANDO`, botões **Aprovar** e **Reprovar** que fazem `PATCH /matriculas/{id}/conclusao` e chamam `carregar()`.

- [ ] **Step 5: Testar o fluxo completo pelo navegador**

Suba o backend (Task 3, Step 4) e o frontend (`npm run dev`, fora do sandbox). Abra a URL do Vite no navegador do app. Confira:
- cadastrar um aluno e vê-lo na lista;
- cadastrar um aluno com o mesmo RA e ver a mensagem vermelha da API;
- cadastrar uma disciplina;
- matricular um aluno, Aprovar e ver os botões sumirem;
- matricular o mesmo aluno numa oferta de POO de outra turma e ver a mensagem de "já foi aprovado".

Se o navegador do app não abrir `localhost`, faça a checagem com `curl` pelo proxy (`curl localhost:5173/api/ofertas` → 200) e peça ao usuário para conferir as telas.

- [ ] **Step 6: Backend desligado**

Pare o backend e cadastre um aluno pela tela. Expected: aparece uma mensagem de erro vermelha, sem tela em branco. Ajuste o `api.js` se o proxy do Vite responder com erro sem JSON.

- [ ] **Step 7: Commit**

```bash
git add .gitignore "Trabalhos/Trabalho 5/frontend"
git commit -m "Atividade Final: frontend React com telas de alunos, disciplinas e matrículas"
```

---

### Task 5: README

**Files:**
- Create: `Trabalho 5/README.md`

- [ ] **Step 1: Escrever o README**

Seções do spec ("README"), com:
- integrantes: Kauã Morais e João Vitor Passos;
- configuração do banco: o usuário de teste da Task 2 como opção recomendada, e como usar outro usuário pelas variáveis `DB_USER`/`DB_PASSWORD` (*Run → Edit Configurations → Environment variables*);
- como as tabelas são criadas sozinhas (`createDatabaseIfNotExist` + `spring.sql.init`) e, como alternativa, como rodar o `schema.sql` manualmente no MySQL Workbench;
- *Working directory* = `Trabalho 5`;
- frontend: `cd frontend`, `npm install`, `npm run dev`, abrir `http://localhost:5173`;
- tabela de endpoints e regras de negócio do spec;
- tabela "JDBC × JPA" (Atividade 03 × Atividade Final): conexão manual × `DataSource` gerenciado; SQL escrito à mão × métodos do `JpaRepository`; `PreparedStatement` e `setString` × parâmetros gerados pelo Hibernate; `ResultSet` e `paraAluno` × entidade montada pelo Hibernate; relacionamento por código × `@ManyToOne` com chave estrangeira.

- [ ] **Step 2: Conferir o README contra o enunciado**

Toda seção que o enunciado lista (integrantes, tema, tecnologias, banco, configurar banco, rodar backend, rodar frontend, funcionalidades) está presente.

- [ ] **Step 3: Commit**

```bash
git add "Trabalhos/Trabalho 5/README.md"
git commit -m "Atividade Final: README com instruções de execução"
```
