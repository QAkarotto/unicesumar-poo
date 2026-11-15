# Controle de Estoque

**Integrantes:** Gustavo Leão, Henrique Alves e João Iuki

## Tema e objetivo
Sistema para controlar produtos, categorias e movimentações (entradas e saídas) de estoque, com alerta de produtos abaixo do estoque mínimo.

## Tecnologias
Java 26, Spring Boot (Web, Data JPA, Validation), React + Vite, MySQL 8, JUnit 5.

## Arquitetura
React → API REST → Controller → Service → Domain/Entity → Repository → JPA/Hibernate → MySQL

As regras de negócio ficam nas entidades (`Produto`); o Service apenas coordena.

## Modelo de dados
`categoria (1) —— (N) produto (1) —— (N) movimentacao` — veja `database/schema.sql`.

## Funcionalidades do frontend
Resumo (produtos ativos, valor em estoque, abaixo do mínimo), busca e filtro, cadastro e edição de produto, entrada e saída de estoque, histórico de movimentações e gestão de categorias.

## Regras de negócio
1. Saída não pode deixar o estoque negativo.
2. Produto com saldo em estoque não pode ser desativado.
3. Produto inativo não pode ser movimentado; a quantidade deve ser > 0.
4. Categoria com produtos não pode ser excluída.
5. SKU é único; produto abaixo do mínimo é sinalizado.

## Banco de dados (MySQL)
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p -e "CREATE USER 'estoque_user'@'localhost' IDENTIFIED BY 'SUA_SENHA'; GRANT ALL ON estoque.* TO 'estoque_user'@'localhost';"
```

## Backend
Configure as variáveis de ambiente (não salve senhas no repositório):
```bash
export DB_URL=jdbc:mysql://localhost:3306/estoque
export DB_USER=estoque_user
export DB_PASSWORD=SUA_SENHA
cd backend && mvn spring-boot:run      # http://localhost:8080
mvn test                               # testes das regras de negócio
```

## Frontend
```bash
cd frontend && npm install && npm run dev   # http://localhost:5173 (proxy para /api)
```

## Endpoints
| Método | Rota | Descrição |
|---|---|---|
| POST/GET | `/api/categorias` | cadastrar / listar |
| DELETE | `/api/categorias/{id}` | excluir (se sem produtos) |
| POST/GET | `/api/produtos` | cadastrar / listar |
| GET | `/api/produtos/{id}`, `/api/produtos/baixo-estoque` | consultas |
| PUT | `/api/produtos/{id}` | atualizar |
| PATCH | `/api/produtos/{id}/desativacao` | desativar |
| POST/GET | `/api/movimentacoes` | registrar / listar (`?produtoId=`) |

Status: 201 criação, 400 dados inválidos, 404 não encontrado, 409 conflito (SKU duplicado), 422 regra de negócio violada.
