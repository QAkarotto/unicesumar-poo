
package br.edu.sistemaacademico.dto;

public record NovaMatriculaRequest(
        String codigo,
        String registroAcademico,
        String codigoTurma,
        String codigoDisciplina
) {}
