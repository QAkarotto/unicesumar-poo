---

# Atividade 02 - Primeiros Endpoints com Spring Boot

## O que foi feito

Nesta atividade, desenvolvi alguns endpoints utilizando Spring Boot para trabalhar com o sistema acadêmico.

Os endpoints implementados foram:

- `GET /api/alunos/{identificador}`: busca um aluno pelo RA.
- `GET /api/matriculas/{id}`: busca uma matrícula pelo código.
- `POST /api/matriculas`: realiza o cadastro de uma nova matrícula.

## Testes realizados

Utilizei o Postman para testar os endpoints e verificar se estavam funcionando corretamente.

Nos testes, consegui consultar um aluno, consultar uma matrícula, cadastrar uma matrícula válida e testar uma matrícula com um RA que não existe.

## Regras de negócio

As regras de negócio ficaram nas classes de domínio do projeto, principalmente na classe `OfertaDisciplina`, que é utilizada para realizar as matrículas.

Os controllers ficaram responsáveis por receber as requisições e retornar as respostas da API.

## Materiais utilizados

- Materiais e orientações da atividade disponibilizados pela faculdade.
- Documentação do [Spring Boot](https://docs.spring.io/spring-boot/).
- Documentação do [Spring Web](https://docs.spring.io/spring-framework/reference/web/webmvc.html).
- Postman para testar as requisições.

## Dificuldade encontrada

Uma das dificuldades foi tratar as matrículas com dados inválidos, principalmente quando o RA informado não existe. Para resolver isso, fiz verificações dos dados recebidos e tratei as exceções, retornando o status HTTP adequado para cada situação.

## Collection do Postman
https://www.postman.com/mateushbp1402-1324453/mateus-presner/collection/uhg8zur/atividade-02-spring-boot?action=share&creator=58792600