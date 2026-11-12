package br.edu.seugame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuarios_usuario", columnNames = "usuario"),
        @UniqueConstraint(name = "uq_usuarios_email", columnNames = "email")
})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    protected Usuario() {}

    public Usuario(String nome, String usuario, String email, String senhaHash) {
        String login = DadosDominio.texto(usuario, 3, 50, "Usuário");
        if (!login.matches("[a-zA-Z0-9_.-]{3,50}")) throw new IllegalArgumentException("Usuário inválido.");
        String hash = DadosDominio.texto(senhaHash, 1, 255, "Hash da senha");
        if (!hash.matches("[\\x21-\\x7E]+")) throw new IllegalArgumentException("Hash deve ser ASCII sem espaços.");
        alterarPerfil(nome, email);
        this.usuario = login;
        this.senhaHash = hash;
    }

    public void alterarPerfil(String nome, String email) {
        String novoNome = DadosDominio.texto(nome, 3, 100, "Nome");
        String novoEmail = DadosDominio.texto(email, 3, 150, "E-mail");
        if (!novoEmail.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("E-mail inválido.");
        this.nome = novoNome;
        this.email = novoEmail;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getUsuario() { return usuario; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
}
