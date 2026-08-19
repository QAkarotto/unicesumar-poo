package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano deve ser positivo");
        }

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre não pode ser nulo");
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
        return "PeriodoLetivo{" +
                "ano=" + ano +
                ", semestre=" + semestre +
                '}';
    }
}

