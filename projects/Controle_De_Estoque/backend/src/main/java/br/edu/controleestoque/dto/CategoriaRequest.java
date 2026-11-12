package br.edu.controleestoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 80, message = "O nome da categoria deve ter no máximo 80 caracteres.")
        String nome
) {
}
