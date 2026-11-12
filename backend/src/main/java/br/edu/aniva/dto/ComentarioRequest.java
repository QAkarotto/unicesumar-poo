package br.edu.aniva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(
        @NotBlank(message = "Escreva algo antes de publicar.")
        @Size(max = 1000, message = "O comentário pode ter no máximo 1000 caracteres.")
        String texto,

        boolean spoiler) {
}
