package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResponse(
        String codigo,
        String registroAcademicoAluno,
        String nomeAluno,
        String codigoDisciplina,
        String nomeDisciplina,
        String codigoTurma,
        String periodoLetivo,
        String situacao,
        String resultado
) {

    public static MatriculaResponse from(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getDisciplina().getCodigo(),
                matricula.getDisciplina().getNome(),
                matricula.getTurma().getCodigo(),
                matricula.getPeriodoLetivo().toString(),
                matricula.getSituacao().name(),
                matricula.getResultado() == null ? null : matricula.getResultado().name()
        );
    }
}
