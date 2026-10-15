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

O domínio continua sem dependência de framework. A API fica no pacote `br.edu.sistemaacademico.api` e apenas traduz HTTP para chamadas ao domínio.

### Executando a API

No diretório `projects/academico`:

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Os dados ficam apenas em memória: ao reiniciar, voltam ao estado inicial (3 alunos, 3 turmas e as matrículas `MAT-001` a `MAT-003`).

### Endpoints

| Método | Caminho | Sucesso | Erros |
|---|---|---|---|
| GET | `/api/matriculas/{id}` | 200 | 404 matrícula inexistente |
| GET | `/api/alunos/{registroAcademico}` | 200 | 404 aluno inexistente |
| POST | `/api/matriculas` | 201 + `Location` | 400 corpo incompleto, 404 aluno/turma/disciplina inexistente, 409 regra do domínio violada |

Exemplo de corpo do `POST /api/matriculas`:

```json
{
  "registroAcademico": "RA2026003",
  "turma": "ESOFT4S-NB",
  "disciplina": "BD"
}
```

### Onde ficam as regras

- Regras de matrícula (aluno já aprovado, matrícula duplicada na oferta): `Matricula`, `Aluno` e `OfertaDisciplina`, no pacote `domain`.
- `MatriculaController` e `AlunoController`: recebem a requisição, chamam o domínio e escolhem o status HTTP. Uma exceção do domínio vira `409 Conflict`.
- `DadosEmMemoria`: guarda os objetos em `Map`s (sem banco de dados) e gera o código da matrícula.
- Records `*Response` e `NovaMatriculaRequest`: definem o formato do JSON. Existem porque `Matricula` e `Aluno` se referenciam mutuamente, e converter o domínio direto para JSON causaria loop infinito.

### Collection do Postman

Importação: abra o Postman, clique em Import e selecione o arquivo `postman/sistema-academico.postman_collection.json` deste módulo.

O arquivo `postman/sistema-academico.postman_collection.json` pode ser importado no Postman (Import) e já traz testes automáticos de status.
