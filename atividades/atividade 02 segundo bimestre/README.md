# Sistema Acadêmico – API REST (Spring Boot)

API simples sobre o domínio acadêmico existente (`Aluno`, `Turma`, `OfertaDisciplina`, `Matricula`...). Dados apenas em memória.

## Endpoints

| Método | Rota | Sucesso | Erros |
|---|---|---|---|
| GET | `/api/matriculas/{id}` | 200 | 404 matrícula inexistente |
| GET | `/api/alunos/{registroAcademico}` | 200 | 404 aluno inexistente |
| POST | `/api/matriculas` | 201 + header `Location` | 400 JSON/campos ausentes, 404 aluno/turma/disciplina inexistente, 422 regra do domínio violada |

Corpo do POST:

```json
{ "registroAcademico": "RA2026002", "turma": "ESOFT4S-NB", "disciplina": "BD" }
```

Erros retornam JSON: `{ "status": 422, "erro": "Unprocessable Entity", "mensagem": "O aluno já foi aprovado nesta disciplina." }`

### Dados iniciais (iguais ao `main` original)

- Alunos: `RA2026001` (Paola), `RA2026002` (Bruno)
- Turmas: `ESOFT4S-NA` (POO, 2025/2), `ESOFT4S-NB` (POO e BD, 2026/1), `ADSIS4S` (POO, 2026/2)
- Matrículas: `MAT-001` (Paola, POO 2025/2, reprovada), `MAT-002` (Paola, POO 2026/1, aprovada), `MAT-003` (Bruno, POO 2026/1, ativa)

## Estrutura

- `domain/` – regras de negócio, **sem alterações**
- `api/MatriculaController`, `api/AlunoController` – só traduzem HTTP <-> domínio
- `api/DadosAcademicos` – armazenamento em memória (mapas); só localiza/guarda objetos
- `api/Dtos` – records de entrada/saída (o domínio tem referências circulares Aluno <-> Matricula, o que quebraria o Jackson)
- `api/TratadorDeErros` – converte exceções em status HTTP (`IllegalArgument/IllegalState` do domínio -> 422)

## Postman

Importe `postman/sistema-academico.postman_collection.json` (variável `baseUrl` = `http://localhost:8080`).
Cada requisição tem um teste de status. A requisição 3 só dá 201 uma vez por execução da API: reinicie a aplicação para repetir.

<img width="1074" height="529" alt="image" src="https://github.com/user-attachments/assets/76790a1f-97ad-42f9-a0d9-a0103cbca1ce" />

<img width="1080" height="786" alt="image" src="https://github.com/user-attachments/assets/e68fd6f5-bb9c-4cfa-af9c-aaa2db42688f" />

