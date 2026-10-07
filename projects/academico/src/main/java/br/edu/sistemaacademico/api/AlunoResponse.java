package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;

import java.util.ArrayList;
import java.util.List;


//Dados do aluno que serão devolvidos em JSON

public record AlunoResponse(
        String registroAcademico,
        String nome,
        String email,
        List<MatriculaResponse> matriculas
) {

    public static AlunoResponse de(Aluno aluno) {
        List<MatriculaResponse> matriculas = new ArrayList<>();
        for (Matricula matricula : aluno.getMatriculas()) {
            matriculas.add(MatriculaResponse.de(matricula));
        }

        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                matriculas
        );
    }
}
