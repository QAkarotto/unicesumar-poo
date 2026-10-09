package br.edu.sistemaacademico.api;

public record NovaMatriculaRequest(
        String codigo,
        String registroAcademico,
        String turma,
        String disciplina
) {
}
