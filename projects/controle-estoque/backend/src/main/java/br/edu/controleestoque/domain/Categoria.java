package br.edu.controleestoque.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agrupa produtos. Relacionamento 1:N com {@link Produto}
 * (a FK categoria_id fica na tabela produto).
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(length = 255)
    private String descricao;

    /** Lado inverso: quem mantém a FK é Produto.categoria. */
    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos = new ArrayList<>();

    protected Categoria() {
        // exigido pelo JPA
    }

    public Categoria(String nome, String descricao) {
        alterarDados(nome, descricao);
    }

    public void alterarDados(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
        this.nome = nome.trim();
        this.descricao = descricao;
    }

    public boolean possuiProdutos() {
        return !produtos.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }
}
