package br.edu.controleestoque.domain;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Empresa que fornece produtos. Relacionamento N:N com {@link Produto}
 * através da tabela associativa produto_fornecedor.
 */
@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 14, columnDefinition = "char(14)")
    private String cnpj;

    @Column(length = 120)
    private String email;

    @Column(length = 20)
    private String telefone;

    /** Lado inverso do N:N: o dono do relacionamento é Produto.fornecedores. */
    @ManyToMany(mappedBy = "fornecedores")
    private Set<Produto> produtos = new HashSet<>();

    protected Fornecedor() {
    }

    public Fornecedor(String nome, String cnpj, String email, String telefone) {
        alterarDados(nome, cnpj, email, telefone);
    }

    public void alterarDados(String nome, String cnpj, String email, String telefone) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do fornecedor é obrigatório.");
        }
        this.nome = nome.trim();
        this.cnpj = normalizarCnpj(cnpj);
        this.email = email;
        this.telefone = telefone;
    }

    /** Aceita "11.222.333/0001-81" ou "11222333000181" e guarda só os dígitos. */
    public static String normalizarCnpj(String cnpj) {
        String digitos = cnpj == null ? "" : cnpj.replaceAll("\\D", "");
        if (digitos.length() != 14) {
            throw new IllegalArgumentException("O CNPJ deve conter 14 dígitos.");
        }
        return digitos;
    }

    /** Usado na exclusão: remove os vínculos N:N antes de apagar o fornecedor. */
    public void desvincularDeTodosOsProdutos() {
        for (Produto produto : Set.copyOf(produtos)) {
            produto.desvincularFornecedor(this);
        }
    }

    // chamados por Produto para manter os dois lados do N:N sincronizados
    void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Set<Produto> getProdutos() {
        return Collections.unmodifiableSet(produtos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fornecedor outro)) return false;
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Fornecedor.class.hashCode();
    }
}
