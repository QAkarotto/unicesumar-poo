package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private int ano;
    private Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano inválido");
        }

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre é obrigatório");
        }

import java.util.Objects;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
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
        if (!(o instanceof PeriodoLetivo)) return false;
        PeriodoLetivo that = (PeriodoLetivo) o;
        return ano == that.ano && semestre == that.semestre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ano, semestre);
    }

    @Override
    public String toString() {
        return ano + "/" + (semestre == Semestre.PRIMEIRO ? "1" : "2");
    }
}
}
