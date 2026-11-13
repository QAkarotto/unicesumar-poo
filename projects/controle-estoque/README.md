Sistema de Controle de Estoque

Aplicação full stack para controlar o estoque de uma empresa: cadastro de **categorias**, **fornecedores** e **produtos**, e registro de **movimentações** (entrada, saída e ajuste de inventário), com histórico completo e alerta de reposição.

Tema e objetivo

controle de estoque.

**Objetivo:** garantir que o saldo de cada produto seja sempre consistente e rastreável. O saldo **nunca é editado diretamente**: ele só muda por meio de uma movimentação (entrada, saída ou ajuste), que fica registrada com saldo anterior, saldo posterior, data/hora e responsável (fornecedor ou justificativa).

Tecnologias

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 26 |
| Backend | Spring Boot 4.1 (Spring Web MVC, Spring Data JPA, Bean Validation) |
| Persistência | JPA / Hibernate 7 + Spring Data JPA |
| Banco de dados | **MySQL 8** |
| Frontend | ReactJS 19 + React Router 7 + Vite 7 |
| Testes | JUnit 6 (Jupiter), AssertJ, Spring Boot Test + MockMvc, H2 (somente nos testes) |
| Build | Maven (Maven Wrapper incluso) / npm |

Estrutura do repositório

```
.
├── database/
│   ├── schema.sql          # CREATE TABLE, PKs, FKs, UNIQUE, CHECK
│   └── dados.sql           # dados de exemplo (opcional)
├── backend/                # API REST Spring Boot
│   ├── pom.xml, mvnw, mvnw.cmd
│   └── src/main/java/br/edu/controleestoque/
│       ├── api/            # camada HTTP: controllers, DTOs (records), GlobalExceptionHandler, CORS
│       ├── service/        # casos de uso + transações
│       ├── domain/         # entidades JPA com as regras de negócio + exceções de negócio
│       └── repository/     # interfaces Spring Data JPA
├── frontend/               # React (Vite)
│   └── src/                # api.js, App.jsx, componentes compartilhados (Mensagem, TabelaMovimentacoes)
│       └── pages/          # uma tela por arquivo
└── docker-compose.yml      # MySQL local (opcional)
```

Fluxo de uma requisição:

```
ReactJS (fetch JSON) → Controller → Service → Domain/Entity → Repository → JPA/Hibernate → MySQL
```

Principais funcionalidades

- **Painel**: indicadores (produtos, unidades, valor total em estoque) e lista de produtos que precisam de reposição.
- **Categorias** e **Fornecedores**: cadastro, listagem, edição e exclusão.
- **Produtos**: cadastro, edição, busca por nome/SKU e categoria, ativação/inativação, exclusão e tela de detalhes com vínculo de fornecedores e histórico.
- **Movimentações**: registro de entrada, saída e ajuste de inventário; histórico filtrável por produto e tipo.
- Mensagens de sucesso/erro exibidas na tela, incluindo os campos inválidos devolvidos pela API.

Regras de negócio (além do CRUD)

As regras ficam nas **entidades de domínio** (principalmente `Produto`), e não no controller:

| # | Regra | Onde | Resposta |
|---|-------|------|----------|
| 1 | O saldo **nunca fica negativo**: saída maior que o saldo é recusada | `Produto.registrarSaida` | 422 |
| 2 | Entrada exige um **fornecedor vinculado ao produto** | `Produto.registrarEntrada` | 422 |
| 3 | Ajuste de inventário exige **justificativa** e novo saldo diferente do atual | `Produto.ajustarEstoque` | 422 |
| 4 | Produto **inativo não pode ser movimentado** | `Produto.garantirAtivo` | 422 |
| 5 | Produto **com saldo não pode ser inativado** | `Produto.inativar` | 422 |
| 6 | Quantidade da movimentação deve ser maior que zero | `Produto` | 422 |
| 7 | Produto com histórico não pode ser excluído (deve ser inativado) | `ProdutoService.excluir` | 409 |
| 8 | Categoria com produtos não pode ser excluída | `CategoriaService.excluir` | 409 |
| 9 | Fornecedor com entradas registradas não pode ser excluído | `FornecedorService.excluir` | 409 |
| 10 | SKU, CNPJ e nome de categoria são únicos | services + `UNIQUE` no banco | 409 |
| 11 | Alerta de reposição quando saldo ≤ estoque mínimo | `Produto.precisaReposicao` | — |

O saldo (`quantidadeEmEstoque`) **não tem setter**: só muda pelos métodos acima, que sempre criam uma `Movimentacao` (imutável). O `MovimentacaoService` roda em uma transação (`@Transactional`): se a regra falhar, nada é gravado.

## 🌐 Endpoints da API

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/categorias` | Lista categorias |
| GET | `/api/categorias/{id}` | Busca categoria |
| POST | `/api/categorias` | Cadastra (201) |
| PUT | `/api/categorias/{id}` | Atualiza |
| DELETE | `/api/categorias/{id}` | Exclui (204) |
| GET/POST | `/api/fornecedores` | Lista / cadastra |
| GET/PUT/DELETE | `/api/fornecedores/{id}` | Busca / atualiza / exclui |
| GET | `/api/produtos?busca=&categoriaId=` | Lista com filtros |
| GET | `/api/produtos/{id}` | Detalhe + fornecedores vinculados |
| GET | `/api/produtos/reposicao` | Produtos no/abaixo do estoque mínimo |
| GET | `/api/produtos/resumo` | Indicadores do estoque |
| POST | `/api/produtos` | Cadastra (201) |
| PUT | `/api/produtos/{id}` | Atualiza dados (SKU é imutável) |
| PATCH | `/api/produtos/{id}/inativacao` | Inativa |
| PATCH | `/api/produtos/{id}/ativacao` | Reativa |
| DELETE | `/api/produtos/{id}` | Exclui (204) |
| POST | `/api/produtos/{id}/fornecedores/{fornecedorId}` | Vincula fornecedor |
| DELETE | `/api/produtos/{id}/fornecedores/{fornecedorId}` | Desvincula fornecedor |
| GET | `/api/movimentacoes?produtoId=&tipo=` | Histórico com filtros |
| GET | `/api/movimentacoes/{id}` | Busca movimentação |
| POST | `/api/movimentacoes` | Registra entrada/saída/ajuste (201) |

Movimentações não têm PUT/DELETE: são registros de auditoria. Para corrigir um saldo, registra-se um **AJUSTE**.

| Status | Quando |
|--------|--------|
| 400 Bad Request | Dados inválidos (Bean Validation — `campos` traz o erro de cada campo), JSON mal formado, tipo inválido |
| 404 Not Found | Recurso inexistente |
| 409 Conflict | Operação não permitida pelo estado dos dados (exclusões bloqueadas, SKU/CNPJ duplicado, violação de restrição) |
| 422 Unprocessable Content | Violação de regra de negócio do domínio |
| 500 Internal Server Error | Erro inesperado: o cliente recebe uma mensagem genérica e a stack trace vai para o log do backend |

Erros do próprio Spring MVC (rota inexistente → 404, método HTTP não suportado → 405) mantêm o status original e também respondem no formato `ErroResponse`. Violações de restrição do banco (409) registram no log qual restrição falhou.
