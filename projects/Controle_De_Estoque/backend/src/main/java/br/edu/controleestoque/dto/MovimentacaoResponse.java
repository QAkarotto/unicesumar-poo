package br.edu.controleestoque.dto;

import br.edu.controleestoque.domain.Movimentacao;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Long produtoId,
        String produto,
        String tipo,
        int quantidade,
        String motivo,
        LocalDateTime dataHora
) {

    public static MovimentacaoResponse de(Movimentacao movimentacao) {
        var produto = movimentacao.getProduto();
        return new MovimentacaoResponse(
                movimentacao.getId(),
                produto.getId(),
                produto.getCodigo() + " - " + produto.getNome(),
                movimentacao.getTipo().name(),
                movimentacao.getQuantidade(),
                movimentacao.getMotivo(),
                movimentacao.getDataHora());
    }
}
