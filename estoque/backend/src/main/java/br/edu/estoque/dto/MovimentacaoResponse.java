package br.edu.estoque.dto;

import br.edu.estoque.domain.Movimentacao;
import java.time.LocalDateTime;

public record MovimentacaoResponse(Long id, String produto, String tipo, int quantidade, LocalDateTime dataHora) {
    public static MovimentacaoResponse de(Movimentacao m) {
        return new MovimentacaoResponse(m.getId(), m.getProduto().getNome(), m.getTipo().name(), m.getQuantidade(), m.getDataHora());
    }
}
