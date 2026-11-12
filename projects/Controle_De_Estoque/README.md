# Controle de Estoque

## Integrantes

- João (Frontend)
- Igor (Backend: domínio, persistência e banco de dados)
- Gustavo (Backend: API, regras de caso de uso e testes de serviço)

## Tema e objetivo

Sistema de controle de estoque para cadastrar categorias e produtos, registrar entradas e saídas
e manter um histórico de auditoria de todas as alterações feitas no sistema.

## Tecnologias

- Java 26
- Spring Boot 4, Spring Web, Spring Data JPA, Bean Validation
- H2 (padrão, para rodar local) e MySQL 8 (profile `mysql`)
- ReactJS (Create React App, `react-scripts`) e CSS3, sem bibliotecas de componentes
- react-toastify para as notificações
- JUnit 5 e Mockito

## Estrutura

```
Controle_De_Estoque/
├── backend/    API REST (controller, service, domain, repository, dto, exception)
│   └── database/schema.sql
├── frontend/   ReactJS (package.json, public/index.html e src/: App.js, api.js, index.js, App.css, index.css)
└── README.md
```

Fluxo: React -> API REST -> Controller -> Service -> Domain -> Repository -> JPA -> MySQL.

## Banco de dados

Por padrão a aplicação sobe com **H2** (arquivo em `backend/database/controle_estoque.mv.db`), sem instalar nada.
O Hibernate cria as tabelas automaticamente nesse modo. Console do H2: `http://localhost:8080/h2-console`.

O script oficial da entrega continua sendo MySQL: `backend/database/schema.sql`.

### MySQL (profile)

1. Crie o banco e as tabelas executando o script (cria o banco `controle_estoque`):

   ```
   mysql -u root -p < backend/database/schema.sql
   ```

2. Informe usuário e senha por variáveis de ambiente (nenhuma credencial fica no repositório):

   ```
   DB_USER=root
   DB_PASSWORD=sua_senha
   DB_URL=jdbc:mysql://localhost:3306/controle_estoque   (opcional)
   ```

3. Suba com o profile `mysql`: `mvn spring-boot:run -Dspring-boot.run.profiles=mysql`

No profile `mysql` o Hibernate não cria nem altera tabelas (`ddl-auto=none`): a estrutura vem do `schema.sql`.

### Modelo

- `categoria` 1 --- N `produto` (FK `produto.categoria_id`)
- `produto` 1 --- N `movimentacao` (FK `movimentacao.produto_id`)
- `log_auditoria`: registros de auditoria, sem FK para continuarem existindo após a exclusão da entidade

Os logs não podem ser alterados nem excluídos pela API ou pelo frontend. Só podem ser removidos
diretamente no banco (por exemplo `DELETE FROM log_auditoria WHERE id = 1;`).

## Executar o backend

```
cd backend
mvn spring-boot:run
```

A API sobe em `http://localhost:8080` usando o H2. Para usar o MySQL, veja a seção do banco de dados. Testes: `mvn test`.

## Executar o frontend

```
cd frontend
npm install
npm start
```

Acesse `http://localhost:3000`. O frontend consome a API em `http://localhost:8080/api`
(definida em `src/api.js`), então o backend precisa estar no ar. O backend libera CORS para `http://localhost:3000`.

## Funcionalidades

- Categorias: cadastrar, listar, editar e excluir.
- Produtos: cadastrar, listar, editar, inativar/ativar e excluir.
- Movimentações: registrar entrada e saída e consultar o histórico.
- Logs: consulta de todas as ações realizadas (somente leitura).
- Toda ação do usuário pede confirmação: "Realmente deseja prosseguir com a ação ...?".
- O resultado de cada ação (sucesso ou erro) aparece em um toast (react-toastify).

## Regras de negócio

- A quantidade em estoque só muda por movimentação (entrada ou saída).
- A saída não pode ser maior que o saldo (o saldo nunca fica negativo).
- Produto inativo não pode ser movimentado.
- Só é possível inativar um produto com saldo zerado.
- O código do produto e o nome da categoria são únicos.
- Categoria com produtos não pode ser excluída.
- Produto com movimentações não pode ser excluído (deve ser inativado, preservando o histórico).
- Movimentações e logs são imutáveis.
- Produto abaixo do estoque mínimo é sinalizado como "Estoque baixo".
- Cada alteração grava um log na mesma transação da ação.

## API

| Método | Rota | Descrição |
| --- | --- | --- |
| GET, POST | `/api/categorias` | Lista e cadastra |
| GET, PUT, DELETE | `/api/categorias/{id}` | Busca, atualiza e exclui |
| GET, POST | `/api/produtos` | Lista e cadastra |
| GET, PUT, DELETE | `/api/produtos/{id}` | Busca, atualiza e exclui |
| PATCH | `/api/produtos/{id}/inativacao` | Inativa |
| PATCH | `/api/produtos/{id}/ativacao` | Ativa |
| GET, POST | `/api/movimentacoes` | Lista e registra |
| GET | `/api/logs` | Lista os logs |

Status: 201 (criado), 204 (excluído), 400 (dados inválidos), 404 (não encontrado)
e 409 (operação não permitida pelas regras de negócio).

## JPA em relação ao JDBC

O JPA/Spring Data passou a abstrair o SQL manual, o `PreparedStatement`, o `ResultSet`, a conversão
linha-objeto, o controle das chaves geradas e das transações. O relacionamento entre objetos
(`Produto` tem uma `Categoria`) é mapeado por `@ManyToOne` + `@JoinColumn`, que corresponde à
chave estrangeira `produto.categoria_id` no banco.
