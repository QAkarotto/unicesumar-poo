# Atividade 02 – Primeiros Endpoints com Spring Boot

## Endpoints implementados

- `GET /api/alunos/{identificador}` — consulta um aluno pelo identificador.
- `GET /api/matriculas/{id}` — consulta uma matrícula pelo ID.
- `POST /api/matriculas` — realiza uma nova matrícula.

## Materiais utilizados na pesquisa

- Documentação oficial do Spring Boot: https://docs.spring.io/spring-boot/
- Guia oficial para criação de serviços REST: https://spring.io/guides/gs/rest-service/
- Documentação oficial do Spring Web: https://docs.spring.io/spring-framework/reference/web/webmvc.html

## Onde ficaram as regras de negócio

As regras de negócio relacionadas à matrícula ficam nas classes do domínio, especialmente em `OfertaDisciplina` e nas demais classes acadêmicas. O `MatriculaController` recebe as requisições HTTP e conecta os endpoints às funcionalidades do sistema.

## Dificuldade encontrada e solução

Uma dificuldade foi configurar o projeto existente para executar a API com Spring Boot. A solução foi ajustar o `pom.xml`, adicionar as dependências necessárias e configurar a aplicação para disponibilizar os endpoints REST. Também foram realizados testes para verificar os resultados das requisições e o tratamento de erros.

## Collection do Postman

Collection utilizada para validar a API:

https://mellinho454-7655059.postman.co/workspace/mellinho's-Workspace~3c94fc45-9ecb-40c1-87f6-cd927d674d6a/collection/58795040-06a1e4c9-4bfb-4342-b874-759066325b98?action=share&source=copy-link&creator=58795040
