package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

/**
 * Representação JSON de uma matrícula.
 * Existe porque o domínio tem referências circulares
 * (Matricula -> Aluno -> matriculas -> Matricula): serializar o objeto
 * do domínio direto geraria recursão infinita.
 */
public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String turma,
        String periodoLetivo,
        String disciplina,
        String nomeDisciplina,
        SituacaoMatricula situacao,
        ResultadoAcademico resultadoAcademico
) {
    public static MatriculaResponse de(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getTurma().getCodigo(),
                matricula.getTurma().getPeriodoLetivo().toString(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getNome(),
                matricula.getSituacao(),
                matricula.getResultadoAcademico()
        );
    }
}
