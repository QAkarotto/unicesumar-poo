package br.edu.agroassist.domain;

public enum Status {
    ABERTA("Aberta"),
    ATRIBUIDA("Atribuída"),
    EM_ATENDIMENTO("Em atendimento"),
    CONCLUIDA("Concluída"),
    AVALIADA("Avaliada");
    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
