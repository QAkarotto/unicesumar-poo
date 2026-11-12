# ANiVA

ANiVA organiza um catálogo de anime, mangá, manhwa e manhua. Você pode filtrar obras por formato, gênero, trope e disponibilidade, consultar fichas, avaliar títulos, comentar e manter uma lista pessoal (favoritos e pauta de "para ver/ler").

## Tecnologias

- **Frontend:** ReactJS (Vite), React Router.
- **Backend:** Java 26, Spring Boot, Spring Web, JPA / Spring Data JPA.
- **Banco de dados:** MySQL.

## Estrutura do projeto

```
aniva/
├── src/, public/, package.json ...   frontend (ReactJS)
├── backend/                          API REST (Spring Boot)
│   └── src/main/java/br/edu/aniva/
│       ├── controller/               rotas HTTP, status e sessão
│       ├── service/                  casos de uso
│       ├── domain/                   entidades JPA e regras do domínio
│       ├── repository/               Spring Data JPA
│       ├── dto/                      dados que entram e saem da API
│       └── exception/                erros e GlobalExceptionHandler
├── database/
│   ├── schema.sql                    estrutura das tabelas
│   └── data.sql                      catálogo inicial de obras
└── BACKEND.md                        documentação dos endpoints
```

Fluxo de uma requisição:

```
React (src/services/api.js) → API REST → Controller → Service → Domain/Entity → Repository → JPA/Hibernate → MySQL
```

## Como funciona

- O frontend chama `/api/...`. Em desenvolvimento, o Vite repassa essas chamadas para `http://localhost:8080` (veja `vite.config.js`).
- O login usa **sessão por cookie**: o servidor guarda o id da pessoa logada, e nenhum endpoint confia em id de usuário enviado pelo frontend.
- As senhas são guardadas com hash BCrypt.
- Busca, filtros e ordenação do catálogo continuam sendo feitos no navegador (`src/utils/filtrarObras.js`).

## Banco de dados

- SGBD: **MySQL 8** ou superior, porta 3306.
- `database/schema.sql` cria todas as tabelas (chaves primárias, estrangeiras, `UNIQUE` e `CHECK`).
- `database/data.sql` insere as obras do catálogo.
- O Spring Boot executa os dois scripts ao iniciar (`spring.sql.init.mode=always`). Ambos são idempotentes (`CREATE TABLE IF NOT EXISTS` e `INSERT IGNORE`), então podem rodar várias vezes sem duplicar nada. O Maven copia os arquivos de `database/` para dentro do backend.
- O Hibernate **não** cria nem altera tabelas (`spring.jpa.hibernate.ddl-auto=none`): a estrutura vem somente do `schema.sql`.

### Como criar o banco

1. Instale o MySQL Server 8 e deixe o serviço rodando na porta 3306. Defina uma senha para o usuário `root`.
2. Conecte como `root`:

```bash
mysql -u root -p
```

3. Crie o banco e o usuário da aplicação:

```sql
CREATE DATABASE aniva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'aniva'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON aniva.* TO 'aniva'@'localhost';
FLUSH PRIVILEGES;
```

4. Não é preciso rodar o `schema.sql` à mão: o backend executa o script ao iniciar. Para rodar manualmente (opcional), use `mysql -u aniva -p aniva < database/schema.sql` e depois o mesmo com `database/data.sql`.

### Como configurar a conexão

A conexão está em `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/aniva?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8}
spring.datasource.username=${DB_USER:aniva}
spring.datasource.password=${DB_PASSWORD:}
```

Há duas formas de informar usuário e senha:

- **Variáveis de ambiente** (recomendado, nada de senha no repositório). No PowerShell:

```powershell
$env:DB_USER = "aniva"
$env:DB_PASSWORD = "sua_senha"
mvn spring-boot:run
```

- **Direto no arquivo**, apenas em desenvolvimento local: troque os dois valores por `spring.datasource.username=aniva` e `spring.datasource.password=sua_senha`. **Não envie senhas reais ao repositório.**

Erros comuns: `Connection refused` (MySQL parado), `Access denied` (senha diferente da criada no passo 3) e `Unknown database 'aniva'` (banco não criado).

### Modelo relacional

```
USUARIO 1──N ITEM_LISTA N──1 OBRA        favoritos e pauta de cada pessoa
USUARIO 1──N AVALIACAO  N──1 OBRA        uma nota (1 a 10) por pessoa e obra
USUARIO 1──N COMENTARIO N──1 OBRA
COMENTARIO N──N USUARIO                  curtidas (tabela COMENTARIO_CURTIDA)
OBRA 1──N OBRA_TIPO / GENERO / TROPE / AVISO / FORMATO / PLATAFORMA / PERSONAGEM
```

## Como rodar

Backend (precisa de JDK 26 e Maven):

```bash
cd backend
mvn spring-boot:run
```

Testes do backend:

```bash
cd backend
mvn test
```

Frontend:

```bash
npm ci
npm run dev
npm run lint
npm run build
npm test
```

O Vite mostra no terminal o endereço do frontend. O backend precisa estar rodando na porta 8080.

## Regras de negócio

- Um e-mail só pode ter uma conta, e a senha é guardada somente com hash (`UsuarioService`).
- Cada pessoa tem uma única nota por obra, de 1 a 10. Avaliar de novo substitui a nota (`Avaliacao`, `AvaliacaoService`).
- Na pauta, "concluído" sempre fica com 100% e "a fazer" com 0%. O progresso vai de 0 a 100 (`ItemLista`).
- Só o autor pode apagar o próprio comentário. Curtir de novo remove a curtida (`Comentario`).

## Rotas do frontend

- `/`: início.
- `/catalogo`: busca e filtros. Os critérios ficam na URL: `q`, `formato`, `genero`, `trope`, `status`, `estudio`, `onde`, `nota` e `ordem`.
- `/obra/:id`: ficha, avaliação e comentários.
- `/minha-lista`: favoritos e obras salvas.
- `/entrar`: entrada e criação de conta.
- `/favoritos` e `/para-ver-ler`: redirecionam para `/minha-lista`.

Os endpoints da API estão em [BACKEND.md](./BACKEND.md).
