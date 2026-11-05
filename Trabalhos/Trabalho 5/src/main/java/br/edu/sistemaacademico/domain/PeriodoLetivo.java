package br.edu.sistemaacademico.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PeriodoLetivo {
    private int ano;

    @Enumerated(EnumType.STRING)
    private Semestre semestre;

    protected PeriodoLetivo() {
    }

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
}
