# Sistema Acadêmico - Aplicação Full Stack

**Atividade Final - 2BIM: Aplicação Full Stack com Spring Boot e ReactJS**  
Disciplina: Programação Orientada a Objetos  
Autor / Trio: Eduardo Diniz e Grupo

---

## 1. Visão Geral da Solução

O projeto consiste em uma aplicação corporativa full stack para gestão acadêmica de matrículas, turmas, ofertas de disciplinas e alunos. A arquitetura segue rigorosamente a separação de responsabilidades em camadas, adotando boas práticas de Programação Orientada a Objetos (POO), persistência relacional com Spring Data JPA / Hibernate, API RESTful e interface moderna e reativa construída com ReactJS e Vite.

### Arquitetura em Camadas

```text
ReactJS (Frontend SPA)
       ↓ (Requisições HTTP / JSON)
API REST (Endpoints RESTful)
       ↓
Controller (Tratamento de requisições, rotas e códigos HTTP)
       ↓
Service (Coordenação de casos de uso e orquestração transacional)
       ↓
Domain / Entity (Entidades JPA com encapsulamento e regras de negócio)
       ↓
Repository (Interfaces Spring Data JPA)
       ↓
JPA / Hibernate (Mapeamento Objeto-Relacional ORM)
       ↓
Banco de Dados Relacional SQL (MySQL)
```

---

## 2. Tecnologias Utilizadas

- **Linguagem Backend:** Java 26
- **Framework Principal:** Spring Boot 4.1.1
- **Camada Web:** Spring Web MVC
- **Persistência / ORM:** Spring Data JPA / Hibernate ORM
- **Driver de Banco de Dados:** MySQL Connector/J (`com.mysql:mysql-connector-j`)
- **Banco de Dados Relacional:** MySQL (ambiente de produção e desenvolvimento); H2 Database em modo de compatibilidade MySQL para execução isolada dos testes automatizados
- **Testes Unitários e Integração:** JUnit 5, AssertJ, Spring Test
- **Cobertura de Código:** JaCoCo (mínimo de 80% de cobertura em regras de domínio)
- **Frontend:** ReactJS 18, Vite 6, JavaScript moderno (ESModules), CSS3 sóbrio sem bibliotecas externas poluídas

---

## 3. Modelo do Banco de Dados Relacional

A estrutura relacional está declarada no script oficial `database/schema.sql`:

```text
+-------------------+       +-----------------------+       +-------------------+
|       ALUNO       |       |       MATRICULA       |       | OFERTA_DISCIPLINA |
+-------------------+       +-----------------------+       +-------------------+
| PK registro_acad. |<------| PK id                 |------>| PK id             |
|    nome           |       | FK aluno_ra           |       |    codigo (unique)|
|    email          |       | FK oferta_id          |       | FK turma_id       |
+-------------------+       |    codigo (unique)    |       | FK disciplina_id  |
                            |    situacao           |       +-------------------+
                            |    resultado          |         |               |
                            +-----------------------+         v               v
                                                        +-----------+   +-------------+
                                                        |   TURMA   |   | DISCIPLINA  |
                                                        +-----------+   +-------------+
                                                        | PK id     |   | PK id       |
                                                        |    codigo |   |    codigo   |
                                                        |    ano    |   |    nome     |
                                                        |  semestre |   | carga_hor.  |
                                                        +-----------+   +-------------+
```

### Script DDL e Carga Inicial
- DDL das tabelas e restrições: `database/schema.sql`
- Dados semente iniciais: `database/dados-iniciais.sql`

---

## 4. Como Configurar e Executar a Aplicação

### Pré-requisitos
- JDK 26 instalado e configurado no `JAVA_HOME`
- Maven 3.9+ instalado (ou via IDE IntelliJ / VS Code)
- Node.js 20+ e npm instalados
- MySQL Server ativo (porta padrão 3306)

---

### Passo 1: Inicializar o Banco de Dados MySQL

No seu cliente MySQL (MySQL Workbench, DBeaver, HeidiSQL ou linha de comando):

```sql
CREATE DATABASE IF NOT EXISTS sistema_academico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Em seguida, execute os scripts na ordem:
1. `projects/academico/database/schema.sql`
2. `projects/academico/database/dados-iniciais.sql`

---

### Passo 2: Configurar o Backend

As configurações de conexão com o banco de dados estão centralizadas em:  
`projects/academico/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_academico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> **Nota:** Caso o seu usuário ou senha do MySQL local sejam diferentes de `root`/`root`, altere as propriedades correspondentes acima antes de iniciar.

---

### Passo 3: Executar os Testes Automatizados do Backend

No diretório `projects/academico`:

```bash
mvn clean verify
```

O comando executará todos os testes unitários, testes de integração HTTP e checagem de cobertura do JaCoCo. O resultado esperado é:
```text
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

---

### Passo 4: Iniciar o Backend Spring Boot

No diretório `projects/academico`:

```bash
mvn spring-boot:run
```

A API RESTful iniciará em `http://localhost:8080`.

---

### Passo 5: Iniciar o Frontend ReactJS

Abra um novo terminal e acesse a pasta `projects/academico/frontend`:

```bash
cd projects/academico/frontend
npm install
npm run dev
```

O frontend iniciará em `http://localhost:5173`. Acesse essa URL no navegador.

---

## 5. Endpoints da API REST

### Alunos (`/api/alunos`)
- `GET /api/alunos`: Lista todos os alunos cadastrados.
- `GET /api/alunos/{ra}`: Consulta detalhes do aluno pelo Registro Acadêmico.
- `POST /api/alunos`: Cadastra um novo aluno.  
  Corpo da requisição:
  ```json
  {
    "registroAcademico": "RA2026100",
    "nome": "Mariana Santos",
    "email": "mariana.santos@unicesumar.edu.br"
  }
  ```

### Disciplinas (`/api/disciplinas`)
- `GET /api/disciplinas`: Lista todas as disciplinas.
- `GET /api/disciplinas/{codigo}`: Consulta uma disciplina por código ou ID.
- `POST /api/disciplinas`: Cadastra uma nova disciplina.  
  Corpo da requisição:
  ```json
  {
    "codigo": "EST-DADOS",
    "nome": "Estrutura de Dados",
    "cargaHoraria": 80
  }
  ```

### Ofertas de Disciplinas (`/api/ofertas`)
- `GET /api/ofertas`: Lista todas as ofertas de disciplinas ativas.
- `POST /api/ofertas`: Cria uma nova oferta vinculando disciplina e turma.

### Matrículas (`/api/matriculas`)
- `GET /api/matriculas`: Lista todas as matrículas cadastradas.
- `GET /api/matriculas?ra={registroAcademico}`: Filtra matrículas de um aluno específico.
- `GET /api/matriculas/{id}`: Consulta dados de uma matrícula por ID ou código (`MAT-001`).
- `POST /api/matriculas`: Realiza uma nova matrícula.  
  Corpo da requisição:
  ```json
  {
    "registroAcademico": "RA2026002",
    "ofertaId": "BD-2026-2"
  }
  ```
- `PATCH /api/matriculas/{id}/conclusao`: Conclui uma matrícula ativa atribuindo aprovação ou reprovação.  
  Corpo da requisição:
  ```json
  {
    "resultado": "APROVADO"
  }
  ```
- `PATCH /api/matriculas/{id}/trancar`: Tranca uma matrícula ativa.
- `PATCH /api/matriculas/{id}/cancelar`: Cancela uma matrícula ativa.
- `DELETE /api/matriculas/{id}`: Remove fisicamente o registro de uma matrícula.

---

## 6. Regras de Negócio e Princípios de POO

O sistema implementa regras de negócio no domínio e garante a integridade dos dados:

1. **Bloqueio de Duplicidade na Oferta:** Um aluno não pode se matricular mais de uma vez na mesma oferta de disciplina.
2. **Bloqueio de Matrícula Após Aprovação:** Um aluno que já foi aprovado em uma disciplina não pode se matricular novamente nessa mesma matéria, mesmo em ofertas ou turmas posteriores.
3. **Máquina de Estados da Matrícula:**
   - Uma matrícula inicia no estado `ATIVA` com resultado acadêmico pendente.
   - Operações como Conclusão (`concluir`), Trancamento (`trancar`) e Cancelamento (`cancelar`) só podem ser aplicadas se o estado atual for `ATIVA`.
   - Uma vez `CONCLUIDA`, `TRANCADA` ou `CANCELADA`, a matrícula não pode sofrer nova alteração de estado (lança exceção de invariante).
4. **Encapsulamento e Invariantes:** As entidades de domínio (`Aluno`, `Disciplina`, `Matricula`, `OfertaDisciplina`, `Turma`) validam argumentos em seus construtores e métodos de negócio, impedindo a criação de objetos em estado inválido.
5. **Abstração ORM com Spring Data JPA:** Ao contrário do acesso manual via JDBC puro (com `Connection`, `PreparedStatement` e `ResultSet`), o Spring Data JPA abstrai o ciclo de vida dos objetos, mapeia chaves estrangeiras automaticamente e gerencia transações com `@Transactional`.

---

## 7. Apresentação da Atividade

Para orientações detalhadas sobre como apresentar a aplicação para o professor e a turma, consulte a seção do roteiro de apresentação no relatório do projeto.
