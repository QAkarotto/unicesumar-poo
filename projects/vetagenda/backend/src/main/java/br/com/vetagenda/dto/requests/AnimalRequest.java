package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
public record AnimalRequest(
    @NotBlank String nome,
    @NotBlank String especie,
    String raca,
    String sexo,
    LocalDate dataNascimento,
    BigDecimal peso,
    @NotNull Long tutorId
) {}