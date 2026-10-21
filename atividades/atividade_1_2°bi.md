## Collection

[[Link para a Collection no Postman](https://jvargasdev07-2285539.postman.co/workspace/Julio-Vargas's-Workspace~54482241-844a-47e9-88ad-034f4bed47d1/http-request/59061847-c1d8ed85-4380-4c21-ad2d-2a7291ff5689?sideView=agentMode)

## Requisições

- POST /usuarios - Status: 201 Created
- POST /login - Status: 200
- POST /produtos - Status: 201 Created
- Consulta do produto - Status: 200 ok
- Cenário inválido - Status: 401

## Cenário inválido

ao fazer o teste aparaceu a seguinte mensangem "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"
com isso concluir que se não tiver como usuario adm. a api não ira validar o cadastro do produto.

## Conclusão

Com esta atividade, consegui compreender melhor como funcionam as requisições HTTP e como elas utilizam URLs para acessar os recursos de uma API.
Aprendi que o método POST pode ser utilizado para cadastrar usuários e produtos, enquanto o GET permite consultar informações.
Utilizando o Postman, consegui criar e executar as requisições de forma simples e rápida. Durante o cadastro do produto, tive
dificuldade com o token de autenticação e fiquei em dúvida sobre o motivo do erro.
Depois, entendi que não basta configurar a requisição corretamente: também é necessário utilizar um token válido para acessar operações protegidas.
