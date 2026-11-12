package br.edu.aniva.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Informe o e-mail.") String email,
        @NotBlank(message = "Informe a senha.") String senha) {
}
