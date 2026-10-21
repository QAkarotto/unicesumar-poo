package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

public record AlunoResposta(String ra, String nome, String email) {

    public static AlunoResposta de(Aluno aluno) {
        return new AlunoResposta(aluno.getRa(), aluno.getNome(), aluno.getEmail());
    }
}