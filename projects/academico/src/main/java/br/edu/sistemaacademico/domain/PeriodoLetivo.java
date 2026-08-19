package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano deve ser um valor válido.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre é obrigatório.");
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
        return ano + "/" + semestre;
    }
}