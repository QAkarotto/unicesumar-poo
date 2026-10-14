package br.edu.sistemaacademico.domain;

public enum Semestre {
    PRIMEIRO(1),
    SEGUNDO(2);

    private final int numero;

    Semestre(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }
}
