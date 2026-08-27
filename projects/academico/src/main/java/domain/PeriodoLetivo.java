package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException(
                    "O ano deve ser maior que zero."
            );
        }
        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre não pode ser nulo."
            );
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodoLetivo that)) return false;
        return ano == that.ano && semestre == that.semestre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ano, semestre);
    }

    @Override
    public String toString() {
        String numeroSemestre = semestre == Semestre.PRIMEIRO ? "1" : "2";
        return ano + "/" + numeroSemestre;
    }
}
