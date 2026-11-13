package br.edu.controleestoque.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registro imutável de uma alteração no saldo de um produto.
 * Só é criada pelo próprio {@link Produto} (construtor com visibilidade de pacote),
 * garantindo que toda movimentação corresponda a uma mudança de saldo válida.
 */
@Entity
@Table(name = "movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** N:1 -> FK movimentacao.produto_id */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /** N:0..1 -> FK movimentacao.fornecedor_id (obrigatória apenas para ENTRADA) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "saldo_anterior", nullable = false)
    private int saldoAnterior;

    @Column(name = "saldo_posterior", nullable = false)
    private int saldoPosterior;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 255)
    private String observacao;

    protected Movimentacao() {
    }

    Movimentacao(Produto produto, TipoMovimentacao tipo, int quantidade, int saldoAnterior,
                 int saldoPosterior, Fornecedor fornecedor, String observacao) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.fornecedor = fornecedor;
        this.observacao = observacao;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getSaldoAnterior() {
        return saldoAnterior;
    }

    public int getSaldoPosterior() {
        return saldoPosterior;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getObservacao() {
        return observacao;
    }
}
