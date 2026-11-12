# 🎮 Loja de Jogos: Atividade Final 2BIM (POO)

Aplicação full stack de e-commerce de jogos: **ReactJS → API REST (Spring Boot) → JPA/Hibernate → MySQL**.

## Integrantes

- Arthur Pacher Santos (ESOFT4S-NA)
- Ketely Horns (ESOFT4S-NA)


## Tema e objetivo

Loja virtual de jogos. A loja cadastra o catálogo de jogos (com preço e estoque) e os clientes, e registra pedidos de compra. O sistema controla o estoque: não vende o que não existe e devolve ao estoque quando um pedido é cancelado.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Frontend | ReactJS + Vite |
| Backend | Java 26, Spring Boot 4.1.1, Spring Web (MVC) |
| Persistência | JPA / Spring Data JPA (Hibernate) |
| Banco | **MySQL 8.4 LTS** |
| Testes | JUnit 5 |

## Modelagem

```text
CLIENTE (1) ──────< PEDIDO >────── (1) JOGO
```

| Tabela | Colunas | Chaves e restrições |
|---|---|---|
| `cliente` | id, nome, email | PK `id` · `email` UNIQUE |
| `jogo` | id, titulo, plataforma, preco, estoque | PK `id` · `preco > 0` · `estoque >= 0` |
| `pedido` | id, cliente_id, jogo_id, quantidade, valor_total, status, data_pedido | PK `id` · FK `cliente_id → cliente.id` · FK `jogo_id → jogo.id` · `quantidade > 0` · `status IN ('CONFIRMADO','CANCELADO')` |

- Um cliente pode ter vários pedidos; cada pedido é de um cliente (1:N).
- Um jogo pode estar em vários pedidos; cada pedido é de um jogo (1:N).
- No Java, os dois relacionamentos são `@ManyToOne` em `Pedido`, com `@JoinColumn` apontando para as colunas de FK.

Script completo: [`database/schema.sql`](database/schema.sql).

## Regras de negócio

1. **Estoque:** um pedido só é criado se a quantidade pedida couber no estoque do jogo. Ao confirmar, o estoque diminui (`Jogo.baixarEstoque`).
2. **Cancelamento:** só um pedido `CONFIRMADO` pode ser cancelado, e o cancelamento devolve a quantidade ao estoque (`Pedido.cancelar`).

Validações extras: preço maior que zero, estoque não negativo, e-mail válido e único por cliente.

## Endpoints

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| GET | `/api/jogos` | Lista os jogos | 200 |
| GET | `/api/jogos/{id}` | Busca um jogo | 200 · 404 |
| POST | `/api/jogos` | Cadastra jogo | 201 · 400 |
| PUT | `/api/jogos/{id}` | Altera jogo | 200 · 400 · 404 |
| GET | `/api/clientes` | Lista clientes | 200 |
| GET | `/api/clientes/{id}` | Busca cliente | 200 · 404 |
| POST | `/api/clientes` | Cadastra cliente | 201 · 400 · 409 (e-mail repetido) |
| GET | `/api/pedidos` | Lista pedidos | 200 |
| GET | `/api/pedidos/{id}` | Busca pedido | 200 · 404 |
| POST | `/api/pedidos` | Cria pedido (compra) | 201 · 400 · 404 · 409 (sem estoque) |
| PATCH | `/api/pedidos/{id}/cancelamento` | Cancela pedido | 200 · 404 · 409 (já cancelado) |

Os erros voltam em JSON: `{ "status": 409, "mensagem": "Estoque insuficiente..." }`.

A collection do Postman com todos os cenários está em [`postman/`](postman/).

## Como executar

### Pré-requisitos

- JDK 26 e Maven
- Node.js 20 ou mais novo
- MySQL 8.4 (MySQL Community Server)

### 1. Criar o banco

No **MySQL Workbench**: *File → Open SQL Script →* `database/schema.sql` e clique no raio ⚡ para executar. Para ter dados de exemplo, faça o mesmo com `database/dados-iniciais.sql`.

Ou pelo terminal, a partir desta pasta:

```bash
mysql -u root -p -e "source database/schema.sql"
mysql -u root -p -e "source database/dados-iniciais.sql"
```

### 2. Configurar a conexão

A conexão está em `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loja_jogos
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}
```

O usuário e a senha **não ficam no repositório**: são lidos das variáveis de ambiente `DB_USER` (padrão `root`) e `DB_PASSWORD`.

- **PowerShell:** `$env:DB_PASSWORD="sua_senha"` (vale só para aquele terminal).
- **IntelliJ:** *Run → Edit Configurations → Environment variables* → `DB_PASSWORD=sua_senha`.

### 3. Executar o backend

```bash
cd backend
mvn test              # roda os testes das regras de negócio
mvn spring-boot:run   # sobe a API em http://localhost:8080
```

Ou abra `LojaJogosApplication` no IntelliJ e clique no ▶.

### 4. Executar o frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

Abra `http://localhost:5173`.

## Testes

`backend/src/test/java/br/edu/lojajogos/domain`: 12 testes JUnit 5 no padrão Arrange → Act → Assert, cobrindo as regras de estoque e de cancelamento e as validações do cadastro. Os testes usam só objetos do domínio, sem banco, porque as regras ficam nas entidades.

## Organização do backend

```text
br.edu.lojajogos
├── LojaJogosApplication.java
├── controller/   ← HTTP: rotas, JSON, status
├── service/      ← casos de uso (comprar, cancelar) e transações
├── domain/       ← entidades JPA + regras de negócio
├── repository/   ← Spring Data JPA
├── dto/          ← records de entrada e saída da API
└── exception/    ← RecursoNaoEncontradoException e GlobalExceptionHandler
```