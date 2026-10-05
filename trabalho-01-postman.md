# Trabalho 01 - 2BIM: Explorando uma API REST com Postman

Nome: GUSTAVO GABRIEL DOS SANTOS 

## Collection

Link da Collection no Postman:

https://gugabriel513-6196635.postman.co/workspace/Gustavo's-Workspace~c4d1160a-b9bd-4fcb-8a49-9c9a82be97b3/collection/58739657-ea97a772-b271-4c01-b65a-8097883dc7cc?action=share&creator=58739657

## Requisições realizadas

### 1. Cadastrar usuário

Método: POST

Endpoint: /usuarios

Foi criado um usuário fictício com nome, email, senha e administrador.

Status da resposta: 201 Created

### 2. Login

Método: POST

Endpoint: /login

Foi realizado o login utilizando o email e a senha cadastrados anteriormente.

A API retornou um token de autorização, que foi utilizado nas requisições que precisavam de autenticação.

Status da resposta: 200 OK

### 3. Cadastrar produto

Método: POST

Endpoint: /produtos

Foi cadastrado um produto utilizando o token de autorização obtido no login.

Status da resposta: 201 Created

### 4. Consultar produto

Método: GET

Endpoint: /produtos/:id

Foi realizada a consulta do produto utilizando o ID retornado no cadastro.

Status da resposta: 200 OK

### 5. Cenário inválido

Método: POST

Endpoint: /produtos

Foi realizado um teste de cadastro de produto utilizando uma autenticação inválida.

A API recusou a requisição e retornou uma mensagem informando que o token de acesso estava ausente, inválido, expirado ou que o usuário do token não existia.

Status da resposta: 401 Unauthorized

Esse teste demonstrou que é necessário possuir uma autenticação válida para cadastrar produtos.

## Conclusão

Neste trabalho foi possível aprender a utilizar o Postman para testar uma API REST.

Foram realizadas requisições utilizando os métodos POST e GET, além da configuração de corpo da requisição e autenticação.

Também foi possível entender o funcionamento do login e do token de autorização, utilizando o token para realizar uma operação protegida.

Por fim, foi realizado um cenário inválido para verificar como a API responde quando a autenticação não é válida.
