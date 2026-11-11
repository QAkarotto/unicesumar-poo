package br.edu.sistemaacademico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class PeriodoLetivo {

    @Column(name = "ano", nullable = false)
    private int ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", nullable = false, length = 20)
    private Semestre semestre;

    protected PeriodoLetivo() {
    }

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
        return ano + "/" + (semestre != null ? semestre.getNumero() : "");
    }
}
