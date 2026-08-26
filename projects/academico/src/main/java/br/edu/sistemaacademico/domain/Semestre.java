package br.edu.sistemaacademico.domain;

public enum Semestre {
    PRIMEIRO("1"),
    SEGUNDO("2");

    private final String descricao;

    Semestre(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}