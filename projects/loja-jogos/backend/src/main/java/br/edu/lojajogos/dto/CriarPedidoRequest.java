package br.edu.lojajogos.dto;

public record CriarPedidoRequest(Long clienteId, Long jogoId, Integer quantidade) {
}