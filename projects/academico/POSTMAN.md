
# Validação da API - Postman

## Atividade 02 - Primeiros Endpoints com Spring Boot

A API do Sistema Acadêmico foi validada utilizando
o Postman.

## Collection

Link da Collection:
[Collection - Sistema Acadêmico](https://www.postman.com/joaovitorpresner-8948042/atividade-02-joo-vitor/collection/m88qc2i/atividade02?action=share&creator=58769097)

## Requisições utilizadas

1. GET /api/matriculas/{id}
   - Consulta de uma matrícula existente.

2. GET /api/alunos/{identificador}
   - Consulta de um aluno pelo RA.

3. POST /api/matriculas
   - Criação de uma matrícula utilizando JSON.

4. GET /api/alunos/RA999999
   - Consulta inválida para verificar o retorno HTTP 404.

## Execução

A aplicação é executada localmente em:

http://localhost:8080

## Testes automatizados

Comando utilizado:

mvn clean verify

Resultado: BUILD SUCCESS, com 33 testes aprovados
e cobertura mínima exigida pelo JaCoCo atendida.
