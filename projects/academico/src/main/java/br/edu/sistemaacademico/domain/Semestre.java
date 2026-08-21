package br.edu.sistemaacademico.domain;

// Únicos semestres aceitos pelo sistema.
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

    @Override
    public String toString() {
        return descricao;
    }
}
