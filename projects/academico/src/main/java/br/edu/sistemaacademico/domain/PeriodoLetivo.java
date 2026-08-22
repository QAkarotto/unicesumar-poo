package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private int ano;
    private Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {

        if (ano <= 0) {
            throw new IllegalArgumentException("Ano inválido.");
        }

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre é obrigatório.");
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return ano + " - " + semestre;
    }
}