package br.edu.biblioteca.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    protected Usuario() {}

    public Usuario(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("O e-mail do usuário é inválido.");
        }
        this.nome = nome.trim();
        this.email = email.trim();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
}