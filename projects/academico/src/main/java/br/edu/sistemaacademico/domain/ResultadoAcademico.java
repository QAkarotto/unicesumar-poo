package br.edu.sistemaacademico.domain;

public enum ResultadoAcademico {
    APROVADO,
    REPROVADO,
    REPROVADO_POR_FALTA;

    public boolean isAprovado() {
        return this == APROVADO;
    }
}