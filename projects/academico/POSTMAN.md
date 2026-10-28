# Validação da API com Postman

## Atividade 03 - Persistência com JDBC e SQLite

Collection: `postman/Sistema Academico - Atividade 03.postman_collection.json`

Para usar: no Postman, **Import** e selecione o arquivo. A variável `baseUrl` já vem como `http://localhost:8080`.

Antes de começar, prepare o banco e suba a API (veja o `README.md`).

### Antes de fechar o programa

Requisições soltas na Collection, para enviar uma por vez, na ordem:

| Requisição | Status esperado |
|------------|-----------------|
| Cadastrar aluno | 201 |
| Consultar aluno | 200 |
| Cadastrar aluno duplicado | 409 |
| Consultar aluno inexistente | 404 |
| Cadastrar disciplina | 201 |
| Consultar disciplina | 200 |
| Cadastrar disciplina duplicada | 409 |
| Consultar disciplina inexistente | 404 |

### Depois de fechar o programa

Pasta **Executar DEPOIS de reiniciar a aplicação**, com as consultas que provam a persistência:

| Requisição | Status esperado |
|------------|-----------------|
| Consultar aluno cadastrado antes do reinício | 200 |
| Consultar disciplina cadastrada antes do reinício | 200 |

### Ordem para demonstrar a persistência

1. Envie as 8 requisições soltas, uma por vez.
2. Encerre a aplicação e execute-a novamente.
3. Envie as 2 requisições da pasta. Os dois `GET` devem responder `200` com os dados cadastrados no passo 1.

As requisições usam dados fixos (aluno `RA2026100` e disciplina `ALG`), que só podem ser cadastrados uma vez por banco: se o cadastro for enviado de novo, a API responde `409`. Para repetir a demonstração, apague `database/academico.db` e crie o banco novamente (veja o `README.md`).