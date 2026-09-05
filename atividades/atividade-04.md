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

## Encapsulamento e proteção do estado

Cada classe deve receber, no momento da criação, as informações necessárias para representar um objeto válido. Informações obrigatórias não podem ser nulas ou vazias, a carga horária deve ser positiva e o período letivo deve aceitar somente semestres válidos.

O e-mail do aluno deve ser obrigatório e possuir um formato minimamente válido. Uma tentativa de atribuir um e-mail vazio ou inválido não pode alterar o estado do aluno.

Sempre que a criação ou uma alteração receber dados inválidos, lance `IllegalArgumentException` com uma mensagem que indique o problema.

Decida quais informações podem mudar e quais devem permanecer iguais durante toda a vida do objeto. Disponibilize métodos de acesso quando forem necessários e métodos de alteração somente quando a mudança fizer sentido. Não crie getters e setters de maneira automática para todos os atributos: qualquer alteração permitida deve continuar respeitando as regras da classe.

Todas as classes devem possuir uma representação textual clara para que o cenário fornecido consiga exibir seus objetos de forma compreensível.

As regras pertencem às classes do domínio. `SistemaAcademico` deve apenas criar os objetos, fazê-los colaborar e apresentar o resultado.

## Resultado esperado

Ao concluir, o cenário fornecido deve compilar e executar por completo. Objetos válidos devem ser criados normalmente, enquanto tentativas de criação ou alteração com dados inválidos devem ser rejeitadas sem deixar o objeto em um estado inconsistente.

## Entrega

A entrega deve ser feita por **Pull Request**. Na descrição do PR, responda brevemente:

- Quais validações foram adicionadas para proteger os objetos?
- Quais informações podem ser alteradas e quais permanecem imutáveis?
- Como os métodos de acesso e alteração ajudam a preservar o estado dos objetos?

Não é necessário entregar um relatório separado.

## Restrições

Nesta atividade, não adicione novas camadas, interfaces, herança, banco de dados, Spring ou frameworks adicionais. O objetivo é criar e encapsular somente o modelo solicitado.
