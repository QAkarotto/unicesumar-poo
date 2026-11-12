package br.com.vetagenda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 60)
    private String especie;

    @Column(length = 80)
    private String raca;

    @Column(length = 20)
    private String sexo;

    private LocalDate dataNascimento;

    @Column(precision = 6, scale = 2)
    private BigDecimal peso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    protected Animal() {}

    public Animal(String nome, String especie, String raca, String sexo,
                  LocalDate dataNascimento, BigDecimal peso, Tutor tutor) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.peso = peso;
        this.tutor = tutor;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public String getRaca() { return raca; }
    public String getSexo() { return sexo; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public BigDecimal getPeso() { return peso; }
    public Tutor getTutor() { return tutor; }

    public void atualizar(String nome, String especie, String raca, String sexo,
                          LocalDate dataNascimento, BigDecimal peso, Tutor tutor) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.peso = peso;
        this.tutor = tutor;
    }
}
