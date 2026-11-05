package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Aluno;

public record AlunoResponse(String ra, String nome, String email) {

    public static AlunoResponse de(Aluno aluno) {
        return new AlunoResponse(aluno.getRa(), aluno.getNome(), aluno.getEmail());
    }
}
