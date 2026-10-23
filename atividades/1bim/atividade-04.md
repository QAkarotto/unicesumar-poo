# Atividade 04: Criação e Encapsulamento do Domínio

## Objetivo

Crie do zero o domínio do Sistema Acadêmico, aplicando encapsulamento para que cada objeto controle o próprio estado e permaneça válido durante seu uso.

## Ponto de partida

O projeto possui apenas `SistemaAcademico`, com um cenário pronto que utiliza as classes ainda não implementadas. Observe esse código para identificar como os objetos são criados, consultados, alterados e apresentados.

Os erros de compilação iniciais são esperados. Não remova partes do cenário fornecido para eliminá-los.

## Classes do domínio

Crie as classes no pacote `br.edu.sistemaacademico.domain`.

### `Aluno`

Atributos: identificador acadêmico, nome e e-mail.

### `Disciplina`

Atributos: código, nome e carga horária.

### `PeriodoLetivo`

Atributos: ano e semestre.

### `Turma`

Atributos: código, disciplina e período letivo.

### `Matricula`

Atributos: código, aluno e turma.

## Enum do domínio

Crie o enum `Semestre` com os valores `PRIMEIRO` e `SEGUNDO`. Utilize esse enum em `PeriodoLetivo` para representar as únicas opções de semestre aceitas pelo sistema.


## Restrições

Nesta atividade, não adicione novas camadas, interfaces, herança, banco de dados, Spring ou frameworks adicionais. O objetivo é criar e encapsular somente o modelo solicitado.
