package br.edu.sistemaacademico.api;

/** Corpo JSON esperado no POST /api/matriculas. */
public record NovaMatriculaRequest(
        String codigo,
        String registroAcademico,
        String turma,
        String disciplina
) {
}
