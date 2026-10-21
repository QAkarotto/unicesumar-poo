package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;

public record DisciplinaResposta(String codigo, String nome, int cargaHoraria) {

    public static DisciplinaResposta de(Disciplina disciplina) {
        return new DisciplinaResposta(
                disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
    }
}
