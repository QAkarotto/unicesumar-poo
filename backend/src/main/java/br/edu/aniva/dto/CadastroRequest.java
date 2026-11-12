package br.edu.aniva.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroRequest(
        @NotBlank(message = "Digite seu nome para criar a conta.")
        @Size(min = 2, max = 80, message = "O nome deve ter entre 2 e 80 caracteres.")
        String nome,

        @NotBlank(message = "Informe um e-mail.")
        @Email(message = "E-mail inválido.")
        @Size(max = 150, message = "O e-mail pode ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "Informe uma senha.")
        @Size(min = 6, max = 72, message = "A senha precisa ter entre 6 e 72 caracteres.")
        String senha) {
}
