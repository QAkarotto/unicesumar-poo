package br.edu.lojajogos.dto;

import java.math.BigDecimal;

// Usado tanto no cadastro (POST) quanto na alteração (PUT) de um jogo.
public record JogoRequest(String titulo, String plataforma, BigDecimal preco, Integer estoque) {
}