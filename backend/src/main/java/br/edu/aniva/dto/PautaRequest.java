package br.edu.aniva.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// Atualização da pauta. Pelo menos um dos campos deve ser informado.
public record PautaRequest(
        String status,

        @Min(value = 0, message = "O progresso deve estar entre 0 e 100.")
        @Max(value = 100, message = "O progresso deve estar entre 0 e 100.")
        Integer progresso) {
}
