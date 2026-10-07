package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;

public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email,
        List<MatriculaResponse> matriculas
) {
    public static AlunoResponse de(Aluno a) {
        return new AlunoResponse(
                a.getRegistroAcademico(),
                a.getNome(),
                a.getEmail(),
                a.getMatriculas().stream().map(MatriculaResponse::de).toList()
        );
    }
}