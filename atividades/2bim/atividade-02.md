# Trabalho 02 - 2BIM : Primeiros Endpoints com Spring Boot

## Objetivo

Pesquisar os conceitos básicos do **Spring Boot** e utilizá-los para disponibilizar algumas funcionalidades do **Sistema Acadêmico** por meio de uma API REST.

Nesta atividade, o uso do Spring Boot **não será apresentado passo a passo em aula**. Parte do trabalho consiste em pesquisar a documentação, compreender os exemplos e aplicar o que foi aprendido no projeto existente.

## Materiais de apoio

Utilize como ponto de partida:

- Spring Initializr: https://start.spring.io/
- Spring Guide — Building a RESTful Web Service: https://spring.io/guides/gs/rest-service
- Spring Framework — Annotated Controllers: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
- Spring Framework — Request Mapping: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
- Spring Boot Reference Documentation: https://docs.spring.io/spring-boot/reference/

Você pode consultar outros materiais, desde que compreenda o código utilizado e seja capaz de explicá-lo.

## Pesquisa inicial

Antes de implementar, pesquise e compreenda:

- o que é Spring Boot;
- para que serve `@SpringBootApplication`;
- para que serve `@RestController`;
- como funcionam `@RequestMapping`, `@GetMapping` e `@PostMapping`;
- como receber valores com `@PathVariable`;
- como receber dados JSON no corpo de uma requisição;
- como o Spring transforma objetos Java em JSON;
- como retornar status HTTP adequados.

Não é necessário entregar um resumo teórico separado. O resultado da pesquisa deve aparecer na implementação e poderá ser discutido durante a apresentação.

## Atividade

Evolua o projeto existente do **Sistema Acadêmico** para disponibilizar uma API REST simples com Spring Boot.

Implemente apenas as funcionalidades abaixo.

### 1. Consultar uma matrícula

Crie uma requisição semelhante a:

`GET /api/matriculas/{id}`

A resposta deve utilizar dados reais do domínio já existente no projeto.

Exemplo:

`GET /api/matriculas/MAT-001`

Caso a matrícula não exista, a API deve retornar um status HTTP adequado.

### 2. Consultar um aluno

Crie uma requisição semelhante a:

`GET /api/alunos/{identificador}`

Utilize o identificador já adotado pelo projeto, como RA ou outro atributo equivalente.

A resposta deve representar um aluno existente no sistema.

### 3. Criar uma matrícula

Crie uma requisição semelhante a:

`POST /api/matriculas`

A requisição deve receber os dados necessários em JSON e utilizar as classes e regras de negócio já existentes no domínio.

**Não replique as regras de matrícula dentro do Controller.**

## Requisitos

A implementação deve:

- utilizar Spring Boot e Spring Web;
- preservar as classes e regras de negócio já existentes;
- manter as regras no domínio, e não nos Controllers;
- receber e retornar dados em JSON;
- utilizar métodos HTTP coerentes;
- retornar status HTTP adequados;
- manter os dados apenas em memória nesta etapa;
- continuar executando os testes existentes.

Não utilize ainda:

- banco de dados;
- JDBC;
- JPA;
- Spring Data;
- Spring Security;
- frontend;
- Lombok;
- Swagger/OpenAPI.

## Organização

A estrutura deve permanecer simples.

É permitido criar Controllers e classes auxiliares pequenas quando realmente necessárias.

Não é obrigatório criar `Service`, `Repository`, DTOs ou outras camadas apenas para seguir um modelo pronto. Caso alguma dessas estruturas seja criada, o grupo deve ser capaz de justificar sua necessidade.

## Validação

Utilize o **Postman** para executar as requisições implementadas.

Crie uma Collection contendo, no mínimo:

- consulta de uma matrícula existente;
- consulta de um aluno existente;
- criação de uma matrícula;
- pelo menos uma requisição com resultado inválido, como consulta de um identificador inexistente ou tentativa de matrícula proibida por uma regra do domínio.

## Entrega

Realize o desenvolvimento em uma branch própria e abra um **Pull Request**.

No Pull Request, informe brevemente:

- quais endpoints foram implementados;
- quais materiais foram utilizados durante a pesquisa;
- onde ficaram as regras de negócio;
- uma dificuldade encontrada durante a implementação e como ela foi resolvida.

Inclua também no repositório a Collection do Postman utilizada para validar a API.

## Apresentação

Na apresentação, demonstre as requisições pelo Postman e esteja preparado para explicar:

- como uma requisição chega ao Controller;
- como os parâmetros ou o JSON são transformados em dados utilizados pelo Java;
- como o Controller utiliza o domínio existente;
- como o retorno é convertido em JSON;
- como a API responde quando uma operação não pode ser realizada.

O objetivo não é decorar anotações do Spring, mas compreender o fluxo entre **requisição HTTP, Controller, objetos Java e resposta HTTP**.
