package br.edu.sistemaacademico.domain;

public enum Semestre {
    PRIMEIRO,
    SEGUNDO;

    public int getNumero() {
        return ordinal() + 1;
    }
}