# API do ANiVA

Base: `/api`. Requisições e respostas em JSON. A pessoa logada é identificada pelo cookie de sessão (`JSESSIONID`), criado em `/api/auth/cadastro` ou `/api/auth/login`.

Erros têm sempre o formato `{ "mensagem": "..." }`.

| Status | Quando |
|---|---|
| 400 | dados inválidos (nota fora de 1–10, texto vazio, progresso fora de 0–100, situação desconhecida...) |
| 401 | sem sessão ativa ou e-mail/senha incorretos |
| 403 | operação não permitida (apagar comentário de outra pessoa) |
| 404 | obra ou comentário não encontrado |
| 409 | regra de negócio violada (e-mail já cadastrado) |

## Conta

| Método | Rota | Corpo | Resposta |
|---|---|---|---|
| POST | `/auth/cadastro` | `{ nome, email, senha }` | 201 `{ id, nome, email }` (já inicia a sessão) |
| POST | `/auth/login` | `{ email, senha }` | 200 `{ id, nome, email }` |
| POST | `/auth/logout` | | 204 |
| GET | `/auth/me` | | 200 `{ id, nome, email }` ou 204 se ninguém estiver logado |

## Catálogo

| Método | Rota | Resposta |
|---|---|---|
| GET | `/obras` | lista de obras |
| GET | `/obras/{id}` | uma obra (404 se não existir) |

Campos de uma obra: `id`, `title`, `type`, `types`, `year`, `status`, `rating`, `popularity`, `episodes`, `formats` (`{ type, length, note }`), `genres`, `tropes`, `studio`, `author`, `duration`, `warnings`, `platforms` (`{ streaming, reading, note }`, com `{ name, url }`), `characters` (`{ name, role, description }`), `cover`, `banner` e `synopsis`. Campos sem valor são omitidos.

## Lista pessoal (exige login)

| Método | Rota | Corpo | Efeito |
|---|---|---|---|
| GET | `/biblioteca` | | `{ favoritos: [id], pauta: { id: { status, progresso } } }` |
| POST | `/biblioteca/favoritos/{obraId}/alternar` | | favorita ou desfavorita |
| POST | `/biblioteca/pauta/{obraId}/alternar` | | entra na pauta como `a-fazer` ou sai dela |
| PATCH | `/biblioteca/pauta/{obraId}` | `{ status?, progresso? }` | atualiza a pauta |

Todas as rotas de escrita devolvem a biblioteca atualizada. `status` aceita `a-fazer`, `andamento` ou `concluido`. `progresso` é um inteiro de 0 a 100; `concluido` força 100 e `a-fazer` força 0.

## Avaliações

| Método | Rota | Corpo | Resposta |
|---|---|---|---|
| GET | `/obras/{obraId}/avaliacoes` | | `{ idDaPessoa: nota }` |
| PUT | `/obras/{obraId}/avaliacoes` | `{ nota }` (1 a 10) | mapa atualizado (exige login) |
| DELETE | `/obras/{obraId}/avaliacoes` | | mapa atualizado (exige login) |

## Comentários

| Método | Rota | Corpo | Resposta |
|---|---|---|---|
| GET | `/obras/{obraId}/comentarios` | | lista de comentários |
| POST | `/obras/{obraId}/comentarios` | `{ texto, spoiler }` | 201, lista atualizada (exige login) |
| POST | `/obras/{obraId}/comentarios/{id}/curtidas` | | lista atualizada; curte ou remove a curtida (exige login) |
| DELETE | `/obras/{obraId}/comentarios/{id}` | | lista atualizada; só o autor pode apagar (exige login) |

Um comentário tem `id`, `authorId`, `author`, `text`, `spoiler`, `likes` (ids de quem curtiu) e `createdAt` (data ISO).

## Frontend

As funções de `src/services/api.js` mantêm os nomes e argumentos do contrato anterior (`getMediaCatalog`, `getMediaById`, `accountApi`, `feedbackApi`). Os argumentos `user` e `userId` continuam existindo por compatibilidade, mas o servidor os ignora e usa a sessão.
