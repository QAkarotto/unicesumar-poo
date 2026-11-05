package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Matricula;

public record MatriculaResponse(Long id, String raAluno, String nomeAluno, Long idOferta, String turma, String disciplina, String resultado) {

    public static MatriculaResponse de(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getAluno().getRa(),
                matricula.getAluno().getNome(),
                matricula.getOferta().getId(),
                matricula.getOferta().getTurma().getCodigo(),
                matricula.getOferta().getDisciplina().getNome(),
                matricula.getResultado().name()
        );
    }
}
