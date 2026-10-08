package br.edu.sistemaacademico.api;

public record NovaMatriculaRequest(
        String codigo,
        String identificadorAluno,
        String codigoTurma,
        String codigoDisciplina) {
}
