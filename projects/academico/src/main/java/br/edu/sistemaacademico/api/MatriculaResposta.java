package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;

/**
 * Formato JSON de uma matrícula. Evita devolver a entidade direto, pois
 * Matricula -> Aluno -> matrículas -> Aluno... geraria um ciclo infinito no JSON.
 */
public record MatriculaResposta(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String turma,
        String periodoLetivo,
        String disciplina,
        String situacao,
        String resultado
) {
    public static MatriculaResposta de(Matricula matricula) {
        var oferta = matricula.getOfertaDisciplina();
        var resultado = matricula.getResultado();
        return new MatriculaResposta(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                oferta.getTurma().getCodigo(),
                oferta.getTurma().getPeriodoLetivo().toString(),
                oferta.getDisciplina().getCodigo(),
                matricula.getSituacao().name(),
                resultado == null ? null : resultado.name()
        );
    }
}
