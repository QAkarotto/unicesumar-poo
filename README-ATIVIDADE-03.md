# Atividade 03 — JDBC e SQLite

## Requisitos
- JDK 21 ou superior (o `pom.xml` usa Java 21)
- Maven

## Executar
Na raiz do projeto:

```bash
mvn spring-boot:run
```

O banco é criado automaticamente em `database/sistema-academico.db`. A configuração está em `src/main/resources/application.properties`. As tabelas são criadas na inicialização; `database/schema.sql` documenta o esquema e `database/dados-iniciais.sql` contém exemplos opcionais de dados iniciais.

## Requisições Postman
Base URL: `http://localhost:8080`

### Criar aluno — POST `/api/alunos`
```json
{
  "registroAcademico": "RA2026100",
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```
Resposta esperada: `201 Created`.

### Consultar aluno — GET `/api/alunos/RA2026100`
Resposta esperada: `200 OK`; se não existir, `404 Not Found`.

### Criar disciplina — POST `/api/disciplinas`
```json
{
  "codigo": "MAT101",
  "nome": "Matemática Aplicada",
  "cargaHoraria": 60
}
```
Resposta esperada: `201 Created`.

### Consultar disciplina — GET `/api/disciplinas/MAT101`
Resposta esperada: `200 OK`; se não existir, `404 Not Found`.

Cadastro repetido retorna `409 Conflict`; corpo incompleto ou inválido retorna `400 Bad Request`. Depois de cadastrar, pare e inicie a aplicação novamente e repita o GET para demonstrar a persistência.

## Arquitetura
- `api`: controllers e DTOs HTTP.
- `repository`: interfaces e implementações JDBC.
- `config/DatabaseConfig`: conexão e criação das tabelas.
- `domain`: entidades e regras de validação.

As consultas usam `PreparedStatement`, os resultados são convertidos em objetos de domínio por meio de `ResultSet`, e recursos JDBC são fechados com try-with-resources.

## Perguntas
1. Os comandos SQL ficam nas implementações concretas dos Repositories JDBC e na inicialização do banco.
2. O Controller não deve executar SQL porque sua responsabilidade é HTTP; separar o acesso a dados reduz acoplamento e facilita manutenção e testes.
3. A interface Repository define um contrato de persistência, permitindo trocar a implementação sem alterar os Controllers.
4. Para trocar SQLite, seria necessário alterar driver, URL/configuração, e possivelmente SQL específico; as interfaces e Controllers podem permanecer se o contrato for mantido.

## Collection Postman
Importe `postman/SistemaAcademico-Atividade03.postman_collection.json` no Postman.
