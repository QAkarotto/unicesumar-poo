package br.edu.sistemaacademico.domain;

public enum ResultadoMatricula {
    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private final String descricao;

    ResultadoMatricula(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
