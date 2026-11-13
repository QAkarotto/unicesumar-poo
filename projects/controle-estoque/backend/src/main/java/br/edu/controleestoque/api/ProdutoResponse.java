package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String sku,
        String nome,
        String descricao,
        BigDecimal precoUnitario,
        int quantidadeEmEstoque,
        int estoqueMinimo,
        boolean ativo,
        boolean precisaReposicao,
        BigDecimal valorEmEstoque,
        Long categoriaId,
        String categoria
) {

    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(p.getId(), p.getSku(), p.getNome(), p.getDescricao(),
                p.getPrecoUnitario(), p.getQuantidadeEmEstoque(), p.getEstoqueMinimo(), p.isAtivo(),
                p.precisaReposicao(), p.valorEmEstoque(),
                p.getCategoria().getId(), p.getCategoria().getNome());
    }
}
