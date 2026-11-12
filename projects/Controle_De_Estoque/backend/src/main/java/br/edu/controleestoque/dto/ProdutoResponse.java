package br.edu.controleestoque.dto;

import br.edu.controleestoque.domain.Produto;

public record ProdutoResponse(
        Long id,
        String codigo,
        String nome,
        Long categoriaId,
        String categoria,
        int quantidade,
        int estoqueMinimo,
        boolean ativo,
        boolean estoqueBaixo
) {

    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.getQuantidade(),
                produto.getEstoqueMinimo(),
                produto.isAtivo(),
                produto.estoqueBaixo());
    }
}
