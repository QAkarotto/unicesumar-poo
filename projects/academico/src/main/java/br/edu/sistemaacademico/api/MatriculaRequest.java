package br.edu.sistemaacademico.api;

public record MatriculaRequest(
        String registroAcademico,
        String turma,
        String disciplina
) {}