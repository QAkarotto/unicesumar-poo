# Sistema Acadêmico — Atividade Final 2BIM

Cadastro de alunos e disciplinas, matrícula em ofertas de disciplina e aprovação/reprovação.

**Integrantes:** Kauã Morais e João Vitor Passos

**Tecnologias:** Java 26, Spring Boot 4.1.2, Spring Data JPA, MySQL 8, ReactJS 19 + Vite.

## Pré-requisitos

- JDK 26
- MySQL 8 (e MySQL Workbench)
- Node.js LTS

## 1. Banco de dados

No MySQL Workbench, como `root`, rode (troque `sua_senha`):

```sql
CREATE USER IF NOT EXISTS 'sistema_academico'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON sistema_academico.* TO 'sistema_academico'@'localhost';
```

O banco, as tabelas (`database/schema.sql`) e os dados iniciais (`database/dados-iniciais.sql`) são criados automaticamente ao iniciar o backend.

## 2. Backend

1. Abra a pasta `Trabalho 5` no IntelliJ.
2. Em **Run → Edit Configurations → SistemaAcademicoApplication**:
   - **Working directory:** pasta `Trabalho 5`
   - **Environment variables:** `DB_USER=sistema_academico;DB_PASSWORD=sua_senha`
3. Rode `SistemaAcademicoApplication`. A API sobe em `http://localhost:8080`.

Testes: `mvn test`

## 3. Frontend

Com o backend rodando:

```bash
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`.

## Endpoints

| Método | URL |
|---|---|
| GET / POST | `/api/alunos` |
| GET | `/api/alunos/{ra}` |
| GET / POST | `/api/disciplinas` |
| GET | `/api/disciplinas/{codigo}` |
| GET | `/api/ofertas` |
| GET / POST | `/api/matriculas` |
| GET | `/api/matriculas/{id}` |
| PATCH | `/api/matriculas/{id}/conclusao` |

Erros retornam `{ "mensagem": "..." }` com status 400, 404 ou 409.
