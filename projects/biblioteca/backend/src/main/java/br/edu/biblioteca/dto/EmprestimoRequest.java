package br.edu.biblioteca.dto;
import jakarta.validation.constraints.*;

public record EmprestimoRequest(@NotNull @Positive Long livroId, @NotNull @Positive Long leitorId) {}
