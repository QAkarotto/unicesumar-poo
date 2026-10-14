package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;

/**
 * Representação JSON de uma matrícula.
 * O objeto de domínio não é serializado diretamente porque Aluno e Matricula
 * se referenciam mutuamente, o que causaria recursão infinita no JSON.
 */
public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String aluno,
        String disciplina,
        String turma,
        String periodoLetivo,
        String situacao,
        String resultado
) {
    public static MatriculaResponse de(Matricula matricula) {
        var resultado = matricula.getResultado();
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getTurma().getCodigo(),
                matricula.getTurma().getPeriodoLetivo().toString(),
                matricula.getSituacao().name(),
                resultado == null ? null : resultado.name()
        );
    }
}
