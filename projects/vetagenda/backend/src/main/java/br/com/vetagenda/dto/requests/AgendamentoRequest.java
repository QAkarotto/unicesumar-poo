package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
public record AgendamentoRequest(
    @NotNull @Future LocalDateTime dataHora,
    @NotBlank String motivo,
    String observacoes,
    @NotNull Long animalId,
    @NotNull Long veterinarioId
) {}