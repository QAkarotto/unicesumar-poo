package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

public record AlunoResposta(String registroAcademico, String nome, String email) {

    public static AlunoResposta de(Aluno aluno) {
        return new AlunoResposta(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail());
    }
}