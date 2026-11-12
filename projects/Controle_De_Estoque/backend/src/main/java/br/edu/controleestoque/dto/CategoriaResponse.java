package br.edu.controleestoque.dto;

import br.edu.controleestoque.domain.Categoria;

public record CategoriaResponse(Long id, String nome) {

    public static CategoriaResponse de(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
