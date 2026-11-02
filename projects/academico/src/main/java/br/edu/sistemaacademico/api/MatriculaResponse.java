package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String codigoTurma,
        String codigoDisciplina,
        String situacao) {

    public static MatriculaResponse from(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getTurma().getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getSituacao().name());
    }
}