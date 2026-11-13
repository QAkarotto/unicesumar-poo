package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.Movimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        String tipo,
        int quantidade,
        int saldoAnterior,
        int saldoPosterior,
        LocalDateTime dataHora,
        String observacao,
        Long produtoId,
        String produtoSku,
        String produtoNome,
        Long fornecedorId,
        String fornecedorNome
) {

    public static MovimentacaoResponse de(Movimentacao m) {
        var f = m.getFornecedor();
        return new MovimentacaoResponse(m.getId(), m.getTipo().name(), m.getQuantidade(),
                m.getSaldoAnterior(), m.getSaldoPosterior(), m.getDataHora(), m.getObservacao(),
                m.getProduto().getId(), m.getProduto().getSku(), m.getProduto().getNome(),
                f == null ? null : f.getId(), f == null ? null : f.getNome());
    }
}
