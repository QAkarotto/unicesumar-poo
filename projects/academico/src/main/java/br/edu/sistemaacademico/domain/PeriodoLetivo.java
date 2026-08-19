package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano letivo deve ser maior que zero.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre não pode ser nulo.");
        }
        this.ano = ano;
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return ano + "/" + semestre;
    }
}