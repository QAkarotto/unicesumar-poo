package br.edu.controleestoque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Produto do estoque e dono das regras de saldo.
 *
 * A quantidade nunca é alterada diretamente: ela só muda por registrarEntrada
 * e registrarSaida, que validam a operação, atualizam o saldo e devolvem a
 * Movimentacao gerada. Assim o saldo e o histórico não ficam inconsistentes.
 *
 * Regras:
 * - o saldo nunca fica negativo;
 * - produto inativo não recebe movimentações;
 * - só é possível inativar um produto com saldo zerado.
 */
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 30)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "estoque_minimo", nullable = false)
    private int estoqueMinimo;

    @Column(nullable = false)
    private boolean ativo;

    protected Produto() {
    }

    public Produto(String codigo, String nome, Categoria categoria, int estoqueMinimo) {
        this.codigo = Validacao.textoObrigatorio(codigo, "O código do produto é obrigatório.");
        atualizar(nome, categoria, estoqueMinimo);
        this.quantidade = 0;
        this.ativo = true;
    }

    public void atualizar(String nome, Categoria categoria, int estoqueMinimo) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria do produto é obrigatória.");
        }
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }
        this.nome = Validacao.textoObrigatorio(nome, "O nome do produto é obrigatório.");
        this.categoria = categoria;
        this.estoqueMinimo = estoqueMinimo;
    }

    public Movimentacao registrarEntrada(int quantidade, String motivo) {
        validarMovimentacao(quantidade);
        this.quantidade += quantidade;
        return new Movimentacao(this, TipoMovimentacao.ENTRADA, quantidade, motivo);
    }

    public Movimentacao registrarSaida(int quantidade, String motivo) {
        validarMovimentacao(quantidade);
        if (quantidade > this.quantidade) {
            throw new IllegalStateException("Estoque insuficiente: saldo de " + this.quantidade
                    + " e saída solicitada de " + quantidade + ".");
        }
        this.quantidade -= quantidade;
        return new Movimentacao(this, TipoMovimentacao.SAIDA, quantidade, motivo);
    }

    public void inativar() {
        if (quantidade > 0) {
            throw new IllegalStateException("Não é possível inativar um produto que ainda tem estoque.");
        }
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public boolean estoqueBaixo() {
        return quantidade < estoqueMinimo;
    }

    private void validarMovimentacao(int quantidade) {
        if (!ativo) {
            throw new IllegalStateException("Produto inativo não pode ser movimentado.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade da movimentação deve ser maior que zero.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
