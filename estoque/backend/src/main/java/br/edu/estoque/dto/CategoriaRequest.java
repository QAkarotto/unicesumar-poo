package br.edu.estoque.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(@NotBlank String nome) {}
