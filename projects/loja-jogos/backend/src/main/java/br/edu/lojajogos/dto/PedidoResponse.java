package br.edu.lojajogos.dto;

import br.edu.lojajogos.domain.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A resposta "achata" o pedido: em vez do objeto Cliente inteiro, manda só o nome.
public record PedidoResponse(
        Long id,
        String cliente,
        String jogo,
        Integer quantidade,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataPedido) {

    public static PedidoResponse de(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getNome(),
                pedido.getJogo().getTitulo(),
                pedido.getQuantidade(),
                pedido.getValorTotal(),
                pedido.getStatus().name(),
                pedido.getDataPedido());
    }
}