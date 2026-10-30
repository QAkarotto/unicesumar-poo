package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;

/** Representação JSON de uma disciplina. */
public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {

    public static DisciplinaResponse de(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
    }
}
