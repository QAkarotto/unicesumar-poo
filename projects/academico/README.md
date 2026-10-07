# Sistema Acadêmico

Projeto didático em Java para a criação de uma pequena parte de um sistema acadêmico.

## Contexto

Este projeto é o ponto de partida da Atividade 04. Apenas `SistemaAcademico` é fornecida; as classes de domínio devem ser criadas pelos alunos com foco em encapsulamento e proteção de estado.

## Modelo inicial

- `Aluno`: representa um aluno com identificador acadêmico, nome e e-mail.
- `Disciplina`: representa um componente curricular e sua carga horária.
- `Turma`: representa a oferta de uma disciplina em um período letivo.
- `Matricula`: representa a relação entre um aluno e uma turma.
- `PeriodoLetivo`: representa um ano e um semestre letivo.
- `Semestre`: `enum` que representa os semestres aceitos pelo sistema.

Esses tipos ainda não estão implementados. Consulte o enunciado em `atividades/atividade-04.md`.

## Executando

O projeto requer o JDK 25. Depois de criar todas as classes solicitadas, compile no diretório `projects/academico` com:

```bash
mvn compile
```

Para executar pela IDE, abra a classe `SistemaAcademico` e utilize a opção de executar o método `main`.

# API REST - Sistema Acadêmico

Collection do Postman:
https://www.postman.com/pbittencourt219-382892/workspace/atividades-2bim-esoft-nb-pierre-bittencourt/collection/58739882-8c77e2fe-c37a-4c89-8a67-b0715d0144e2?action=share&source=copy-link&creator=58739882



## Endpoints

- GET /api/matriculas/{id}
- GET /api/alunos/{ra}
- POST /api/matriculas
