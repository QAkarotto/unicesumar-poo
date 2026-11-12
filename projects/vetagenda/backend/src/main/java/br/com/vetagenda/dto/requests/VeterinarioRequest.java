package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record VeterinarioRequest(@NotBlank String nome, @NotBlank String crmv,
                                 String telefone, @NotNull Long especialidadeId) {}