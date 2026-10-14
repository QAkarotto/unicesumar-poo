
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String codigoDisciplina,
        String codigoTurma,
        String situacao,
        String resultado
) {
    public static MatriculaResponse de(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getTurma().getCodigo(),
                matricula.getSituacao().name(),
                matricula.getResultado() == null
                        ? null
                        : matricula.getResultado().name()
        );
    }
}
