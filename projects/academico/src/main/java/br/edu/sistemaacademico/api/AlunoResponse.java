package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email
) {

    public static AlunoResponse de(Aluno aluno) {
        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }
}
