# Atividade 06: Testes Unitários com JUnit

## Objetivo

Criar testes unitários para o **Sistema Acadêmico** utilizando **JUnit 5**, verificando os principais comportamentos e regras de negócio implementados nas atividades anteriores.

## Atividade

Implemente testes para as classes do pacote:

```text
br.edu.sistemaacademico.domain
```

Os testes devem verificar comportamentos relevantes do sistema, incluindo situações válidas e inválidas.

Considere, entre outras, regras relacionadas a:

- oferta de disciplinas;
- matrículas;
- matrícula duplicada;
- aprovação e reprovação;
- nova matrícula após reprovação;
- bloqueio de nova matrícula após aprovação;
- alterações de estado;
- exceções geradas por operações inválidas.

Priorize **testes de comportamento e regras de negócio**. Não crie testes apenas para getters, setters ou métodos triviais.

Utilize recursos do JUnit quando pertinentes, como:

```java
@Test
assertEquals(...)
assertTrue(...)
assertFalse(...)
assertThrows(...)
```

Organize os testes de forma clara, seguindo preferencialmente a estrutura:

**Arrange → Act → Assert**

## Cobertura

A suíte de testes deve atingir no mínimo **80% de cobertura de linhas** do código de domínio.

A cobertura deve representar testes relevantes. Não crie testes artificiais apenas para aumentar o percentual.

## Pipeline

Todos os testes devem ser executados com sucesso pelo **GitHub Actions** disponibilizado no projeto.

A entrega somente será considerada concluída quando:

- os testes executarem localmente com `mvn test`;
- a cobertura mínima for atingida;
- a pipeline do GitHub Actions finalizar com sucesso.

## Entrega

Realize a atividade em uma branch própria e envie por **Pull Request**.

Na descrição do Pull Request, informe brevemente:

1. quais comportamentos principais foram testados;
2. qual cobertura foi atingida;
3. qual regra do sistema você considera mais importante manter protegida por testes.