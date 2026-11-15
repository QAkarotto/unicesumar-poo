package br.edu.estoque.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    protected Categoria() {}
    public Categoria(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
