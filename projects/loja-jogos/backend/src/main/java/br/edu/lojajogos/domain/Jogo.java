package br.edu.lojajogos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 50)
    private String plataforma;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    protected Jogo() {
    }

    public Jogo(String titulo, String plataforma, BigDecimal preco, Integer estoque) {
        atualizar(titulo, plataforma, preco, estoque);
    }

    /**
     * Altera os dados do jogo. As mesmas validações do cadastro valem aqui,
     * então o objeto nunca fica com preço zerado ou estoque negativo.
     */
    public void atualizar(String titulo, String plataforma, BigDecimal preco, Integer estoque) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título do jogo é obrigatório.");
        }
        if (plataforma == null || plataforma.isBlank()) {
            throw new IllegalArgumentException("A plataforma do jogo é obrigatória.");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (estoque == null || estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.titulo = titulo.trim();
        this.plataforma = plataforma.trim();
        this.preco = preco;
        this.estoque = estoque;
    }

    /**
     * Regra de negócio 1: só é possível vender o que existe em estoque.
     */
    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (quantidade > estoque) {
            throw new IllegalStateException(
                    "Estoque insuficiente para \"" + titulo + "\". Disponível: " + estoque + ".");
        }
        this.estoque -= quantidade;
    }

    /**
     * Usado quando um pedido é cancelado: o jogo volta para o estoque.
     */
    public void devolverEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.estoque += quantidade;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getEstoque() {
        return estoque;
    }
}