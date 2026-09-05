package br.edu.sistemaacademico.domain;

import java.util.Objects;

public final class PeriodoLetivo {
    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre é obrigatório.");
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
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof PeriodoLetivo periodo)) {
            return false;
        }
        return ano == periodo.ano && semestre == periodo.semestre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ano, semestre);
    }

    @Override
    public String toString() {
        return ano + "/" + semestre.getNumero();
    }
}