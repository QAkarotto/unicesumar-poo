
package br.edu.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmprestimoRequest(
    @NotNull(message = "O ID do livro é obrigatório.")
    @Positive(message = "O ID do livro deve ser positivo.")
    Long livroId,

    @NotNull(message = "O ID do leitor é obrigatório.")
    @Positive(message = "O ID do leitor deve ser positivo.")
    Long leitorId
) {
}
