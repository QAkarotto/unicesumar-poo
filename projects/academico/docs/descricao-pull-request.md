# Atividade 02 — Primeiros Endpoints com Spring Boot

## O que foi implementado

- `GET /api/matriculas/{id}` para consultar uma matrícula.
- `GET /api/alunos/{registroAcademico}` para consultar um aluno.
- `POST /api/matriculas` para receber um JSON e solicitar a criação de uma matrícula.
- Respostas HTTP para sucesso e falhas: `200`, `201`, `400`, `404` e `409`, conforme o cenário.
- Collection do Postman com oito cenários de validação.

## Onde ficaram as regras de negócio

As regras permanecem nas classes do domínio. O Controller valida a presença dos dados necessários à requisição, localiza os objetos e delega a criação da matrícula ao domínio. Violações das regras de negócio são convertidas em `409 Conflict`.

## Materiais consultados

- Spring Initializr: https://start.spring.io/
- Spring Guide — Building a RESTful Web Service: https://spring.io/guides/gs/rest-service
- Annotated Controllers: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
- Request Mapping: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
- Spring Boot Reference Documentation: https://docs.spring.io/spring-boot/reference/

## Dificuldade técnica e solução

O domínio possui referências entre objetos relacionados, como aluno e matrículas. Retornar diretamente esses objetos poderia causar recursão na conversão para JSON. Para evitar isso, a API usa records de resposta (`AlunoResponse` e `MatriculaResponse`) que selecionam os campos expostos pela API.

## Validação

- [ ] `mvn clean test` executado com sucesso.
- [ ] API iniciada localmente e Collection executada no Postman.
- [ ] Link compartilhável da Collection incluído no README: **PREENCHER**.
