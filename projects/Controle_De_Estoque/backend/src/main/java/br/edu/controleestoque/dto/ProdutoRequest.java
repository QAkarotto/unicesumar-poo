package br.edu.controleestoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(
        @NotBlank(message = "O código do produto é obrigatório.")
        @Size(max = 30, message = "O código deve ter no máximo 30 caracteres.")
        String codigo,

        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoriaId,

        @NotNull(message = "O estoque mínimo é obrigatório.")
        @PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
        Integer estoqueMinimo
) {
}
