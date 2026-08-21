package br.edu.sistemaacademico.domain;

// Situação da matrícula. Não vira atributo: a matrícula descobre a situação
// pelo resultado, senão seriam dois estados para manter iguais.
public enum SituacaoMatricula {

    EM_CURSO("Em curso"),
    CONCLUIDA("Concluída");

    private final String descricao;

    SituacaoMatricula(String descricao) {
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
