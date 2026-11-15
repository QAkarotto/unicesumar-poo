package br.edu.estoque.domain;

import br.edu.estoque.exception.RegraNegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String sku;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private int quantidade;
    @Column(name = "estoque_minimo", nullable = false)
    private int estoqueMinimo;
    @Column(nullable = false)
    private boolean ativo = true;
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    protected Produto() {}

    public Produto(String sku, String nome, BigDecimal preco, int estoqueMinimo, Categoria categoria) {
        atualizar(nome, preco, estoqueMinimo, categoria);
        this.sku = sku;
    }

    public void atualizar(String nome, BigDecimal preco, int estoqueMinimo, Categoria categoria) {
        if (preco.signum() < 0) throw new RegraNegocioException("preco", "O preço não pode ser negativo.");
        if (estoqueMinimo < 0) throw new RegraNegocioException("estoqueMinimo", "O estoque mínimo não pode ser negativo.");
        this.nome = nome;
        this.preco = preco;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
    }

    /** Regra: só movimenta produto ativo e com quantidade positiva. */
    public Movimentacao registrarEntrada(int qtd) {
        validarMovimentacao(qtd);
        quantidade += qtd;
        return new Movimentacao(this, TipoMovimentacao.ENTRADA, qtd);
    }

    /** Regra: a saída não pode deixar o estoque negativo. */
    public Movimentacao registrarSaida(int qtd) {
        validarMovimentacao(qtd);
        if (qtd > quantidade)
            throw new RegraNegocioException("quantidade", "Estoque insuficiente: disponível " + quantidade + ", solicitado " + qtd + ".");
        quantidade -= qtd;
        return new Movimentacao(this, TipoMovimentacao.SAIDA, qtd);
    }

    /** Regra: produto com saldo em estoque não pode ser desativado. */
    public void desativar() {
        if (quantidade > 0) throw new RegraNegocioException("Não é possível desativar produto com saldo em estoque.");
        ativo = false;
    }

    public boolean abaixoDoMinimo() { return quantidade < estoqueMinimo; }

    private void validarMovimentacao(int qtd) {
        if (!ativo) throw new RegraNegocioException("Produto inativo não pode ser movimentado.");
        if (qtd <= 0) throw new RegraNegocioException("quantidade", "A quantidade deve ser maior que zero.");
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public boolean isAtivo() { return ativo; }
    public Categoria getCategoria() { return categoria; }
}
