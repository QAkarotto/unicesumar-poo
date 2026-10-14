package br.edu.sistemaacademico.api.dto;

public record CriarMatriculaRequest(
        String codigo,
        String registroAcademico,
        String codigoTurma,
        String codigoDisciplina
) {
}
