package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// Personagem principal de uma obra. Tabela obra_personagem.
@Embeddable
public record Personagem(
        @Column(name = "nome", nullable = false, length = 100) String nome,
        @Column(name = "papel", length = 100) String papel,
        @Column(name = "descricao", length = 500) String descricao) {
}
