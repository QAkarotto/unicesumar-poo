package br.edu.lojajogos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Construtor vazio exigido pelo JPA (ele cria o objeto e depois preenche os campos).
    protected Cliente() {
    }

    public Cliente(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
        this.nome = nome.trim();
        this.email = email.trim().toLowerCase();
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
}