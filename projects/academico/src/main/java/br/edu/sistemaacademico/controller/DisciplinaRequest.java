package br.edu.sistemaacademico.controller;

public record DisciplinaRequest(
        String codigo,
        String nome,
        Integer cargaHoraria
) {
}
