# Endpoints Spring Boot — Sistema Acadêmico

A API usa dados em memória, sem banco de dados.

- `GET http://localhost:8080/api/matriculas/MAT-001`
- `GET http://localhost:8080/api/alunos/RA2026001`
- `POST http://localhost:8080/api/matriculas`

Para o POST, selecione `Body > raw > JSON` no Postman:
```json
{
  "codigo": "MAT-002",
  "identificadorAluno": "RA2026001",
  "codigoTurma": "POO-NA"
}
```

Casos de erro:
- `GET /api/matriculas/INEXISTENTE` deve retornar 404.
- `GET /api/alunos/INEXISTENTE` deve retornar 404.
- Código de matrícula repetido ou aluno/turma inexistente deve retornar 400.
