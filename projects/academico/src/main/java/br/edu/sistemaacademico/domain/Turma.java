package br.edu.sistemaacademico.domain;

public class Turma {

    private String codigo;
    private Disciplina disciplina;
    private PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório.");
        }

        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo é obrigatório.");
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    @Override
    public String toString() {
        return codigo + " - " + disciplina + " - " + periodoLetivo;
    }
}