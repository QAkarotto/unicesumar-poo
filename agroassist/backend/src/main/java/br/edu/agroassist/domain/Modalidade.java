package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;

public enum Modalidade {
    VISITA("Visita à propriedade"),
    REMOTA("Orientação remota");
    private final String descricao;

    Modalidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Modalidade deDescricao(String valor) {
        for (Modalidade modalidade : values()) {
            if (modalidade.descricao.equals(valor)) return modalidade;
        }
        throw new DadosInvalidosException("Selecione uma modalidade válida.");
    }
}
