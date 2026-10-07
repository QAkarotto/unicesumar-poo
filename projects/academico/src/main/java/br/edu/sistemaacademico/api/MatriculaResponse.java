package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String disciplina,
        String turma,
        String periodo,
        SituacaoMatricula situacao,
        ResultadoAcademico resultado
) {
    public static MatriculaResponse de(Matricula m) {
        return new MatriculaResponse(
                m.getCodigo(),
                m.getAluno().getRegistroAcademico(),
                m.getAluno().getNome(),
                m.getOfertaDisciplina().getDisciplina().getCodigo(),
                m.getTurma().getCodigo(),
                m.getTurma().getPeriodoLetivo().toString(),
                m.getSituacao(),
                m.getResultado()
        );
    }
}