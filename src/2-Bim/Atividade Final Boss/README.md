# Atividade Final - Biblioteca Universitária

Aplicação full stack com **Spring Boot + JPA**, **React** e **MySQL**.

## Integrantes e responsabilidades

- **Lucas**: Spring Boot, API REST, testes e frontend React
- **Maria**: schema SQL, domínio, repositories e services (back)

## Domínio

- **Leitor**
- **Livro**
- **Empréstimo**

### Regras de negócio (além do CRUD)

1. Não empresta livro sem exemplares disponíveis
2. Leitor não pode ter mais de **3** empréstimos ativos
3. Leitor não pode ter dois empréstimos ativos do mesmo livro
4. Empréstimo já devolvido não pode ser devolvido novamente

## Estrutura

```
Atividade Final Boss/
├── pom.xml
├── database/schema.sql
├── src/                  # backend Spring Boot
└── biblioteca-web/       # frontend React (Create React App)
```

## Banco de dados

Por padrão a aplicação sobe com **H2** (arquivo em `database/biblioteca.mv.db`), para rodar fácil no IntelliJ.

O script oficial da entrega continua sendo MySQL: [database/schema.sql](database/schema.sql).

### MySQL (profile)

1. Suba o MySQL e execute:

```sql
CREATE DATABASE biblioteca;
```

```bash
mysql -u root -p biblioteca < database/schema.sql
```

2. Ajuste usuário/senha em `src/main/resources/application-mysql.properties`
3. No IntelliJ, em **Run Configuration → Program arguments**:

```text
--spring.profiles.active=mysql
```

Não versionamos senhas reais.

## Executar no IntelliJ

Se você abrir o repositório inteiro, o IntelliJ pode não tratar esta pasta como projeto Java. Sem o Maven linkado, o `application.properties` não entra no classpath e a aplicação falha no DataSource.

1. No painel **Project**, clique com o botão direito em [pom.xml](pom.xml)
2. Escolha **Add as Maven Project** (ou **Link Maven Project**)
3. No Maven tool window, clique em **Reload All Maven Projects**
4. Use **JDK 21+** em **File → Project Structure → Project → SDK**
5. Confira se `src/main/resources` está marcada como **Resources Root**
6. Rode a classe `BibliotecaApplication` (Run ▶ ao lado do `main`)

API em `http://localhost:8080`.

### Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/leitores` | Cadastrar leitor |
| GET | `/api/leitores` | Listar leitores |
| PUT | `/api/leitores/{id}` | Atualizar leitor |
| DELETE | `/api/leitores/{id}` | Excluir leitor |
| POST | `/api/livros` | Cadastrar livro |
| GET | `/api/livros` | Listar livros |
| PUT | `/api/livros/{id}` | Atualizar livro |
| DELETE | `/api/livros/{id}` | Excluir livro |
| POST | `/api/emprestimos` | Realizar empréstimo |
| GET | `/api/emprestimos` | Listar empréstimos |
| POST | `/api/emprestimos/{id}/devolucao` | Devolver livro |

## Frontend (React)

```bash
cd biblioteca-web
npm install
npm start
```

Abra `http://localhost:3000`. O front consome `http://localhost:8080/api`.

## Camadas

Controller → Service → Domain/Entity → Repository → JPA/Hibernate → Banco (H2 local / MySQL)
