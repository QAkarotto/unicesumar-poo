package br.edu.estoque.dto;

import br.edu.estoque.domain.TipoMovimentacao;
import jakarta.validation.constraints.*;

public record MovimentacaoRequest(@NotNull Long produtoId, @NotNull TipoMovimentacao tipo, @Positive int quantidade) {}
