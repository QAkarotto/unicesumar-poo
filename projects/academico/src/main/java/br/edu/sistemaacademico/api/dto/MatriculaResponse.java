
package br.edu.sistemaacademico.api.dto;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResponse(
        String codigo,
        AlunoResponse aluno,
        String codigoTurma,
        String codigoDisciplina,
        String situacao,
        String resultado
) {
    public static MatriculaResponse de(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                AlunoResponse.de(matricula.getAluno()),
                matricula.getTurma().getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getSituacao().name(),
                matricula.getResultado() == null
                        ? null
                        : matricula.getResultado().name()
        );
    }
}
