package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

public record MatriculaResponse(
        Long id,
        String codigo,
        AlunoResponse aluno,
        String codigoDisciplina,
        String nomeDisciplina,
        String codigoTurma,
        int ano,
        Semestre semestre,
        SituacaoMatricula situacao,
        ResultadoAcademico resultado
) {
    public static MatriculaResponse de(Matricula matricula) {
        var turma = matricula.getTurma();
        var oferta = matricula.getOfertaDisciplina();
        var disciplina = oferta.getDisciplina();

        return new MatriculaResponse(
                matricula.getId(),
                matricula.getCodigo(),
                AlunoResponse.de(matricula.getAluno()),
                disciplina.getCodigo(),
                disciplina.getNome(),
                turma.getCodigo(),
                turma.getPeriodoLetivo().getAno(),
                turma.getPeriodoLetivo().getSemestre(),
                matricula.getSituacao(),
                matricula.getResultado()
        );
    }
}
