package br.edu.sistemaacademico.api;

/** Corpo do POST /api/disciplinas. */
public record DisciplinaRequest(String codigo, String nome, Integer cargaHoraria) {
}
