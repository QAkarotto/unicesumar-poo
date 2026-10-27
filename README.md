# unicesumar-poo
Repositório utilizado na disciplina de Programação Orientada a Objetos dos cursos de Engenharia de Software e Análise e Desenvolvimento de Sistemas da Unicesumar Ponta Grossa

## Manifesto Oficial

### Lista de alunos que concordam que o Professor Goku é o melhor professor do universo (adicione seu nome e turma (ESOFT4S-NA, ESOFT4S-NB, ADSIS4S) em ordem alfabética:):

1. Aluno Exemplo (ESOFT3S-NA)
2. Gabriel Afonso Santana Messias ESOFT4S-NA

## API do sistema acadêmico

O projeto em `projects/academico` é uma aplicação Spring Boot 3 com persistência em SQLite via JDBC.

Para executar, a partir da pasta `projects/academico`, use:

```bash
mvn spring-boot:run
```

Endpoints exigidos pela Atividade 03:

- `POST /api/alunos`
- `GET /api/alunos/{ra}`
- `POST /api/disciplinas`
- `GET /api/disciplinas/{codigo}`

Exemplo de cadastro de aluno:

```json
{
  "ra": "RA24294346-2",
  "nome": "Vegeta",
  "email": "vegeta@unicesumar.edu.br"
}
```

Exemplo de cadastro de disciplina:

```json
{
  "codigo": "POO-01",
  "nome": "Programação Orientada a Objetos",
  "cargaHoraria": 80
}
```

O banco é criado em `data/academico.db` e os registros permanecem após reiniciar a aplicação.

## Fluxo simples

A requisição HTTP chega ao Controller, que chama o Repository. O Repository executa SQL parametrizado com JDBC, lê ou grava no SQLite e converte os dados em objetos Java para a resposta.

## Perguntas da atividade

1. Os comandos SQL ficam nas classes `JdbcAlunoRepository` e `JdbcDisciplinaRepository`.
2. O Controller não deve executar SQL porque sua responsabilidade é HTTP; separar o acesso a dados deixa o código mais organizado e fácil de trocar/testar.
3. A interface Repository define as operações de acesso aos dados sem prender o restante da aplicação à implementação JDBC.
4. Para trocar SQLite por outra tecnologia, basta criar novas implementações dos contratos Repository, mantendo Controller e domínio praticamente iguais.
