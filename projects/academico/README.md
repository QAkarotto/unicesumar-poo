# Sistema Acadêmico

Projeto didático em Java 26 para praticar orientação a objetos e testes unitários com JUnit 5.

O domínio representa alunos, turmas, ofertas de disciplinas e matrículas. As regras permanecem nos próprios objetos, sem frameworks ou camadas adicionais.

## Executando a aplicação

No diretório `projects/academico`, compile com:

```bash
mvn compile
```

Depois, execute `br.edu.sistemaacademico.SistemaAcademico` pela IDE.

## Testes unitários

Para executar todos os testes:

```bash
mvn test
```

Para executar apenas os testes de matrícula:

```bash
mvn -Dtest=MatriculaTest test
```

Para executar os testes e verificar a cobertura de código:

```bash
mvn verify
```

O JaCoCo exige no mínimo 80% de cobertura de linhas das classes de domínio. O relatório HTML é gerado em `target/site/jacoco/index.html`. A classe `SistemaAcademico` não entra nessa métrica porque representa o fluxo de demonstração da aplicação.

Os testes ficam em `src/test/java` e cobrem oferta de disciplinas, proteção das coleções, matrículas, mudanças de estado e as regras de aprovação e reprovação do histórico acadêmico.

O workflow **Java Tests** do GitHub Actions executa os testes e verifica a cobertura a cada `push` e `pull_request`.

## API REST

Execute `br.edu.sistemaacademico.SistemaAcademicoApplication` pela IDE. A API sobe em `http://localhost:8080`.

- `GET /api/alunos/{ra}`: consulta um aluno
- `GET /api/matriculas/{codigo}`: consulta uma matrícula
- `POST /api/matriculas`: cria uma matrícula (`{ "registroAcademico": "RA2026002" }`)

Os dados ficam em memória e voltam ao estado inicial quando a aplicação reinicia.

Collection do Postman: https://andreyfogaca1234-6266405.postman.co/workspace/Andrey-Foga%C3%A7a's-Workspace~02691adf-6f1e-45ca-8f48-d22cad1cbd6d/collection/58743241-6f14fdb4-f54d-4f0b-be98-50a847772fc3?action=share&creator=58743241
