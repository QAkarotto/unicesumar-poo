
package br.edu.sistemaacademico.dto;

public record MatriculaRequest(
        String identificadorAluno,
        String codigoTurma,
        String codigoDisciplina
) {
}
