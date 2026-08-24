package br.edu.sistemaacademico.domain;


public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano do período letivo deve ser positivo.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre do período letivo é obrigatório.");
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
        return ano + "/" + (semestre == Semestre.PRIMEIRO ? "1" : "2");
    }
}
