package br.edu.controleestoque.api;

import java.math.BigDecimal;

/** Indicadores exibidos no painel inicial do frontend. */
public record ResumoEstoqueResponse(
        long totalProdutos,
        long produtosAtivos,
        long produtosParaReposicao,
        long unidadesEmEstoque,
        BigDecimal valorTotalEmEstoque
) {
}
