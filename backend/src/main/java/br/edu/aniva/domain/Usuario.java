package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    protected Usuario() {
        // exigido pelo JPA
    }

    public Usuario(String nome, String email, String senhaHash) {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("Digite seu nome para criar a conta.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Informe um e-mail.");
        }
        this.nome = nome.trim();
        this.email = email.trim().toLowerCase();
        this.senhaHash = senhaHash;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }
}
