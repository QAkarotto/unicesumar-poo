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

Os testes ficam em `src/test/java` e cobrem oferta de disciplinas, proteção das coleções, matrículas, mudanças de estado e as regras de aprovação e reprovação do histórico acadêmico.

Sugestão de demonstração em aula:

1. Execute `mvn test` e observe todos os testes passando.
2. Remova temporariamente a validação de matrícula duplicada em `OfertaDisciplina`.
3. Execute `mvn test` novamente e observe a falha.
4. Restaure a validação e confirme que os testes voltam a passar.

O workflow **Java Tests** do GitHub Actions executa os mesmos testes a cada `push` e `pull_request`.
