package br.edu.sistemaacademico.domain;

public enum ResultadoAcademico {

    APROVADO,
    REPROVADO;

    public boolean isAprovado() {
        return this == APROVADO;
    }
}
