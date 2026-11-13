
package br.edu.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LeitorRequest(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150)
    String nome,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150)
    String email,

    @Size(max = 20)
    String telefone
) {
}
