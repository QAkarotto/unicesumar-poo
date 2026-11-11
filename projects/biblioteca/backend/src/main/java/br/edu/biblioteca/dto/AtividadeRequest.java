package br.edu.biblioteca.dto;
import jakarta.validation.constraints.NotNull;
public record AtividadeRequest(@NotNull Boolean ativo) {}
