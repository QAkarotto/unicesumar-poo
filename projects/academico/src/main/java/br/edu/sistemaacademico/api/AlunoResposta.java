package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;

public record AlunoResposta(
        String registroAcademico,
        String nome,
        String email,
        List<MatriculaResposta> matriculas
) {
    public static AlunoResposta de(Aluno aluno) {
        return new AlunoResposta(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatriculas().stream().map(MatriculaResposta::de).toList()
        );
    }
}
