# Sistema Acadêmico — API REST com Spring Boot

Projeto didático em Java 26. A API expõe funcionalidades do domínio acadêmico usando Spring Boot e mantém os dados apenas em memória (não há banco de dados).

## Como executar

Requisitos: JDK 26 e Maven. Abra um terminal nesta pasta (`projects/academico`) e execute:

```bash
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. Os dados de exemplo são recriados sempre que a aplicação inicia.

## Endpoints

| Método | Rota | Sucesso | Descrição |
|---|---|---:|---|
| GET | `/api/matriculas/{id}` | 200 | Consulta matrícula pelo código |
| GET | `/api/alunos/{identificador}` | 200 | Consulta aluno pelo RA ou e-mail |
| POST | `/api/matriculas` | 201 | Cria uma matrícula |

Respostas de erro: `400 Bad Request` para corpo inválido ou violação de regra do domínio; `404 Not Found` para aluno, turma, disciplina, oferta ou matrícula inexistente; `409 Conflict` para código de matrícula já utilizado.

### Exemplo de POST `/api/matriculas`

Header: `Content-Type: application/json`

```json
{
  "registroAcademico": "RA2026002",
  "turmaCodigo": "ESOFT4S-NA",
  "disciplinaCodigo": "BD"
}
```

O campo `codigo` é opcional; se omitido, a API gera um código. Para informar um código manualmente:

```json
{
  "codigo": "MAT-2026-010",
  "registroAcademico": "RA2026002",
  "turmaCodigo": "ESOFT4S-NA",
  "disciplinaCodigo": "BD"
}
```

### Dados de exemplo

- Alunos: `RA2026001` (Paola Oliveira) e `RA2026002` (Bruno Santos).
- Turma: `ESOFT4S-NA`, período `2026/1`.
- Disciplinas ofertadas: `POO` e `BD`.
- Matrícula existente: `MAT-2026-001` (Paola em POO).

A tentativa de matricular novamente Paola na mesma oferta de POO é inválida; as regras continuam implementadas nas classes do domínio.

## Postman

Importe `postman/Sistema-Academico-API.postman_collection.json` no Postman. A collection contém consultas válidas, criação de matrícula e cenários de erro. Rode as requisições na ordem, pois a matrícula criada pelo POST passa a existir apenas durante a execução atual da aplicação.

## Testes

```bash
mvn test
mvn verify
```

O segundo comando também verifica a cobertura mínima de 80% configurada para o domínio.
