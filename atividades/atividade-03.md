# Laboratório: Introdução ao Java

Esta atividade prática tem como objetivo a aplicação das estruturas fundamentais e recursos modernos da linguagem Java. O trabalho consiste em implementar as regras de negócio de um sistema de gestão acadêmica básico para a disciplina de Programação Orientada a Objetos (POO).

## Objetivos de Aprendizagem
- Diferenciar os tipos de dados primitivos (`double`, `int`) de tipos de referência (`String`).
- Aplicar a inferência de tipos locais por meio da palavra reservada `var`.
- Estruturar o controle de fluxo do programa utilizando blocos condicionais (`if / else if / else`).
- Implementar o mapeamento de valores através do recurso *Switch Expressions* (disponível a partir do Java 14).

## Instruções de Execução e Versionamento (Git)

O arquivo base para a realização desta atividade encontra-se no repositório oficial da disciplina:
[https://github.com/QAkarotto/unicesumar-poo](https://github.com/QAkarotto/unicesumar-poo)

Caminho do arquivo no projeto: `projects/academico/src/main/java/org/example/SistemaAcademico.java`

Para o desenvolvimento e submissão do código, siga rigorosamente o fluxo de versionamento abaixo:

1. **Sincronização Inicial:** Realize o *clone* do repositório localmente. Caso já possua o projeto clonado, execute um *pull* para garantir que a sua branch `master` esteja atualizada com as últimas modificações.
2. **Criação da Branch:** A partir da branch `master`, crie uma nova branch para desenvolver sua solução utilizando o padrão de nomenclatura `feature/turma/nome_sobrenome` (Exemplo: `feature/esoft5s/joao_silva`).
3. **Ambiente de Desenvolvimento:** Abra o projeto na IDE de sua preferência. Certifique-se de que o ambiente está configurado com o JDK 14 ou superior.
4. **Análise de Código:** O arquivo fonte contém um método chamado `mostrarExemplosDaAula()`. Recomenda-se a execução inicial do programa para revisar os conceitos de sintaxe abordados na teoria.
5. **Implementação:** Localize no código as marcações indicadas pelo comentário `// TODO`. Insira a lógica de programação correspondente à resolução de cada exercício descrito na seção a seguir. Valide o funcionamento executando o método `main`.
6. **Submissão (Pull Request):** Após concluir a implementação e validar os testes locais, realize o *commit* das suas alterações, envie (*push*) a branch para o repositório remoto e abra um *Pull Request* apontando para a branch `master`.

---

## Especificação dos Exercícios

### Exercício 1: Tipos Primitivos, Variáveis e Operadores
* **Objetivo:** Implementar o método `calcularMedia`.
* **Requisitos:** O método recebe as notas de três avaliações (Prova, Projeto e Lista). Deve-se calcular a média aritmética simples e retornar este valor numérico. Recomenda-se a utilização de `var` para a declaração das variáveis locais que armazenam os cálculos intermediários.

### Exercício 2: Controle de Fluxo Clássico (if / else)
* **Objetivo:** Implementar o método `verificarStatus`.
* **Requisitos:** O método deve analisar a média final e o total de faltas do aluno para retornar uma `String` indicando seu status, seguindo estritamente a ordem de prioridade abaixo:
    1. Se as faltas ultrapassarem o limite de **20**, retornar `"REPROVADO_POR_FALTA"`.
    2. Caso contrário, se a média for maior ou igual a **6.0**, retornar `"APROVADO"`.
    3. Caso a média seja inferior a 6.0 (sem estourar as faltas), retornar `"EXAME"`.

### Exercício 3: Controle com Switch Expressions
* **Objetivo:** Implementar o método `gerarOrientacao`.
* **Requisitos:** A partir da `String` de status gerada no Exercício 2, o método deve retornar uma mensagem de orientação final. É **obrigatório** o uso da estrutura *Switch Expression* (com o operador `->`) para realizar o mapeamento abaixo:
    * `"APROVADO"` -> *"Parabéns! Você dominou Classes e Objetos. Boas férias!"*
    * `"EXAME"` -> *"Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva."*
    * `"REPROVADO_POR_FALTA"` -> *"Reprovação automática. Frequência abaixo do mínimo exigido."*
    * `default` -> *"Procure a coordenação do curso."*

---

## Critérios de Entrega
A submissão da atividade será considerada efetivada apenas através da abertura do *Pull Request* no GitHub, contendo a solução desenvolvida na respectiva branch.
O link do pull request deve ser enviado no fórum da disciplina no Studeo.