package br.com.vetagenda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "especialidade")
public class Especialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    protected Especialidade() {}

    public Especialidade(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
