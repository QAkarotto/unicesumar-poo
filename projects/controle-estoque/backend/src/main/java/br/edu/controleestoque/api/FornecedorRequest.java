package br.edu.controleestoque.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @Pattern(regexp = "\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}",
                message = "CNPJ inválido. Use 14 dígitos (com ou sem máscara).")
        String cnpj,

        @Email(message = "E-mail inválido.")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
        String email,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefone
) {
}
