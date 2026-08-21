package br.edu.sistemaacademico.domain;

// Resultados possíveis quando uma matrícula é concluída.
public enum ResultadoAcademico {

    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private final String descricao;

    ResultadoAcademico(String descricao) {
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
