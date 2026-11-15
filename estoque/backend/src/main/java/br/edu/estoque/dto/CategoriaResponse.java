package br.edu.estoque.dto;

import br.edu.estoque.domain.Categoria;

public record CategoriaResponse(Long id, String nome) {
    public static CategoriaResponse de(Categoria c) { return new CategoriaResponse(c.getId(), c.getNome()); }
}
