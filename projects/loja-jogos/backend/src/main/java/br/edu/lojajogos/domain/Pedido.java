package br.edu.lojajogos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos pedidos para um cliente -> coluna cliente_id (FK) na tabela pedido
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Muitos pedidos para um jogo -> coluna jogo_id (FK) na tabela pedido
    @ManyToOne(optional = false)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    // Grava o nome do enum ("CONFIRMADO") em vez do número (0)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    protected Pedido() {
    }

    /**
     * Cria um pedido já confirmado.
     * Quem baixa o estoque é o próprio Jogo (regra de negócio 1).
     */
    public Pedido(Cliente cliente, Jogo jogo, int quantidade) {
        if (cliente == null) {
            throw new IllegalArgumentException("O pedido precisa de um cliente.");
        }
        if (jogo == null) {
            throw new IllegalArgumentException("O pedido precisa de um jogo.");
        }

        jogo.baixarEstoque(quantidade);

        this.cliente = cliente;
        this.jogo = jogo;
        this.quantidade = quantidade;
        this.valorTotal = jogo.getPreco().multiply(BigDecimal.valueOf(quantidade));
        this.status = StatusPedido.CONFIRMADO;
        this.dataPedido = LocalDateTime.now();
    }

    /**
     * Regra de negócio 2: só pedido CONFIRMADO pode ser cancelado,
     * e o cancelamento devolve os jogos ao estoque.
     */
    public void cancelar() {
        if (status != StatusPedido.CONFIRMADO) {
            throw new IllegalStateException("Somente pedidos confirmados podem ser cancelados.");
        }
        jogo.devolverEstoque(quantidade);
        this.status = StatusPedido.CANCELADO;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
}