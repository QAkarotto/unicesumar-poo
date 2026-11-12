package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// Onde assistir ou ler. categoria = "streaming" ou "leitura". Tabela obra_plataforma.
@Embeddable
public record Plataforma(
        @Column(name = "categoria", nullable = false, length = 20) String categoria,
        @Column(name = "nome", nullable = false, length = 100) String nome,
        @Column(name = "url", nullable = false, length = 500) String url) {
}
