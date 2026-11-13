package br.edu.biblioteca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarLivroRequest(
        @NotBlank(message = "isbn é obrigatório")
        String isbn,

        @NotBlank(message = "titulo é obrigatório")
        String titulo,

        @NotBlank(message = "autor é obrigatório")
        String autor,

        @NotNull(message = "quantidadeTotal é obrigatória")
        @Min(value = 1, message = "quantidadeTotal deve ser pelo menos 1")
        Integer quantidadeTotal
) {
}
