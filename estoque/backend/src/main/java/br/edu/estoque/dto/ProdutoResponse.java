package br.edu.estoque.dto;

import br.edu.estoque.domain.Produto;
import java.math.BigDecimal;

public record ProdutoResponse(Long id, String sku, String nome, BigDecimal preco, int quantidade,
                              int estoqueMinimo, boolean ativo, boolean abaixoDoMinimo, String categoria) {
    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(p.getId(), p.getSku(), p.getNome(), p.getPreco(), p.getQuantidade(),
                p.getEstoqueMinimo(), p.isAtivo(), p.abaixoDoMinimo(), p.getCategoria().getNome());
    }
}
