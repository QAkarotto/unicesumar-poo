
package br.edu.sistemaacademico.dto;

public record DisciplinaRequest(
        String codigo,
        String nome,
        int cargaHoraria
) {
}
