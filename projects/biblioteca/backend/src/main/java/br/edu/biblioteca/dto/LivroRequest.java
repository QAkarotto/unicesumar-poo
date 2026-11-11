package br.edu.biblioteca.dto;
import jakarta.validation.constraints.*;

public record LivroRequest(
    @NotBlank @Size(max = 180) String titulo,
    @NotBlank @Size(max = 120) String autor,
    @NotBlank @Pattern(regexp = "\\d{13}", message = "deve conter 13 dígitos") String isbn,
    @NotBlank @Size(max = 60) String categoria,
    @NotNull @Min(1) @Max(10000) Integer exemplares
) {}
