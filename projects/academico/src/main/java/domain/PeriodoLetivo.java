package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre não pode ser nulo.");
        }
        if (ano < 2000 || ano > 2100) {
            throw new IllegalArgumentException("Ano inválido.");
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    @Override
    public String toString() {
        return "Período: " + ano + " - " + semestre;
    }
}