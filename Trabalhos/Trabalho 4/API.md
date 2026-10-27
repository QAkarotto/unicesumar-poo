# Manual da API — Sistema Acadêmico

Endereço base: `http://localhost:8080`

Todas as requisições com corpo usam **JSON** (no Postman: Body → raw → JSON).
As mensagens de erro voltam como **texto simples** no corpo da resposta.

## Como rodar

1. Abra o projeto no IntelliJ.
2. Confira se o *Working directory* da execução é a pasta `Trabalho 4` (é onde fica a pasta `database`).
3. Execute `SistemaAcademicoApplication`.
4. Espere aparecer `Started SistemaAcademicoApplication` no console.

## Resumo dos endpoints

| Método | URL | O que faz |
|---|---|---|
| POST | `/api/alunos` | Cadastra um aluno |
| GET | `/api/alunos/{ra}` | Consulta um aluno pelo RA |
| POST | `/api/disciplinas` | Cadastra uma disciplina |
| GET | `/api/disciplinas/{codigo}` | Consulta uma disciplina pelo código |
| POST | `/api/matriculas` | Matricula um aluno em uma oferta de disciplina |
| GET | `/api/matriculas/{id}` | Consulta uma matrícula pelo id |

---

## Alunos

### Cadastrar aluno

`POST /api/alunos`

```json
{
  "ra": "RA2026010",
  "nome": "Maria Souza",
  "email": "maria.souza@email.com"
}
```

| Campo | Regra |
|---|---|
| `ra` | Obrigatório. Deve começar com `RA` (ex.: `RA2026010`) |
| `nome` | Obrigatório |
| `email` | Obrigatório |

| Status | Quando |
|---|---|
| 201 Created | Aluno cadastrado. Retorna o aluno salvo |
| 400 Bad Request | Campo faltando ou RA inválido |
| 409 Conflict | Já existe aluno com esse RA |

### Consultar aluno

`GET /api/alunos/{ra}` — exemplo: `GET /api/alunos/RA2026001`

O RA precisa ser escrito exatamente como foi cadastrado (maiúsculas e minúsculas fazem diferença).

| Status | Quando |
|---|---|
| 200 OK | Aluno encontrado |
| 404 Not Found | Nenhum aluno com esse RA |

Resposta de exemplo:

```json
{
  "ra": "RA2026001",
  "nome": "Paola Oliveira",
  "email": "paola.oliveira@email.com"
}
```

---

## Disciplinas

### Cadastrar disciplina

`POST /api/disciplinas`

```json
{
  "codigo": "LP",
  "nome": "Lógica de Programação",
  "cargaHoraria": 60
}
```

| Campo | Regra |
|---|---|
| `codigo` | Obrigatório |
| `nome` | Obrigatório |
| `cargaHoraria` | Obrigatório. Número inteiro maior que zero |

| Status | Quando |
|---|---|
| 201 Created | Disciplina cadastrada. Retorna a disciplina salva |
| 400 Bad Request | Campo faltando ou carga horária menor ou igual a zero |
| 409 Conflict | Já existe disciplina com esse código |

### Consultar disciplina

`GET /api/disciplinas/{codigo}` — exemplo: `GET /api/disciplinas/POO`

| Status | Quando |
|---|---|
| 200 OK | Disciplina encontrada |
| 404 Not Found | Nenhuma disciplina com esse código |

Resposta de exemplo:

```json
{
  "codigo": "POO",
  "sigla": "POO",
  "nome": "Programação Orientada a Objetos",
  "cargaHoraria": 80
}
```

---

## Matrículas

### Criar matrícula

`POST /api/matriculas`

```json
{
  "raAluno": "RA2026001",
  "codigoOferta": "ESOFT4S-NB-POO"
}
```

O `codigoOferta` é formado por `{código da turma}-{código da disciplina}`. Ofertas disponíveis:

| codigoOferta | Disciplina |
|---|---|
| `ESOFT4S-NB-POO` | Programação Orientada a Objetos |
| `ESOFT4S-NB-BD` | Banco de Dados |

Alunos que podem ser matriculados: `RA2026001` (Paola Oliveira) e `RA2026002` (Bruno Santos).

| Status | Quando |
|---|---|
| 201 Created | Matrícula criada. Retorna a matrícula |
| 400 Bad Request | Aluno não encontrado, oferta não encontrada, aluno já matriculado nessa oferta ou aluno já aprovado na disciplina |

### Consultar matrícula

`GET /api/matriculas/{id}` — exemplo: `GET /api/matriculas/MAT-RA2026002-POO`

O id segue o formato `MAT-{RA do aluno}-{código da disciplina}`.

| Status | Quando |
|---|---|
| 200 OK | Matrícula encontrada |
| 404 Not Found | Retorna a mensagem `Matrícula não encontrada!!` |

Resposta de exemplo:

```json
{
  "id": "MAT-RA2026002-POO",
  "aluno": {
    "ra": "RA2026002",
    "nome": "Bruno Santos",
    "email": "bruno.santos@email.com"
  },
  "oferta": {
    "codigo": "ESOFT4S-NB-POO",
    "disciplina": {
      "codigo": "POO",
      "sigla": "POO",
      "nome": "Programação Orientada a Objetos",
      "cargaHoraria": 80
    }
  },
  "resultado": "CURSANDO"
}
```

---

## Onde os dados ficam salvos

| Recurso | Armazenamento | Continua após reiniciar? |
|---|---|---|
| Alunos | SQLite (`database/sistema-academico.db`) | Sim |
| Disciplinas | SQLite (`database/sistema-academico.db`) | Sim |
| Matrículas | Memória (`DadosEmMemoria`) | Não |

As matrículas usam uma lista própria de alunos e ofertas em memória. Por isso, um aluno cadastrado pelo `POST /api/alunos` ainda não pode ser matriculado: só os alunos `RA2026001` e `RA2026002` estão nessa lista.

Dados iniciais do banco (inseridos na primeira execução):

- Alunos: `RA2026001` (Paola Oliveira), `RA2026002` (Bruno Santos)
- Disciplinas: `POO` (Programação Orientada a Objetos), `BD` (Banco de Dados)

Para zerar o banco, pare a aplicação e apague `database/sistema-academico.db`. Ele é recriado com os dados iniciais na próxima execução.
