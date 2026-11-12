# VetAgenda — Sistema de Agenda de Clínica Veterinária

Projeto acadêmico com Java 26, Spring Boot 4.1.2, Spring Web MVC, Spring Data JPA, MySQL e ReactJS.

## Requisitos
- JDK 26
- Maven 3.6.3+
- MySQL 8+
- Node.js 20+ e npm

## 1. Banco de dados
1. Inicie o MySQL.
2. Execute `database/schema.sql` no MySQL Workbench ou cliente SQL.
3. Configure usuário e senha em `backend/src/main/resources/application.properties`, ou defina as variáveis `DB_USERNAME` e `DB_PASSWORD`.

Banco padrão: `vetagenda`.

## 2. Backend
No terminal:
```bash
cd backend
mvn spring-boot:run
```
API: `http://localhost:8080`

## 3. Frontend
Em outro terminal:
```bash
cd frontend
npm install
npm run dev
```
Frontend: `http://localhost:5173`

## Endpoints principais
- `GET/POST /api/tutores`
- `GET/PUT/DELETE /api/tutores/{id}`
- `GET/POST /api/animais`
- `GET/PUT/DELETE /api/animais/{id}`
- `GET/POST /api/veterinarios`
- `GET/POST /api/especialidades`
- `GET/POST /api/agendamentos`
- `GET/PUT /api/agendamentos/{id}`
- `PATCH /api/agendamentos/{id}/cancelar`
- `POST /api/atendimentos`

## Regras de negócio implementadas
1. Não permite agendamento em data/hora passada.
2. Não permite dois agendamentos ativos para o mesmo veterinário no mesmo horário.
3. Só permite cancelar agendamentos que ainda não foram realizados ou cancelados.
4. Não permite registrar atendimento para agendamento cancelado.
5. Cada animal deve estar vinculado a um tutor.
6. Cada veterinário deve estar vinculado a uma especialidade.

## Observações
- O script `database/schema.sql` documenta a estrutura relacional. O Hibernate está configurado com `ddl-auto=validate`, portanto não cria tabelas automaticamente.
- Para simplificar o projeto acadêmico, a API não implementa autenticação. Não utilize dados reais/sensíveis de pacientes em ambiente público.
- Os dados do formulário React são exemplos para demonstração. Para testar o cadastro, primeiro cadastre especialidade, tutor, animal e veterinário na API (ou use os formulários disponíveis).
