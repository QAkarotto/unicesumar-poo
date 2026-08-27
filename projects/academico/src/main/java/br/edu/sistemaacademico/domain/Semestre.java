package br.edu.sistemaacademico.domain;

public enum Semestre {
    PRIMEIRO("1º Semestre"),
    SEGUNDO("2º Semestre");
    private String descricao;

    Semestre(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
