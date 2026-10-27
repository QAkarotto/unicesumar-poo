## Persistência com JDBC e SQLite (Atividade 03)

### Requisitos
- JDK 25

### Como executar
A aplicação deve ser executada a partir da **raiz do repositório**, porque os caminhos em `application.properties` são relativos a ela.

- **VS Code:** rodar a classe `SistemaAcademico` pelo botão Run.
- **Terminal (na raiz do repositório):**
```
  mvn -f projects/academico/pom.xml spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Banco de dados
Não é necessário nenhum passo manual. Ao iniciar, a aplicação cria o arquivo `projects/academico/database/academico.db` e executa `schema.sql` (tabelas) e `dados-iniciais.sql` (dados de exemplo, com `INSERT OR IGNORE`, então pode reiniciar sem duplicar). O arquivo `.db` não é versionado.

### Endpoints
- `POST /api/alunos` e `GET /api/alunos/{ra}`
- `POST /api/disciplinas` e `GET /api/disciplinas/{codigo}`

Status usados: 201 (criado), 200 (encontrado), 404 (não encontrado), 409 (duplicado), 400 (dados inválidos).

### Validação
Collection do Postman em `projects/academico/postman/`.
https://pbittencourt219-382892.postman.co/workspace/Pierre's-Workspace~e2e5aaa7-cf1c-44ac-8415-30ca1d071f5d/collection/58739882-baf4274d-1d36-4484-a3fb-bc9279f463fc?action=share&source=copy-link&creator=58739882.
