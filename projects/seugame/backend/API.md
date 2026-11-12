# API REST

Base local: `http://localhost:8080/api`. O frontend usa `/api` pelo proxy do Vite. As respostas são JSON, exceto nos casos de status 204. O criador do torneio é identificado pela sessão, não por um ID enviado no formulário.

## Autenticação

Cadastro e login criam uma sessão por cookie HttpOnly. Antes de qualquer POST, PUT, PATCH ou DELETE, o cliente obtém o token em `GET /api/auth/csrf` e envia o cabeçalho indicado na resposta. Após cadastro, login ou logout, deve buscar outro token. O frontend implementa esse fluxo em [api.js](../frontend/src/services/api.js).

| Método | Caminho | Acesso |
| --- | --- | --- |
| GET | `/api/status` | Público |
| GET | `/api/auth/csrf` | Público |
| POST | `/api/auth/cadastro` | Público, com CSRF |
| POST | `/api/auth/login` | Público, com CSRF |
| POST | `/api/auth/logout` | Sessão e CSRF |
| GET, PATCH | `/api/usuarios/me` | Sessão; PATCH exige CSRF |
| GET | `/api/usuarios/me/torneios` | Sessão |
| GET, POST | `/api/torneios` | GET público; POST exige sessão e CSRF |
| GET, PUT, DELETE | `/api/torneios/{id}` | GET público; alterações restritas ao criador |
| GET | `/api/torneios/{id}/partidas` | Público |
| GET | `/api/torneios/{id}/classificacao` | Público; torneio de tabela |
| GET | `/api/torneios/{id}/chaveamento` | Público; torneio de chaveamento |
| PUT | `/api/torneios/{id}/partidas/{partidaId}/resultado` | Criador e CSRF |
| POST | `/api/torneios/{id}/finalizacao` | Criador e CSRF |

`GET /api/torneios` aceita `busca`, `tipo` e `status` como filtros opcionais. Não existe endpoint de ranking geral.

## Exemplos de entrada

Cadastro: `nome`, `usuario`, `email`, `senha`, `confirmarSenha`. Login: `identificador` (usuário ou e-mail) e `senha`. Alteração de perfil: `nome` e `email`.

Criação e edição de torneio usam o mesmo formato:

```json
{
  "nome": "Copa da turma",
  "descricao": "",
  "tipo": "TABELA",
  "dataInicio": "2035-01-01",
  "participantes": ["Ana", "Bruno", "Caio"]
}
```

São necessários pelo menos três participantes. Nomes repetidos no mesmo torneio são recusados após remover espaços nas extremidades e ignorar maiúsculas/minúsculas. A edição descritiva preserva o sorteio; mudanças estruturais obedecem ao estado e aos resultados existentes.

Para registrar placar, envie `{ "placar1": 2, "placar2": 1 }`. Os valores devem ser inteiros de 0 a 999. Chaveamentos não aceitam empate. A finalização não recebe um campeão no corpo: o backend o determina pelos resultados.

## Respostas e erros

Criação retorna 201, exclusão e logout retornam 204, e consultas/alterações bem-sucedidas retornam 200. Erros de validação retornam 400; falta de sessão, 401; falta de permissão ou CSRF inválido, 403; recurso inexistente, 404; duplicidade ou operação incompatível com o estado, 409. Uma escrita anônima sem CSRF pode receber 403 antes da verificação de sessão. Erros usam `application/problem+json` e não expõem credenciais ou stack traces.

O schema e os relacionamentos estão em [database/modelagem.md](../database/modelagem.md). Para executar e testar, consulte o [README principal](../README.md).
