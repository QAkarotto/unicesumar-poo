package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record AtendimentoRequest(@NotBlank String diagnostico, String procedimento,
                                 String observacoes, @NotNull Long agendamentoId) {}