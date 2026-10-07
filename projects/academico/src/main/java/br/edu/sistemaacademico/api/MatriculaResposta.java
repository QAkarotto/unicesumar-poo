package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResposta(
        String id,
        String ra,
        String aluno,
        String disciplina,
        String turma,
        String resultado
) {

    public static MatriculaResposta de(String id, Matricula matricula) {
        return new MatriculaResposta(
                id,
                matricula.getAluno().getRa(),
                matricula.getAluno().getNome(),
                matricula.getOferta().getDisciplina().getNome(),
                matricula.getOferta().getTurma().getCodigo(),
                matricula.getResultado() == null ? "cursando" : matricula.getResultado().name()
        );
    }
}