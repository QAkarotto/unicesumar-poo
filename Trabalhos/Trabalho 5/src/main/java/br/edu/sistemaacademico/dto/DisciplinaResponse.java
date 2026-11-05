package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Disciplina;

public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {

    public static DisciplinaResponse de(Disciplina disciplina) {
        return new DisciplinaResponse(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
    }
}
