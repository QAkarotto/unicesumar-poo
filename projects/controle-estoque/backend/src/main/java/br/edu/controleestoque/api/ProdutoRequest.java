package br.edu.controleestoque.api;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** Usado no cadastro (POST) e na atualização (PUT). No PUT o SKU é ignorado (é imutável). */
public record ProdutoRequest(
        @NotBlank(message = "O SKU é obrigatório.")
        @Size(max = 30, message = "O SKU deve ter no máximo 30 caracteres.")
        String sku,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String descricao,

        @NotNull(message = "O preço unitário é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        @Digits(integer = 10, fraction = 2, message = "Preço com no máximo 2 casas decimais.")
        BigDecimal precoUnitario,

        @NotNull(message = "O estoque mínimo é obrigatório.")
        @PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
        Integer estoqueMinimo,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoriaId
) {
}
