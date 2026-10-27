package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;

import java.util.List;

/** Representação JSON de um aluno, com o histórico de matrículas. */
public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email,
        List<MatriculaResponse> matriculas
) {
    public static AlunoResponse de(Aluno aluno) {
        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatriculas().stream().map(MatriculaResponse::de).toList()
        );
    }
}
