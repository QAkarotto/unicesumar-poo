### Atividade 02 — Primeiros Endpoints com Spring Boot
### Link da Collection do Postman
[Acesse a Collection aqui](https://kauamoraiscontato-6932661.postman.co/workspace/Kaua-Morais's-Workspace~f93045a5-2b1e-4ca9-82fe-bdb3029e903a/collection/58692083-d0de0d32-1530-4ef4-970e-7f76e24795f7?action=share&source=copy-link&creator=58692083)

### Endpoints Implementados

GET /api/alunos/{identificador}: Consulta detalhada de alunos cadastrados utilizando o seu respectivo RA.
GET /api/matriculas/{id}: Consulta de matrículas ativas no sistema por meio de seu código identificador.
POST /api/matriculas: Criação de novas matrículas a partir de dados recebidos no formato JSON.
GET /api/matriculas/MAT-RA121212-POO: Validação de requisição para matrícula inexistente, retornando corretamente o status HTTP 404 Not Found.

### Materiais Utilizados na Pesquisa

Documentação oficial do Spring Boot.
Guia oficial Spring Guide — Building a RESTful Web Service.
Documentação do Spring Framework voltada a controladores e mapeamento de requisições (Request Mapping).
Assistência do Gemini para assimilação conceitual, resolução de erros e suporte técnico geral.

### Regras de Negócio
Todas as regras de negócio foram estritamente preservadas e mantidas nas classes originais do domínio, com destaque para Aluno, Matricula e OfertaDisciplina.

Os controladores (Controllers) atuam de forma enxuta, recebendo as requisições HTTP e utilizando uma classe auxiliar de gerenciamento em memória para acessar os objetos de domínio, sem duplicar qualquer lógica de validação ou de matrícula.

### Dificuldades e Resolução

A maior dificuldade foi adaptar o projeto Maven para funcionar com o Spring Boot sem quebrar os testes. Também foi preciso garantir que as funcionalidades e os endpoints estivessem funcionando corretamente.

Ajustamos a estrutura do projeto e as dependências do Maven. Depois, testamos as funcionalidades e as rotas da API para verificar se tudo estava funcionando como esperado.

### Validação
Testamos a API pelo Postman, verificando consultas, cadastro de novas matrículas em JSON e o tratamento de erros em requisições inválidas ou que não seguiam as regras do sistema.

### Participantes

Kauã Morais: Desenvolvimento e implementação da API REST.
João Vitor Passos: Participação no desenvolvimento, validação de testes e refinamento da atividade.