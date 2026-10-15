# Sistema Acadêmico

Projeto didático em Java para praticar orientação a objetos, testes unitários e, nesta atividade, a criação de uma API REST com Spring Boot.

## Requisitos

- JDK compatível com a versão configurada em `pom.xml` (Java 26).
- Maven.
- Postman para validar as requisições HTTP.

Confira a versão instalada antes de executar:

```bash
java -version
mvn -version
```

## Executar os testes

Na pasta que contém o `pom.xml`:

```bash
mvn clean test
```

Para executar também a verificação de cobertura configurada com JaCoCo:

```bash
mvn verify
```

A configuração exige pelo menos 80% de cobertura de linhas para as classes do domínio. Se algum comando falhar, consulte a primeira mensagem de erro relevante antes de alterar a configuração.

## Executar a API

Na pasta que contém o `pom.xml`:

```bash
mvn spring-boot:run
```

A API usa a porta `8080` por padrão e mantém os dados apenas em memória. Ao reiniciar a aplicação, os dados voltam ao estado inicial.

## Endpoints

| Método | Caminho | Sucesso | Erros esperados |
|---|---|---|---|
| `GET` | `/api/matriculas/{id}` | `200 OK` | `404 Not Found` se a matrícula não existir |
| `GET` | `/api/alunos/{registroAcademico}` | `200 OK` | `404 Not Found` se o aluno não existir |
| `POST` | `/api/matriculas` | `201 Created`, corpo JSON e cabeçalho `Location` | `400 Bad Request` para corpo incompleto; `404 Not Found` para aluno, turma ou oferta inexistente; `409 Conflict` para regra de negócio violada |

Exemplo de JSON para `POST /api/matriculas`:

```json
{
  "registroAcademico": "RA2026003",
  "turma": "ESOFT4S-NB",
  "disciplina": "BD"
}
```

## Organização e regras de negócio

- `SistemaAcademicoApi`: ponto de entrada do Spring Boot.
- `AlunoController` e `MatriculaController`: recebem requisições HTTP e retornam respostas HTTP; não devem duplicar as regras de matrícula.
- `DadosEmMemoria`: mantém os objetos do domínio em memória e localiza alunos, turmas, ofertas e matrículas.
- `NovaMatriculaRequest`: representa o JSON recebido na criação de matrícula.
- `AlunoResponse`, `MatriculaResponse` e `ErroResponse`: definem os formatos JSON de resposta.
- As regras de negócio permanecem nas classes do domínio, como `Aluno`, `Matricula` e `OfertaDisciplina`. O Controller delega a operação ao domínio e traduz violações de regra para `409 Conflict`.

Os objetos de resposta evitam serializar diretamente referências circulares entre aluno e matrículas, que poderiam causar recursão durante a conversão para JSON.

## Materiais consultados

- [Spring Initializr](https://start.spring.io/)
- [Spring Guide — Building a RESTful Web Service](https://spring.io/guides/gs/rest-service)
- [Spring Framework — Annotated Controllers](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html)
- [Spring Framework — Request Mapping](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/reference/)

## Validar com Postman

A Collection fica em `postman/sistema-academico.postman_collection.json`. No Postman, selecione **Import** e escolha esse arquivo. Inicie a API antes de executar as requisições.

A Collection contém oito cenários: consulta de matrícula existente, consulta de aluno existente, criação de matrícula, matrícula inexistente, aluno inexistente, tentativa de matrícula de aluno já aprovado, tentativa de matrícula duplicada e corpo incompleto.

**Link compartilhável da Collection:** `PREENCHER_COM_O_LINK_PUBLICADO_NO_POSTMAN`

Para obter o link, publique/compartilhe a Collection no Postman usando a opção de compartilhamento disponível na sua conta e substitua o texto acima pelo link real. O arquivo JSON local, sozinho, não gera um link público.
