package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Aluno;

public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email,
        int totalMatriculas
) {
    public static AlunoResponse de(Aluno aluno) {
        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatriculas() != null ? aluno.getMatriculas().size() : 0
        );
    }
}
