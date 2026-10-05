# Atividade 01 - 2BIM: Explorando uma API REST com Postman

## Objetivo

Conhecer o **Postman** e utilizá-lo para criar, organizar e executar requisições HTTP em uma API REST.

A atividade será realizada utilizando a **ServeRest**, uma API que simula uma loja virtual e disponibiliza operações de usuários, autenticação, produtos e carrinhos.

## Materiais de referência

- ServeRest: https://serverest.dev/
- Postman Quick Start: https://learning.postman.com/docs/getting-started/quick-start/
- Postman Collections: https://learning.postman.com/docs/use/use-collections/overview/

Antes de iniciar, pesquise brevemente como utilizar o Postman para:

- criar e salvar requisições;
- definir método HTTP, URL, headers e body;
- visualizar status code e corpo da resposta;
- organizar requisições em uma Collection.

## Atividade

Crie uma **Collection no Postman** para representar o fluxo abaixo utilizando a API ServeRest.

### 1. Cadastrar um usuário administrador

Crie uma requisição para:

`POST /usuarios`

Utilize dados fictícios e cadastre o usuário como administrador.

> Utilize dados próprios para teste. Não use senhas ou informações pessoais reais.

### 2. Realizar login

Crie uma requisição para:

`POST /login`

Utilize o usuário criado anteriormente e identifique o token retornado no campo `authorization`.

### 3. Cadastrar um produto

Crie uma requisição para:

`POST /produtos`

Envie o token obtido no login no header de autorização e cadastre um novo produto.

### 4. Consultar o produto

Crie uma requisição que permita consultar o produto cadastrado, utilizando uma das rotas disponibilizadas pela ServeRest.

### 5. Explorar um cenário inválido

Execute pelo menos **uma requisição inválida**, por exemplo:

- tentar cadastrar um produto sem autenticação;
- realizar login com credenciais inválidas;
- tentar cadastrar novamente um produto com o mesmo nome.

Observe o **status code** e a resposta retornada pela API.

## Organização da Collection

A Collection deve:

- possuir um nome que identifique o aluno;
- conter todas as requisições da atividade;
- utilizar nomes claros para cada requisição;
- manter configurados os bodies e headers necessários;
- estar organizada na ordem do fluxo executado.

Não utilize uma Collection pronta ou importada da documentação da ServeRest. As requisições devem ser criadas pelo aluno no Postman.

## Entrega

A entrega será realizada por meio de uma **Issue no GitHub**.

A Issue deve conter:

1. link para a Collection compartilhada no Postman;
2. lista das requisições implementadas;
3. status code obtido em cada etapa;
4. breve descrição do cenário inválido executado e do resultado observado;
5. uma breve conclusão sobre o que foi aprendido ao utilizar o Postman e consumir uma API REST.

### Modelo

```md
## Collection

[Link para a Collection no Postman]

## Requisições

- POST /usuarios - Status:
- POST /login - Status:
- POST /produtos - Status:
- Consulta do produto - Status:
- Cenário inválido - Status:

## Cenário inválido

Descreva brevemente o que foi executado e qual resposta foi obtida.

## Conclusão

Em poucas linhas, descreva o que você compreendeu sobre requisições HTTP, autenticação e uso do Postman.
```

## Observações

- A ServeRest utiliza autenticação para determinadas operações. O cadastro de produtos exige autenticação de um usuário administrador.
- Os dados da instância online da ServeRest podem ser alterados ou removidos. A avaliação será realizada principalmente pela organização e configuração da Collection e pela documentação registrada na Issue.
- O objetivo da atividade não é automatizar testes, mas compreender o funcionamento básico de uma API REST utilizando uma ferramenta de requisições HTTP.
