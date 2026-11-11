package br.edu.agroassist.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "culturas")
public class Cultura {
    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    protected Cultura() {}

    public Cultura(String id, String nome) {
        this.id = Objects.requireNonNull(id);
        this.nome = Validacao.obrigatorio(nome, 60, "o nome da cultura");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
