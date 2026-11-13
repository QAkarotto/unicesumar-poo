package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.Produto;

import java.util.Comparator;
import java.util.List;

/** Produto + fornecedores vinculados (GET /api/produtos/{id}). */
public record ProdutoDetalheResponse(ProdutoResponse produto, List<FornecedorResponse> fornecedores) {

    public static ProdutoDetalheResponse de(Produto p) {
        var fornecedores = p.getFornecedores().stream()
                .map(FornecedorResponse::de)
                .sorted(Comparator.comparing(FornecedorResponse::nome))
                .toList();
        return new ProdutoDetalheResponse(ProdutoResponse.de(p), fornecedores);
    }
}
