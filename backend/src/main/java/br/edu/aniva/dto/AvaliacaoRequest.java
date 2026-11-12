package br.edu.aniva.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequest(
        @NotNull(message = "Informe a nota.")
        @Min(value = 1, message = "A nota deve ser de 1 a 10.")
        @Max(value = 10, message = "A nota deve ser de 1 a 10.")
        Integer nota) {
}
