package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.Categoria;

public record CategoriaResponse(Long id, String nome, String descricao) {

    public static CategoriaResponse de(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome(), c.getDescricao());
    }
}
