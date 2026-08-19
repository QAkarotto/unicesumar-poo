package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {

        if (ano <= 0) {
            throw new IllegalArgumentException(
                    "O ano deve ser positivo."
            );
        }

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre é obrigatório."
            );
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }
//a
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