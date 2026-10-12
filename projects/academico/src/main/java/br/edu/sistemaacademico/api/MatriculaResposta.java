package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

public record MatriculaResposta(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String disciplina,
        String turma,
        String periodoLetivo,
        SituacaoMatricula situacao,
        ResultadoAcademico resultado
) {

    public static MatriculaResposta de(Matricula matricula) {
        return new MatriculaResposta(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getOfertaDisciplina().getDisciplina().getNome(),
                matricula.getTurma().getCodigo(),
                matricula.getTurma().getPeriodoLetivo().toString(),
                matricula.getSituacao(),
                matricula.getResultado()
        );
    }
}