## Collection

[Link para a Collection no Postman](https://reksuat-f0c18eca-4590243.postman.co/workspace/Priscila's-Workspace~95953261-1acd-4640-8357-e415f1cf6dcb/collection/58738953-27573771-85ae-4118-b9fd-c09705178d88?action=share&source=copy-link&creator=58738953)

## Requisições

- POST /usuarios - Status: 201 Created
- POST /login - Status: 200 OK
- POST /produtos - Status: 201 Created
- Consulta do produto - Status: 200 OK
- Cenário inválido - Status: 400 Bad Request

## Cenário inválido

Foi executado uma tentativa de consultar produto inválido e com o Id muito curto (`POST /produtos/teste`), a resposta foi o erro 400 Bad Request e a mensagem `"id": "id deve ter exatamente 16 caracteres alfanuméricos"`

## Conclusão

execução de requisições HTTP (GET e POST) e o fluxo de autenticação do usuário para cadastrar um produto, ver o mesmo com o ID.
