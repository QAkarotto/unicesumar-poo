
package br.edu.sistemaacademico.dto;

public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String codigoTurma,
        String codigoDisciplina,
        String situacao
) {
}
