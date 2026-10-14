package br.edu.sistemaacademico.api;

/**
 * Corpo JSON do POST /api/matriculas.
 * O "codigo" é opcional: se não for informado, a API gera um.
 */
public record MatriculaRequest(
        String codigo,
        String registroAcademico,
        String turma,
        String disciplina
) {
}
