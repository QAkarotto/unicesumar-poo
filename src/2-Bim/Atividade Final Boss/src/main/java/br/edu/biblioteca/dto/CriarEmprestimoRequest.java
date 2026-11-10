package br.edu.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

public record CriarEmprestimoRequest(
        @NotNull(message = "leitorId é obrigatório")
        Long leitorId,

        @NotNull(message = "livroId é obrigatório")
        Long livroId
) {
}
