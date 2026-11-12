package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// Formato em que a obra existe.Tabela obra_formato.
@Embeddable
public record Formato(
        @Column(name = "tipo", nullable = false, length = 20) String tipo,
        @Column(name = "extensao", length = 100) String extensao,
        @Column(name = "observacao", length = 200) String observacao) {
}
