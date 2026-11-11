# Biblioteca - Atividade Final

Sistema de biblioteca com Spring Boot, JPA e MySQL. Gerencia usuários, livros e empréstimos, com as regras:

- um usuário não pode ter mais de 3 empréstimos ativos;
- um livro não pode ser emprestado se já estiver com um empréstimo ativo;
- a devolução só é possível para empréstimos ativos.

## Tecnologias

- Java 26
- Spring Boot 4.1.2 (Spring Web, Spring Data JPA)
- MySQL 8
- JUnit 5 (testes das regras de negócio)

## Banco de dados

1. Crie o banco e o usuário no MySQL (exemplo com `root`):

```sql
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'biblioteca_app'@'localhost' IDENTIFIED BY 'SUA_SENHA_AQUI';
GRANT ALL PRIVILEGES ON biblioteca.* TO 'biblioteca_app'@'localhost';
```

2. Execute o script de criação das tabelas:

mysql -u root -p biblioteca < projects\biblioteca\database\schema.sql


O `schema.sql` é a fonte da estrutura do banco. O Hibernate não cria nem altera tabelas (`spring.jpa.hibernate.ddl-auto=none`).

## Configuração

A senha não fica no repositório. Defina a variável de ambiente `DB_PASSWORD` antes de executar:

- No IntelliJ: **Run > Edit Configurations > BibliotecaApplication > Environment variables** e adicione `DB_PASSWORD=SUA_SENHA_AQUI`.
- No terminal (PowerShell): `$env:DB_PASSWORD="SUA_SENHA_AQUI"`.

A URL e o usuário estão em `src/main/resources/application.properties`.

## Como executar

1. Dentro de `projects/biblioteca`, rode `./mvnw spring-boot:run` (ou execute `BibliotecaApplication` pelo IntelliJ).
2. A API sobe em `http://localhost:8080`.

## Como testar

- Testes automatizados: `./mvnw test`.
- Requisições: importe `postman/biblioteca.postman_collection.json` no Postman.

## Endpoints

| Método | URL | Descrição | Status |
|---|---|---|---|
| POST | `/api/emprestimos` | Cria empréstimo | 201 / 404 / 409 / 400 |
| GET | `/api/emprestimos/{id}` | Consulta empréstimo | 200 / 404 |
| PATCH | `/api/emprestimos/{id}/devolucao` | Devolve empréstimo | 200 / 404 / 409 |

## Dados de exemplo

Para testar, cadastre um usuário e alguns livros (veja os `INSERT` no README do PR, ou use:

```sql
INSERT INTO usuario (nome, email) VALUES ('Ana', 'ana@example.com');
INSERT INTO livro (titulo, autor, isbn) VALUES
    ('Dom Casmurro', 'Machado de Assis', '111'),
    ('Iracema', 'José de Alencar', '222'),
    ('Memórias Póstumas', 'Machado de Assis', '333'),
    ('O Cortiço', 'Aluísio Azevedo', '444');
```

## Integrantes

- Pedro Henrique Antunes Pereira