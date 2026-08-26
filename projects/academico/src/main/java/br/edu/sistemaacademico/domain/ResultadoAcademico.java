package br.edu.sistemaacademico.domain;

/*
 * Resultado final de uma matrícula já concluída.
 * Enquanto a matrícula está em andamento, ela simplesmente não possui resultado.
 */
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

    public boolean isAprovado() {
        return this == APROVADO;
    }

    @Override
    public String toString() {
        return descricao;
    }
}