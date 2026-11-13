
package br.edu.biblioteca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroRequest(
    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 200)
    String titulo,

    @NotBlank(message = "O autor é obrigatório.")
    @Size(max = 150)
    String autor,

    @NotBlank(message = "O ISBN é obrigatório.")
    @Size(max = 20)
    String isbn,

    @NotNull(message = "A quantidade total é obrigatória.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    Integer quantidadeTotal
) {
}
