package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

/**
 * Formato JSON de uma matrícula.
 *
 * Por que existe: Matricula aponta para Aluno e Aluno aponta de volta para suas
 * Matriculas. Se o Spring tentasse converter as classes do domínio direto para JSON,
 * entraria em loop infinito. Este record escolhe só os campos que interessam.
 */
public record MatriculaResponse(
        String codigo,
        SituacaoMatricula situacao,
        ResultadoAcademico resultado,
        String registroAcademico,
        String aluno,
        String turma,
        String periodoLetivo,
        String disciplina,
        String nomeDisciplina
) {
    public static MatriculaResponse de(Matricula matricula) {
        var oferta = matricula.getOfertaDisciplina();
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getSituacao(),
                matricula.getResultado(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getTurma().getCodigo(),
                matricula.getTurma().getPeriodoLetivo().toString(),
                oferta.getDisciplina().getCodigo(),
                oferta.getDisciplina().getNome()
        );
    }
}
