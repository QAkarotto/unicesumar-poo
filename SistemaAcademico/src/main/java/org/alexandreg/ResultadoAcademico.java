package org.alexandreg;

public enum ResultadoAcademico {

    APROVADO,
    REPROVADO;

    public boolean isAprovado() {
        return this == APROVADO;
    }
}
