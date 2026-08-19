package br.edu.sistemaacademico;


public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano do período letivo deve ser positivo.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre do período letivo é obrigatório.");
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
        return String.format("%d/%d", ano, semestre.getNumero());
    }
}