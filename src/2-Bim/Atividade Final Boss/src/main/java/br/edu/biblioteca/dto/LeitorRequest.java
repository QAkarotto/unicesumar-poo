package br.edu.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LeitorRequest(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        String email
) {
}
