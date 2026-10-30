package br.edu.sistemaacademico.domain;

public enum Semestre {
    PRIMEIRO("1º semestre"),
    SEGUNDO("2º semestre");

    private final String descricao;

    Semestre(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
