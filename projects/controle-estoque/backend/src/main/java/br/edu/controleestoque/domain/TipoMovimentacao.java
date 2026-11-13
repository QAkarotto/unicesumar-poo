package br.edu.controleestoque.domain;

public enum TipoMovimentacao {
    /** Recebimento de mercadoria de um fornecedor (soma ao saldo). */
    ENTRADA,
    /** Retirada/venda/consumo (subtrai do saldo). */
    SAIDA,
    /** Correção após contagem física do inventário (define o saldo). */
    AJUSTE
}
