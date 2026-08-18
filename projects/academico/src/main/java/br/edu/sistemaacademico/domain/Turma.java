package br.edu.sistemaacademico.domain;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma não pode ser nulo ou vazio.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina da turma não pode ser nula.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo não pode ser nulo.");
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    @Override
    public String toString() {
        return codigo + " de " + disciplina + " no período " + periodoLetivo;
    }
}