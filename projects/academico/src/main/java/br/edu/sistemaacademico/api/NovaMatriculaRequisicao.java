package br.edu.sistemaacademico.api;

/** Corpo JSON esperado no POST /api/matriculas. */
public record NovaMatriculaRequisicao(
        String registroAcademico,
        String turma,
        String disciplina
) {
}
