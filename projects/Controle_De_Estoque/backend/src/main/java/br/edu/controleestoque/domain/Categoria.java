package br.edu.controleestoque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    protected Categoria() {
    }

    public Categoria(String nome) {
        renomear(nome);
    }

    public void renomear(String nome) {
        this.nome = Validacao.textoObrigatorio(nome, "O nome da categoria é obrigatório.");
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
