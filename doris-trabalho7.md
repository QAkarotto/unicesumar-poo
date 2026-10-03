# Trabalho 7 - Postman e ServeRest

Nome: Doris 

## Collection

[Collection "Art ServeTest" no Postman](https://www.postman.com/liy1iu-8516682/trabalho-2bim/collection/naix1uj/art-servetest?action=share&creator=58704955)

## Requisições

- POST /usuarios - Status: 201 Created
- POST /login - Status: 200 OK
- POST /produtos - Status: 201 Created
- Consulta do produto (GET /produtos/{_id}) - Status: 200 OK
- Cenário inválido (POST /produtos sem token) - Status: 401 Unauthorized

## Cenário inválido

dupliquei o cadastro de produto (POST /produtos) sem enviar o header authorization. a API recusou a requisição com status 401 unauthorized e uma mensagem informando que o token tava ausente, inválido ou não existia mais

## Conclusão

com o postman, aprendi melhor como fazer e testar requisições na api. entendi a diferença entre GET e POST, como olhar os status codes e as respostas pra saber se deu certo ou não. também entendi melhor como funciona o token do login e que ele precisa ser passado no authorization pra conseguir acessar algumas rotas, como a de cadastro de produtos