package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;

public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email,
        List<String> matriculas
) {

    public static AlunoResponse from(Aluno aluno) {
        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatriculas().stream()
                        .map(matricula -> matricula.getCodigo())
                        .toList()
        );
    }
}
