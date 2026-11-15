package br.edu.estoque.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank String sku,
        @NotBlank String nome,
        @NotNull @PositiveOrZero BigDecimal preco,
        @PositiveOrZero int estoqueMinimo,
        @NotNull Long categoriaId) {}
