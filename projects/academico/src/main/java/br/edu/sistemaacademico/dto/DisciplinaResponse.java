package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Disciplina;

public record DisciplinaResponse(
        Long id,
        String codigo,
        String nome,
        int cargaHoraria
) {
    public static DisciplinaResponse de(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getId(),
                disciplina.getCodigo(),
                disciplina.getNome(),
                disciplina.getCargaHoraria()
        );
    }
}
