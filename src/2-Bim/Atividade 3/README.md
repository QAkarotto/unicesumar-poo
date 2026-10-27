# Atividade 2 - 2º bimestre

## Collection do Postman

Criei um arquivo JSON da collection para quem clonar o repositório importar no Postman em poucos cliques, sem montar as requisições manualmente.

**Arquivo:** [postman/Sistema-Academico.postman_collection.json](postman/Sistema-Academico.postman_collection.json)

No Postman: **Import** → **File** → selecione esse JSON. Com a API rodando em `http://localhost:8080`, execute as requests na ordem da collection.

## Executar no IntelliJ

Se você abrir o repositório inteiro, o IntelliJ pode não tratar esta pasta como projeto Java. Sem isso, `SistemaAcademicoApplication` fica **not runnable**.

Adicione o Maven desta atividade:

1. No painel **Project**, clique com o botão direito em [pom.xml](pom.xml)
2. Escolha **Add as Maven Project** (ou **Link Maven Project** / recarregar o Maven, conforme a versão do IntelliJ).
3. Use **JDK 17 ou superior** em **File → Project Structure → Project → SDK**.
4. Rode a classe `SistemaAcademicoApplication` (Run ▶ ao lado do `main`).

