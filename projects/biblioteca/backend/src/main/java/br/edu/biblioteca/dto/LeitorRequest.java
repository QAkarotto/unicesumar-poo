package br.edu.biblioteca.dto;
import jakarta.validation.constraints.*;

public record LeitorRequest(
    @NotBlank @Size(max = 120) String nome,
    @NotBlank @Email @Size(max = 180) String email
) {}
