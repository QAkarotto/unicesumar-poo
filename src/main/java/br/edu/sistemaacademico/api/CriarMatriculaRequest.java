package br.edu.sistemaacademico.api;

public record CriarMatriculaRequest(
        String registroAcademico,
        String codigoTurma,
        String codigoDisciplina
) {
}
