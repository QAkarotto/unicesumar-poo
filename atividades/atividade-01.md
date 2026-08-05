# Adicione seu nome na lista de alunos do Manifesto Oficial do `README` do repositório

## Objetivo

Adicionar seu nome à lista de alunos no **Manifesto Oficial** do arquivo `README.md` e enviar um **Pull Request (PR)** com a alteração.

---

## Passo 1 – Confirme seu acesso ao repositório

Verifique se seu usuário do GitHub foi adicionado como colaborador do repositório da disciplina:

> https://github.com/QAkarotto/unicesumar-poo

> **Caso ainda não tenha acesso**, informe seu usuário do GitHub no fórum do Studeo.

---
## aa
## Passo 2 – Clone o repositório

Acesse o repositório da disciplina:

> https://github.com/QAkarotto/unicesumar-poo

Certifique-se de que o **Git Bash** está instalado e execute o comando abaixo na pasta onde deseja salvar o projeto:

```bash
git clone https://github.com/QAkarotto/unicesumar-poo.git
```

---

## Passo 3 – Crie uma branch

Entre na pasta do projeto e crie uma nova branch seguindo o padrão:

```text
feature/sua-turma/seu-nome
```

Comando:

```bash
git checkout -b feature/sua-turma/seu-nome
```

---

## Passo 4 – Edite o `README.md`

Abra o projeto em um editor de código (como o VS Code) e:

- Localize o arquivo `README.md`;
- Adicione seu nome na lista de alunos da seção **Manifesto Oficial**;
- Salve as alterações.

---

## Passo 5 – Faça o commit

Você pode fazer o commit pelo **VS Code** ou pelo **terminal**.

### Pelo VS Code

1. Abra a aba **Source Control**;
2. Selecione os arquivos alterados;
3. Digite a mensagem de commit;
4. Clique em **Commit**.

### Pelo terminal

Execute:

```bash
git add .
git commit -m "docs: adiciona nome SEU_NOME na lista de alunos"
```

### Se for seu primeiro uso do Git

Caso o Git solicite a configuração do usuário e e-mail, execute:

```bash
git config --global user.name "Seu nome"
git config --global user.email "seu.email@example.com"
```

---

## Passo 6 – Publique sua branch

Envie sua branch para o GitHub.

### Pelo VS Code

Clique em **Publish Branch** na aba **Source Control**.

### Pelo terminal

```bash
git push
```

> Se o GitHub ainda não estiver configurado no computador, será necessário realizar a autenticação pelo navegador.

---

## Passo 7 – Crie um Pull Request

Após publicar a branch:

1. Acesse sua branch no GitHub;
2. Clique em **Compare & pull request** (ou **New Pull Request**);
3. Crie um Pull Request para a branch **`master`**.

---

## Passo 8 – Envie o link

Por fim, envie **no fórum no Studeo** o **link do Pull Request** criado.