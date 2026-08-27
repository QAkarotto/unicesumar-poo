package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private int ano;
    private Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser maior que zero.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre é obrigatório.");
        }
        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return this.ano;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    @Override
    public String toString() {
        return this.ano + "/" + this.semestre;
    }
}