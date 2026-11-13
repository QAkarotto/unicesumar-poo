package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.TipoMovimentacao;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * ENTRADA: quantidade recebida + fornecedorId obrigatório.
 * SAIDA:   quantidade retirada.
 * AJUSTE:  novoSaldo contado no inventário + observação (justificativa).
 *
 * Cada campo tem um único significado: "quantidade" é sempre quanto entra ou sai,
 * e "novoSaldo" é usado somente no AJUSTE.
 */
public record MovimentacaoRequest(
        @NotNull(message = "O produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O tipo é obrigatório (ENTRADA, SAIDA ou AJUSTE).")
        TipoMovimentacao tipo,

        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantidade,

        @PositiveOrZero(message = "O novo saldo não pode ser negativo.")
        Integer novoSaldo,

        Long fornecedorId,

        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
        String observacao
) {

    @AssertTrue(message = "Informe a quantidade na ENTRADA e na SAIDA. No AJUSTE, use o campo novoSaldo.")
    public boolean isQuantidadeCompativelComOTipo() {
        if (tipo == null) return true; // já reportado por @NotNull
        return tipo == TipoMovimentacao.AJUSTE ? quantidade == null : quantidade != null;
    }

    @AssertTrue(message = "Informe o novoSaldo somente no AJUSTE.")
    public boolean isNovoSaldoCompativelComOTipo() {
        if (tipo == null) return true;
        return tipo == TipoMovimentacao.AJUSTE ? novoSaldo != null : novoSaldo == null;
    }
}
