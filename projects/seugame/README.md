# SeuGame — Arena de Torneios

Projeto da disciplina de Programação Orientada a Objetos e Banco de dados. O sistema permite criar torneios de tabela ou chaveamento, cadastrar participantes, registrar placares e definir o campeão. A versão atual usa React no frontend, uma API REST em Java/Spring Boot e MySQL.

## Integrantes

- Cristian de Oliveira da Silva — RA 25177998-2
- Gustavo Pettenon — RA 26013249-2
- João Henrique Guimarães — RA 25364014-2

## Funcionalidades

- Cadastro, login e gerenciamento de torneios pelo criador.
- Catálogo público com busca e filtros.
- Tabela em turno único, com classificação por pontos, vitórias, saldo, gols marcados e nome.
- Chaveamento eliminatório com BYEs e avanço dos vencedores.
- Correção de placares, recálculo da classificação e atualização das partidas dependentes.
- Estados FUTURO, ANDAMENTO e FINALIZADO; torneios finalizados ficam bloqueados para edição.

A classificação é individual de cada torneio. Não há ranking geral.

## Tecnologias e estrutura

Java 26, Maven 3.9, Spring Boot 4.1.2, Spring Web, Spring Data JPA/Hibernate, Bean Validation, Spring Security, MySQL 8.4, React 19 e Vite 8. Os testes do backend usam JUnit 5.

```text
frontend/              Interface React e cliente HTTP
backend/               API, domínio, repositórios e testes Java
database/schema.sql    Criação das tabelas MySQL
database/modelagem.md  Entidades, relacionamentos e restrições
```

O fluxo principal é React → Controller → Service → domínio/Repository → JPA/Hibernate → MySQL. Os Controllers recebem DTOs; as regras de torneio ficam no domínio e são coordenadas pelos Services. O Hibernate usa `ddl-auto=validate`: o banco deve ser criado pelo script SQL, não pela aplicação. Os contratos HTTP estão em [backend/API.md](backend/API.md).

## Preparar o banco

Instale e inicie o MySQL 8.4. No terminal, a partir da **raiz do projeto**, abra o cliente MySQL com uma conta autorizada a criar banco e tabelas:

```powershell
& 'C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe' -u root -p
```

Em uma instalação nova, execute no cliente:

```sql
CREATE DATABASE seugame CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci;
USE seugame;
SOURCE database/schema.sql;
SHOW TABLES;
```

O script deve ser executado **uma vez em um banco vazio**. Se `seugame` já contém as cinco tabelas (`usuarios`, `torneios`, `participantes`, `partidas`, `classificacao`), não repita a importação. A aplicação precisa de uma conta MySQL com `SELECT`, `INSERT`, `UPDATE` e `DELETE` nesse banco; configure essa conta no arquivo local de ambiente. Não coloque senhas no repositório.

## Iniciar a aplicação

Confira `java --version`, `mvn --version`, `node --version` e `npm --version`. O Maven deve usar o JDK 26. O frontend precisa de Node.js 22.13+ ou 24+.

No primeiro terminal:

```powershell
cd backend
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
notepad .env
mvn spring-boot:run
```

Preencha `DB_NAME`, `DB_USER` e `DB_PASSWORD` com a configuração local. O exemplo usa MySQL em `localhost:3306`; o backend inicia em `http://localhost:8080`. Se `backend/.env` já existir, edite-o sem copiar novamente. Execute o Maven a partir de `backend/` para que o arquivo seja carregado. O perfil `infra` serve apenas ao endpoint de status; use o perfil normal para o sistema completo.

Em outro terminal:

```powershell
cd frontend
npm ci
npm run dev
```

Abra `http://localhost:5173`. O Vite encaminha `/api` ao backend. Use `localhost` nos dois lados para que a sessão funcione. Caso altere a porta do backend, ajuste `API_PROXY_TARGET` em `frontend/.env`, criado a partir de `frontend/.env.example`.

## Testes

```powershell
cd backend
mvn verify
.\scripts\test-mysql.ps1
```

`mvn verify` executa os testes sem banco e gera o JAR. O script de integração cria uma instância MySQL temporária e testa JPA/API com o schema real; requer MySQL 8.4 instalado. Ele não usa o banco configurado em `backend/.env`.

```powershell
cd ..\frontend
npm ci
npm run lint
npm test
npm run build
```

Para conferir manualmente, crie um torneio de tabela com três participantes, registre e corrija um placar, observe a classificação e finalize. Depois crie um chaveamento com cinco participantes e confira os BYEs e o avanço dos vencedores.

Se o backend não iniciar, confira se o serviço MySQL está ativo, se `DB_NAME` aponta para o banco importado e se a conta configurada tem acesso. O erro de validação do Hibernate geralmente indica tabela ausente ou schema diferente do script. `backend/.env` e `frontend/.env` são ignorados pelo Git.
