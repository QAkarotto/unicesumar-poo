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

## API REST (Spring Boot)

Para subir a API em `http://localhost:8080`:

```bash
mvn spring-boot:run
```

Os dados ficam apenas em memória e já vêm com os alunos `RA2026001` (Paola) e `RA2026002` (Bruno), a matrícula `MAT-001` e as turmas `ADSIS4S` (POO e BD) e `ESOFT4S-NA` (POO).

| Método | Rota | Resposta |
|--------|------|----------|
| GET | `/api/matriculas/{id}` | 200, ou 404 se não existir |
| GET | `/api/alunos/{registroAcademico}` | 200, ou 404 se não existir |
| POST | `/api/matriculas` | 201, 400 (campos faltando), 404 (aluno, turma ou disciplina inexistente) ou 409 (regra do domínio) |

Corpo do POST: `{"registroAcademico": "RA2026002", "turma": "ADSIS4S", "disciplina": "BD"}` (`codigo` é opcional).

### Collection do Postman

Link da Collection: _COLE AQUI O LINK PUBLICADO DA COLLECTION_

O arquivo [`postman-collection.json`](postman-collection.json) pode ser importado no Postman.
