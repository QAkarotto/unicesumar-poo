# EventManager — Sistema de Gerenciamento de Eventos

## 1. Descrição

O EventManager é uma aplicação Full Stack desenvolvida para gerenciar eventos, participantes e inscrições.

O sistema permite cadastrar eventos, controlar sua capacidade, registrar participantes e realizar inscrições, respeitando as regras de negócio estabelecidas.

O projeto foi desenvolvido como atividade acadêmica, utilizando Programação Orientada a Objetos, API REST, persistência em banco de dados relacional e integração entre frontend e backend.

## 2. Tecnologias utilizadas

**Backend:**
- Java 26
- Spring Boot
- Spring Web
- Spring Data JPA
- Maven
- PostgreSQL

**Frontend:**
- ReactJS
- JavaScript
- HTML5
- CSS3
- Vite

**Testes:**
- JUnit 5
- Mockito
- MockMvc

**Ferramentas:**
- Visual Studio Code
- Postman
- pgAdmin

## 3. Funcionalidades

### Gerenciamento de eventos
- Cadastrar eventos
- Listar eventos
- Consultar eventos por ID
- Editar informações de eventos
- Excluir eventos sem inscrições
- Definir e atualizar a capacidade dos eventos

### Gerenciamento de participantes
- Cadastrar participantes
- Listar participantes
- Consultar participantes por ID
- Editar informações
- Excluir participantes sem inscrições
- Impedir o cadastro de e-mails duplicados

### Gerenciamento de inscrições
- Inscrever participantes em eventos
- Listar inscrições
- Consultar inscrições por evento
- Consultar inscrições por participante
- Cancelar inscrições
- Impedir inscrições duplicadas
- Impedir inscrições em eventos lotados
- Impedir inscrições em eventos encerrados

## 4. Estrutura do projeto

```text
EventManager/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/eventos/
│   │   │   │   ├── controller/
│   │   │   │   ├── exception/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── services/
│   │   ├── App.jsx
│   │   └── App.css
│   └── package.json
└── README.md
```

## 5. Modelo de dados

O sistema utiliza três entidades principais.

**Evento:** representa um evento, contendo nome, descrição, local, data, horário e capacidade.

**Participante:** representa uma pessoa cadastrada, contendo nome e e-mail.

**Inscricao:** representa o vínculo entre um participante e um evento, registrando também a data da inscrição.

### Relacionamentos

- Um evento pode possuir várias inscrições.
- Um participante pode possuir várias inscrições.
- Cada inscrição pertence a um único evento e a um único participante.

A entidade Inscricao representa a associação entre Evento e Participante.

## 6. Regras de negócio

1. Um evento deve possuir nome, local, data e capacidade válida.
2. Um participante deve possuir nome e e-mail válido.
3. Não é permitido cadastrar participantes com e-mails duplicados.
4. Um participante não pode se inscrever duas vezes no mesmo evento.
5. Não são permitidas inscrições quando a capacidade do evento está esgotada.
6. Não são permitidas inscrições em eventos encerrados.
7. A capacidade de um evento não pode ser reduzida abaixo do número de inscritos.
8. Eventos com inscrições não podem ser excluídos.
9. Participantes com inscrições não podem ser excluídos.
10. Inscrições podem ser canceladas.

## 7. Como executar o projeto

### Pré-requisitos

- JDK 26
- PostgreSQL
- Node.js e npm
- Maven Wrapper incluído no backend

### Configurar o banco de dados

No PostgreSQL, crie um banco chamado:

```sql
CREATE DATABASE gerenciamento_eventos;
```

Configure o arquivo `backend/src/main/resources/application.properties` com os dados de conexão do PostgreSQL.

$env:DB_PASSWORD = "SUA_SENHA"

### Executar o backend

Abra um terminal na pasta `backend` e execute, no Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

`http://localhost:8080`

### Executar o frontend

Abra outro terminal na pasta `frontend` e execute:

```bash
npm install
npm run dev
```

A aplicação ficará disponível, normalmente, em:

`http://localhost:5173`

## 8. Endpoints da API REST

### Eventos

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /api/eventos | Listar eventos |
| GET | /api/eventos/{id} | Consultar evento |
| POST | /api/eventos | Cadastrar evento |
| PUT | /api/eventos/{id} | Atualizar evento |
| DELETE | /api/eventos/{id} | Excluir evento |

### Participantes

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /api/participantes | Listar participantes |
| GET | /api/participantes/{id} | Consultar participante |
| POST | /api/participantes | Cadastrar participante |
| PUT | /api/participantes/{id} | Atualizar participante |
| DELETE | /api/participantes/{id} | Excluir participante |

### Inscrições

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /api/inscricoes | Listar inscrições |
| GET | /api/inscricoes/evento/{id} | Listar por evento |
| GET | /api/inscricoes/participante/{id} | Listar por participante |
| POST | /api/inscricoes | Realizar inscrição |
| DELETE | /api/inscricoes/{id} | Cancelar inscrição |

## 9. Testes automatizados

O projeto possui 29 testes automatizados, incluindo testes unitários dos serviços e testes HTTP dos controllers.

Para executar os testes:

```powershell
cd backend
.\mvnw.cmd clean test
```

Os testes verificam operações válidas, validações de dados, regras de negócio e respostas da API REST.

## 10. Integrantes

- Gabriel Afonso Santana Messias

## 11. Finalidade

Projeto acadêmico desenvolvido para aplicar os conhecimentos de Programação Orientada a Objetos, desenvolvimento Full Stack, APIs REST, persistência de dados e testes automatizados.