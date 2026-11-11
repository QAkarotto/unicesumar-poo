package br.edu.lojajogos.dto;

import br.edu.lojajogos.domain.Jogo;

import java.math.BigDecimal;

public record JogoResponse(Long id, String titulo, String plataforma, BigDecimal preco, Integer estoque) {

    public static JogoResponse de(Jogo jogo) {
        return new JogoResponse(
                jogo.getId(),
                jogo.getTitulo(),
                jogo.getPlataforma(),
                jogo.getPreco(),
                jogo.getEstoque());
    }
}