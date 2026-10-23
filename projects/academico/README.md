# Sistema Acadêmico

Projeto didático em Java 26, Maven e JUnit 5. O domínio mantém as regras e o encapsulamento das atividades anteriores. A demonstração de console continua disponível em `SistemaAcademico` pela IDE.

## Atividade 02 - 2BIM: Primeiros Endpoints com Spring Boot

Esta atividade está resolvida no projeto-base: HTTP → Controller → domínio → armazenamento em memória → JSON.

Com JDK 26 e Maven no PATH, execute a partir de `projects/academico`:

```bash
mvn clean test
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`.

| Método e rota | Exemplo / resultado |
| --- | --- |
| `GET /api/alunos/{registroAcademico}` | `RA2026001` ou `RA2026002` — 200 |
| `GET /api/matriculas/{codigo}` | `MAT-001` — 200 |
| `POST /api/matriculas` | Cria matrícula — 201 e cabeçalho `Location` |

## Atividade 03 - 2BIM: Persistência com JDBC e SQLite

Esta atividade evolui a API REST para persistir alunos e disciplinas no banco SQLite utilizando JDBC.

### Arquitetura e Separação de Responsabilidades

- **HTTP / Controller (`AcademicoController`):** Trata requisições e respostas HTTP, status codes e serialização JSON.
- **Domínio (`Aluno`, `Disciplina`, etc.):** Mantém regras de negócio e validações intrínsecas às entidades.
- **Interfaces de Repository (`AlunoRepository`, `DisciplinaRepository`):** Definem os contratos de acesso a dados desacoplados da tecnologia de banco.
- **Implementação JDBC (`AlunoRepositoryJdbc`, `DisciplinaRepositoryJdbc`):** Gerenciam `Connection`, executam SQL com `PreparedStatement` e mapeiam o `ResultSet` para instâncias de domínio, tratando adequadamente `SQLException`.
- **Configuração / Infraestrutura (`ConnectionFactory`, `DatabaseInitializer`):** Provê conexões JDBC e inicializa automaticamente as tabelas e dados iniciais caso ainda não existam.

### Endpoints da Atividade 03

| Método e rota | Status Sucesso | Descrição |
| --- | --- | --- |
| `POST /api/alunos` | `201 Created` | Cadastra novo aluno (retorna `409 Conflict` se duplicado, `400 Bad Request` se inválido) |
| `GET /api/alunos/{registroAcademico}` | `200 OK` | Consulta aluno por registro acadêmico (retorna `404 Not Found` se inexistente) |
| `POST /api/disciplinas` | `201 Created` | Cadastra nova disciplina (retorna `409 Conflict` se duplicada, `400 Bad Request` se inválida) |
| `GET /api/disciplinas/{codigo}` | `200 OK` | Consulta disciplina por código (retorna `404 Not Found` se inexistente) |

### Como Executar o Projeto e Preparar o Banco

1. **Compilar e rodar os testes:**
   No diretório `projects/academico`:
   ```bash
   mvn clean test
   ```

2. **Iniciar a aplicação:**
   ```bash
   mvn spring-boot:run
   ```
   *Nota:* O banco de dados SQLite (`database/sistema-academico.db`) é preparado e inicializado automaticamente na primeira execução através do `DatabaseInitializer` a partir de `database/schema.sql` e `database/dados-iniciais.sql`. Alternativamente, pode ser preparado manualmente com:
   ```bash
   sqlite3 database/sistema-academico.db ".read database/schema.sql"
   sqlite3 database/sistema-academico.db ".read database/dados-iniciais.sql"
   ```

3. **Validação com Postman:**
   - Importe a collection `postman/atividade-03-persistencia.postman_collection.json`.
   - A collection contém os testes automatizados para:
     1. Cadastro de aluno (`POST /api/alunos` → `201 Created`);
     2. Consulta de aluno (`GET /api/alunos/{ra}` → `200 OK`);
     3. Cadastro duplicado de aluno (`POST /api/alunos` → `409 Conflict`);
     4. Consulta de aluno inexistente (`GET /api/alunos/NAOEXISTE` → `404 Not Found`);
     5. Cadastro de aluno inválido (`POST /api/alunos` → `400 Bad Request`);
     6. Cadastro de disciplina (`POST /api/disciplinas` → `201 Created`);
     7. Consulta de disciplina (`GET /api/disciplinas/{codigo}` → `200 OK`);
     8. Cadastro duplicado de disciplina (`POST /api/disciplinas` → `409 Conflict`);
     9. Consulta de disciplina inexistente (`GET /api/disciplinas/NAOEXISTE` → `404 Not Found`);
     10. Cadastro de disciplina inválida (`POST /api/disciplinas` → `400 Bad Request`);
     11. Consulta de aluno persistido inicial (`GET /api/alunos/RA2026001` → `200 OK`);
     12. Consulta de disciplina persistida inicial (`GET /api/disciplinas/POO` → `200 OK`).

### Respostas para a Entrega (Pull Request)

1. **Onde estão os comandos SQL da aplicação?**
   > Os comandos SQL estão isolados exclusivamente nas classes de implementação dos repositórios: `AlunoRepositoryJdbc` e `DisciplinaRepositoryJdbc` (no pacote `br.edu.sistemaacademico.repository.jdbc`), não se misturando com os Controllers nem com as classes de domínio.

2. **Por que o Controller não deve executar SQL diretamente?**
   > Porque violaria o Princípio da Responsabilidade Única (SRP). O Controller deve se concentrar em receber requisições HTTP, tratar parâmetros, invocar as operações adequadas e devolver a resposta HTTP com status e payload corretos. Permitir que o Controller execute SQL criaria alto acoplamento à tecnologia de banco, dificultaria testes unitários e impediria a reutilização de consultas.

3. **Qual é o objetivo da interface de Repository?**
   > Estabelecer um contrato abstrato para as operações de persistência e recuperação de entidades. As camadas de controle e domínio dependem dessa interface (abstração) e não de uma tecnologia concreta (Princípio da Inversão de Dependência - DIP), permitindo alternar a implementação para outro banco ou mockar em testes sem alterar regras de negócio ou rotas da API.

4. **O que precisaria mudar para substituir SQLite por outra tecnologia de persistência?**
   > Bastaria:
   > 1. Adicionar o driver JDBC do novo banco no `pom.xml`;
   > 2. Alterar a URL e credenciais no `application.properties` (e no `ConnectionFactory`);
   > 3. Criar ou adaptar a classe concreta do Repository (ex: `AlunoRepositoryPostgres` ou similar) implementando a mesma interface `AlunoRepository`, adequando comandos SQL caso haja diferenças de dialeto;
   > 4. Nenhuma classe de Controller ou do pacote de domínio precisaria sofrer qualquer alteração.

## Testes e Cobertura

`mvn clean test` executa todos os testes de domínio, testes HTTP e testes de integração com SQLite JDBC.

`mvn verify` verifica a cobertura mínima de 80% das classes do domínio com JaCoCo.
