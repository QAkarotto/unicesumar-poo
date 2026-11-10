package br.edu.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarLivroRequest(
        @NotBlank(message = "titulo é obrigatório")
        String titulo,

        @NotBlank(message = "autor é obrigatório")
        String autor
) {
}
