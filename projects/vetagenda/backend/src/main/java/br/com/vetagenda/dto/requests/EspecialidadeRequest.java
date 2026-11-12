package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.NotBlank;
public record EspecialidadeRequest(@NotBlank String nome, String descricao) {}