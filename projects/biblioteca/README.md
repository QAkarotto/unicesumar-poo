# Sistema de Biblioteca

Aplicação full stack da atividade final do 2º bimestre. O sistema conecta livros, leitores e empréstimos, com regras no domínio Java, API REST, persistência relacional e frontend React.

## Tema e objetivo

O tema é o gerenciamento de uma biblioteca. O objetivo é cadastrar livros e leitores, registrar empréstimos e devoluções e controlar os exemplares disponíveis e os prazos de devolução.

## Integrantes

| Integrante | RA |
| --- | --- |
| Vitor Bahena Correa | 25229921-2 |
| Murilo Antunes da Luz | 25014370-2 |
| João Vitor de Souza Nascimento | 25164789-2 |
| Ryan Andreski | 25181918-2 |

## Tecnologias e requisitos

- **JDK 26** e Maven 3.9+.
- Spring Boot 4.1.2, Spring Web MVC, Bean Validation, Spring Data JPA e Hibernate.
- **MySQL 8.4** com InnoDB; Docker Compose é uma opção para executar o banco.
- React 19, Vite 7, JavaScript, CSS responsivo. Node.js 22.12+ ou 24 e npm.
- JUnit e testes de integração HTTP/JPA com H2 **apenas no escopo de testes**. A aplicação usa MySQL.

Compatibilidade do Java conferida na [documentação do Spring Boot](https://docs.spring.io/spring-boot/system-requirements.html).

## Funcionalidades

- Painel com títulos, exemplares, leitores ativos, empréstimos em circulação e atrasos.
- Cadastro, consulta, busca e edição de livros e leitores.
- Ativação e inativação de leitores sem apagar seu histórico.
- Registro de empréstimos, consulta de histórico e confirmação de devolução.
- Busca por livro/leitor e filtros de disponibilidade e situação.
- Validação de formulários, erros da API e mensagens após operações.

O banco começa vazio. Cadastre livros e leitores pela interface; não há dados fictícios exibidos como registros reais.

## Execução rápida com MySQL no Docker

Depois de clonar ou baixar o repositório, abra a pasta que contém este README, `backend`, `frontend` e `database`. Para quem já tem o MySQL instalado, siga a seção **MySQL já instalado, sem Docker**; não é necessário instalar Docker.

Abra um terminal **na pasta `biblioteca`**. Confirme `java -version`, `mvn -version`, `node -v` e `docker compose version`.

1. Copie `.env.example` para `.env` e substitua as duas senhas por valores locais diferentes. Não envie `.env` ao repositório.

   ```powershell
   Copy-Item .env.example .env
   ```

2. Inicie o banco e aguarde ficar saudável:

   ```powershell
   docker compose up -d --wait db
   ```

   O Compose cria o banco `biblioteca`, o usuário `biblioteca` e executa `database/schema.sql` na primeira inicialização. O volume `biblioteca-dados` mantém os registros entre reinicializações. Alterar senhas no `.env` não altera usuários de um volume já inicializado.

3. Em um terminal, configure o backend. A senha precisa ser a mesma definida em `.env`. **O Spring Boot não lê o arquivo `.env` automaticamente.**

   ```powershell
   cd backend
   $env:DB_USER = 'biblioteca'
   $senha = Read-Host 'Senha do usuario biblioteca' -AsSecureString
   $env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $senha).Password
   mvn spring-boot:run
   ```

   API em `http://localhost:8080/api/livros`. O Spring executa o schema idempotente e o Hibernate usa `ddl-auto=validate`: a criação das tabelas está no SQL, não na geração automática do Hibernate.

4. Em outro terminal, a partir de `biblioteca`:

   ```powershell
   cd frontend
   npm ci
   npm run dev
   ```

   Abra [a aplicação](http://localhost:5173). Use a porta indicada pelo Vite se 5173 estiver ocupada. O proxy encaminha `/api` ao backend; não é necessário liberar CORS.

No Linux/macOS, use `cp .env.example .env` e `export DB_USER=biblioteca DB_PASSWORD='sua_senha_local'` no lugar dos comandos PowerShell. A senha solicitada deve ser a mesma configurada para o usuário `biblioteca`, não necessariamente a senha do administrador `root`.

### Java/Maven no IntelliJ deste computador

Se os comandos não estiverem no PATH, as instalações encontradas neste ambiente podem ser usadas no terminal do backend:

```powershell
$env:JAVA_HOME = "$env:USERPROFILE\.jdks\openjdk-26.0.2"
& 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.1\plugins\maven-plugin\lib\maven3\bin\mvn.cmd' spring-boot:run
```

Em outro computador, instale o JDK 26 e Maven e configure `JAVA_HOME` e `PATH`. No IntelliJ também é possível importar `backend/pom.xml`, selecionar o SDK 26 e executar `BibliotecaApplication` com a variável de ambiente `DB_PASSWORD`.

## MySQL já instalado, sem Docker

Entre no cliente MySQL com um usuário administrador e execute, substituindo a senha de exemplo:

```sql
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE USER 'biblioteca'@'localhost' IDENTIFIED BY 'substitua_por_uma_senha_local';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, REFERENCES ON biblioteca.* TO 'biblioteca'@'localhost';
USE biblioteca;
SOURCE C:/caminho/para/biblioteca/database/schema.sql;
```

`SOURCE` é um comando do cliente `mysql`, não um comando SQL do Workbench. No Workbench, abra `schema.sql`, selecione o banco `biblioteca` e execute o arquivo.

Para outra configuração de conexão, defina antes de iniciar o backend:

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:3306/biblioteca?connectionTimeZone=America/Sao_Paulo'
$env:DB_USER = 'biblioteca'
$senha = Read-Host 'Senha do usuario biblioteca' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $senha).Password
```

Com essas variáveis configuradas, execute `mvn spring-boot:run` dentro de `backend`. Mantenha esse terminal aberto. Em outro terminal, entre em `frontend`, execute `npm ci` e depois `npm run dev`, e abra o endereço exibido pelo Vite. No PowerShell, se `npm` for bloqueado pela política de scripts, use `npm.cmd ci` e `npm.cmd run dev`.

As variáveis configuram `spring.datasource` em `backend/src/main/resources/application.properties`. Não é necessário editar o código nem gravar credenciais pessoais. Se MySQL usar outra porta, ajuste `DB_URL` e, no caso do Docker, o mapeamento no Compose. A API usa `PORT` (padrão 8080); se alterar, defina `API_TARGET` no terminal do frontend antes de iniciar o Vite.

## Regras de negócio

1. Só é possível emprestar se houver exemplar disponível. A disponibilidade é `exemplares − empréstimos sem devolução`, evitando um contador duplicado de estoque.
2. Um leitor pode ter **até três empréstimos ativos**.
3. Qualquer empréstimo atrasado bloqueia novas retiradas para aquele leitor.
4. O mesmo leitor não pode ter duas retiradas ativas do mesmo livro.
5. Leitores inativos não podem retirar livros. Leitores com empréstimos pendentes não podem ser inativados.
6. O prazo é de **14 dias**; o dia previsto ainda está no prazo. O atraso começa no dia seguinte. A referência de calendário é `America/Sao_Paulo`, injetada por `Clock`.
7. Uma devolução só pode ocorrer uma vez e não pode anteceder a retirada.
8. A quantidade de exemplares deve ser positiva e não pode diminuir abaixo do total em uso.
9. ISBN (13 dígitos) e e-mail são únicos. O e-mail é normalizado para minúsculas. O ISBN tem validação de formato, sem cálculo do dígito verificador.

`@Transactional` e bloqueios pessimistas nos registros do leitor e do livro serializam retiradas concorrentes. A ordem é sempre leitor → livro. O domínio contém as regras; os services consultam os repositórios e fornecem as contagens necessárias. A devolução bloqueia o empréstimo para impedir duas devoluções simultâneas.

## Estrutura e responsabilidades

```text
biblioteca/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/br/edu/biblioteca/
│       │   ├── controller/  HTTP, JSON, validação de entrada e status
│       │   ├── service/     Casos de uso, transações e coordenação
│       │   ├── domain/      Entidades encapsuladas e regras de negócio
│       │   ├── repository/ Interfaces Spring Data JPA e consultas
│       │   ├── dto/        Records de entrada e saída
│       │   └── exception/  Tratamento centralizado de erros
│       ├── main/resources/application.properties
│       └── test/           Testes de domínio, HTTP, SQL/JPA e concorrência
├── frontend/src/          React, cliente fetch e CSS
├── database/schema.sql    Modelagem relacional executável
├── docs/                  Modelagem e roteiro de apresentação
├── compose.yaml
└── .env.example
```

Os controllers não acessam o banco. Os DTOs não expõem entidades JPA. As entidades têm construtor protegido para o JPA e métodos públicos para operações válidas, sem setters genéricos. O projeto usa `@ManyToOne` para as duas referências do empréstimo; não cria coleções bidirecionais sem necessidade.

## API REST

| Método | Rota | Operação |
| --- | --- | --- |
| GET | `/api/livros` | Listar livros com disponibilidade |
| GET | `/api/livros/{id}` | Consultar livro |
| POST | `/api/livros` | Cadastrar livro |
| PUT | `/api/livros/{id}` | Atualizar livro |
| GET | `/api/leitores` | Listar leitores |
| GET | `/api/leitores/{id}` | Consultar leitor |
| POST | `/api/leitores` | Cadastrar leitor |
| PUT | `/api/leitores/{id}` | Atualizar nome e e-mail |
| PATCH | `/api/leitores/{id}/atividade` | Ativar/inativar com `{"ativo": false}` |
| GET | `/api/emprestimos` | Listar histórico, mais recentes primeiro |
| GET | `/api/emprestimos/{id}` | Consultar empréstimo |
| POST | `/api/emprestimos` | Registrar retirada |
| PATCH | `/api/emprestimos/{id}/devolucao` | Confirmar devolução, sem corpo |

Cadastro de livro:

```json
{"titulo":"Dom Casmurro","autor":"Machado de Assis","isbn":"9788535914849","categoria":"Romance","exemplares":2}
```

Cadastro de leitor e empréstimo (use os IDs retornados pela API):

```json
{"nome":"Ana Exemplo","email":"ana@example.com"}
```

```json
{"livroId":1,"leitorId":1}
```

Respostas: `201 Created` e `Location` em cadastros; `200 OK` nas consultas/atualizações; `400 Bad Request` para campos e JSON inválidos; `404 Not Found` para recurso inexistente; `409 Conflict` para regra de negócio ou duplicidade.

```json
{
  "timestamp":"2026-11-11T12:00:00Z",
  "status":409,
  "mensagem":"Este livro não possui exemplares disponíveis.",
  "caminho":"/api/emprestimos",
  "campos":{}
}
```

## Testes e build

No backend:

```powershell
mvn test
mvn package
```

Os testes executam o **mesmo `database/schema.sql`** no H2 em modo MySQL e validam o mapeamento Hibernate. Cobrem regras de disponibilidade, limite, atraso, devolução, inativação, atualização de acervo, duplicidade e concorrência. Os testes HTTP sobem a aplicação em porta aleatória e percorrem cadastro → empréstimo → consulta → devolução, além de erros 400/404/409. Não precisam de MySQL nem de credenciais. H2 não substitui a validação final no MySQL real; consulte `docs/APRESENTACAO.md` para o roteiro manual.

O artefato fica em `backend/target/biblioteca-1.0.0.jar`. Para executá-lo, mantenha `DB_PASSWORD` e demais variáveis configuradas e rode `java -jar target/biblioteca-1.0.0.jar`.

No frontend:

```powershell
npm ci
npm run build
npm run preview
```

O build fica em `frontend/dist`. O preview do Vite encaminha `/api` para o backend. Em hospedagem própria, configure um proxy reverso para `/api` no mesmo domínio. A interface usa Arial e CSS simples, sem fontes externas ou bibliotecas de componentes.

## Limites e decisões

- Escopo acadêmico local, sem autenticação ou autorização; não publique a API com dados pessoais sem acrescentar essas camadas.
- Sem exclusão física: editar livros e inativar leitores preserva os históricos. A atividade exige atualização **ou** exclusão e as atualizações estão implementadas.
- Um título/ISBN possui vários exemplares agregados. Não se controla código de barras de cada cópia.
- Consultas retornam listas completas e calculam contagens por registro, suficiente para uma biblioteca pequena. Paginação e consultas agregadas são melhorias para grandes acervos.
- `CREATE TABLE IF NOT EXISTS` permite inicialização repetida, mas não migra esquemas antigos. Mudanças futuras devem ter migrações versionadas.

Veja [a modelagem](docs/MODELAGEM.md) e [o roteiro de apresentação](docs/APRESENTACAO.md).

## Arquivos da entrega

| Requisito | Local no repositório |
| --- | --- |
| Frontend React completo | `frontend/` |
| Backend Spring Boot completo | `backend/` |
| Modelagem e relacionamentos | [docs/MODELAGEM.md](docs/MODELAGEM.md) |
| Estrutura SQL | [database/schema.sql](database/schema.sql) |
| Configuração de persistência | `backend/src/main/resources/application.properties` |
| Testes automatizados | `backend/src/test/` |
| Integrantes e instruções de execução | Este `README.md` |

O banco utilizado na execução normal é MySQL. Cada pessoa configura sua própria senha por variável de ambiente. Não é necessário enviar os dados internos do MySQL; o banco pode ser criado seguindo as instruções e executando o script SQL.
