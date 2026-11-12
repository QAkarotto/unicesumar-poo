package br.com.vetagenda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "veterinario")
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 30)
    private String crmv;

    @Column(length = 20)
    private String telefone;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    protected Veterinario() {}

    public Veterinario(String nome, String crmv, String telefone, Especialidade especialidade) {
        this.nome = nome;
        this.crmv = crmv;
        this.telefone = telefone;
        this.especialidade = especialidade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCrmv() { return crmv; }
    public String getTelefone() { return telefone; }
    public Especialidade getEspecialidade() { return especialidade; }
}
