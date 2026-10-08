# API REST do Sistema Acadêmico

A aplicação sobe com Spring Boot e guarda os dados só em memória. As regras de matrícula continuam em `OfertaDisciplina` e `Aluno`.

## Como executar

O projeto Maven fica na pasta `sistema-academico`, junto com o `pom.xml` e o wrapper (`mvnw.cmd`). Comentários e mensagens do wrapper estão em português; o arquivo `.mvn/jvm.config` pede saída do Maven em pt-BR quando o JDK permitir. Detalhes em `sistema-academico/.mvn/wrapper/LEIA-ME.txt`. Entre na pasta e rode:

```powershell
cd sistema-academico
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

A API fica em `http://localhost:8080`. Ao encerrar o processo, as matrículas criadas durante a execução deixam de existir.

## Collection do Postman

Importe o arquivo local [`postman/Sistema-Academico.postman_collection.json`](postman/Sistema-Academico.postman_collection.json).

No Postman: **Import**, **File**, e selecione esse JSON. A variável `baseUrl` já aponta para `http://localhost:8080`.

A collection cobre:

- consulta da matrícula `MAT-001`;
- consulta do aluno `RA-1001`;
- criação da matrícula `MAT-010` para a aluna `RA-1003`;
- consulta da matrícula inexistente `MAT-999`;
- tentativa de matricular o aluno `RA-1002` de novo em POO, recusada pelo domínio.

A criação usa o código `MAT-010`. Para repetir esse pedido com sucesso, reinicie a aplicação.

## Dados iniciais

| Identificador | Quem | Situação |
| --- | --- | --- |
| `RA-1001` | Marina Costa | matrícula `MAT-001` em andamento em POO, turma `ADS-2026-1` (2026/1) |
| `RA-1002` | Pedro Alves | matrícula `MAT-000` com resultado `APROVADO` em POO, turma `ADS-2025-2` |
| `RA-1003` | Julia Nunes | sem matrícula; pode cursar POO na turma `ADS-2026-1` |

## Endpoints

`GET /api/matriculas/{codigo}`

Retorna a matrícula. Se o código não existir, responde `404`.

`GET /api/alunos/{identificador}`

O identificador é o RA (`identificadorAcademico`). Se não existir, responde `404`.

`POST /api/matriculas`

Corpo JSON:

```json
{
  "codigo": "MAT-010",
  "identificadorAluno": "RA-1003",
  "codigoTurma": "ADS-2026-1",
  "codigoDisciplina": "POO"
}
```

Sucesso: `201`, com o cabeçalho `Location` apontando para a matrícula criada.

| Situação | Status |
| --- | --- |
| Aluno, turma ou disciplina não encontrados | `404` |
| Código de matrícula vazio ou outro dado obrigatório em branco | `400` |
| Aluno já matriculado na oferta, já aprovado na disciplina, ou código de matrícula repetido | `409` |

## Onde ficam as regras

A `PontedaApi` (ponte da API) localiza aluno e oferta e chama `OfertaDisciplina.matricular`. Essa classe é quem impede matrícula duplicada na mesma oferta e nova matrícula depois da aprovação. `Aluno.jaAprovadoEm` participa dessa segunda regra. O controller não reimplementa essas decisões.
