package br.edu.sistemaacademico.dto;

public record CriarDisciplinaRequest(
        String codigo,
        String nome,
        Integer cargaHoraria,
        Integer carga_horaria
) {
    public Integer obterCargaHoraria() {
        return cargaHoraria != null ? cargaHoraria : carga_horaria;
    }
}
