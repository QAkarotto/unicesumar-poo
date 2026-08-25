package org.alexandreg;
import java.util.Objects;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano < 1900) {
            throw new IllegalArgumentException("O ano do período letivo é inválido: " + ano + ".");
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PeriodoLetivo)) {
            return false;
        }
        PeriodoLetivo outro = (PeriodoLetivo) obj;
        return ano == outro.ano && semestre == outro.semestre;
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
