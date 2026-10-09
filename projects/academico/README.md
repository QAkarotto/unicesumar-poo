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

## API REST (Atividade 02 - 2BIM)

Para subir a API em `http://localhost:8080`:

```bash
mvn spring-boot:run
```

| Método | Caminho | Sucesso | Falhas |
|---|---|---|---|
| GET | `/api/matriculas/{codigo}` (ex.: `MAT-001`) | 200 | 404 se não existir |
| GET | `/api/alunos/{registroAcademico}` (ex.: `RA2026001`) | 200 | 404 se não existir |
| POST | `/api/matriculas` | 201 + header `Location` | 404 aluno/oferta inexistente, 400 matrícula repetida na oferta, 409 aluno já aprovado |

Corpo do POST:

```json
{
  "registroAcademico": "RA2026002",
  "turma": "ESOFT4S-NB",
  "disciplina": "BD"
}
```

Os dados ficam em memória (`api/DadosAcademicos`) e são os mesmos do exemplo em `SistemaAcademico`. As regras de matrícula continuam no domínio: o `MatriculaController` só localiza o aluno e a oferta e chama `oferta.matricular(...)`. As exceções do domínio viram status HTTP em `api/TratamentoDeErros`.

### Postman

Collection publicada: LINK_DA_COLLECTION

O arquivo `postman/sistema-academico.postman_collection.json` pode ser importado no Postman.
