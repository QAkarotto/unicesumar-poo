package br.edu.controleestoque.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Entidade central do domínio. O saldo em estoque é encapsulado: não existe
 * setter de quantidade; ele só muda através de entrada, saída ou ajuste,
 * e cada mudança gera uma {@link Movimentacao}.
 */
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30, updatable = false)
    private String sku;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_em_estoque", nullable = false)
    private int quantidadeEmEstoque;

    @Column(name = "estoque_minimo", nullable = false)
    private int estoqueMinimo;

    @Column(nullable = false)
    private boolean ativo = true;

    /** N:1 -> FK produto.categoria_id */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /** N:N -> tabela associativa produto_fornecedor (lado dono do relacionamento) */
    @ManyToMany
    @JoinTable(name = "produto_fornecedor",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "fornecedor_id"))
    private Set<Fornecedor> fornecedores = new HashSet<>();

    /** 1:N -> histórico; lado inverso de Movimentacao.produto */
    @OneToMany(mappedBy = "produto")
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    protected Produto() {
    }

    public Produto(String sku, String nome, String descricao, BigDecimal precoUnitario,
                   int estoqueMinimo, Categoria categoria) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("O SKU é obrigatório.");
        }
        this.sku = sku.trim().toUpperCase();
        this.quantidadeEmEstoque = 0;
        atualizarDados(nome, descricao, precoUnitario, estoqueMinimo, categoria);
    }

    public void atualizarDados(String nome, String descricao, BigDecimal precoUnitario,
                               int estoqueMinimo, Categoria categoria) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (precoUnitario == null || precoUnitario.signum() < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo.");
        }
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }
        this.categoria = Objects.requireNonNull(categoria, "A categoria é obrigatória.");
        this.nome = nome.trim();
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.estoqueMinimo = estoqueMinimo;
    }

    // ------------------------------------------------------------------
    // Regras de movimentação de estoque
    // ------------------------------------------------------------------

    /**
     * Regra: a entrada precisa vir de um fornecedor vinculado ao produto.
     */
    public Movimentacao registrarEntrada(int quantidade, Fornecedor fornecedor, String observacao) {
        garantirAtivo();
        garantirQuantidadePositiva(quantidade);
        if (fornecedor == null) {
            throw new RegraNegocioException("A entrada de estoque exige um fornecedor.");
        }
        if (!fornecedores.contains(fornecedor)) {
            throw new RegraNegocioException("O fornecedor " + fornecedor.getNome()
                    + " não está vinculado ao produto " + sku + ".");
        }
        return alterarSaldo(TipoMovimentacao.ENTRADA, quantidade,
                quantidadeEmEstoque + quantidade, fornecedor, observacao);
    }

    /**
     * Regra: o saldo nunca pode ficar negativo.
     */
    public Movimentacao registrarSaida(int quantidade, String observacao) {
        garantirAtivo();
        garantirQuantidadePositiva(quantidade);
        if (quantidade > quantidadeEmEstoque) {
            throw new EstoqueInsuficienteException(sku, quantidadeEmEstoque, quantidade);
        }
        return alterarSaldo(TipoMovimentacao.SAIDA, quantidade,
                quantidadeEmEstoque - quantidade, null, observacao);
    }

    /**
     * Ajuste de inventário: define o saldo a partir de uma contagem física.
     * Regra: exige justificativa e o novo saldo deve ser diferente do atual.
     */
    public Movimentacao ajustarEstoque(int novoSaldo, String justificativa) {
        garantirAtivo();
        if (novoSaldo < 0) {
            throw new RegraNegocioException("O saldo ajustado não pode ser negativo.");
        }
        if (justificativa == null || justificativa.isBlank()) {
            throw new RegraNegocioException("O ajuste de estoque exige uma justificativa.");
        }
        if (novoSaldo == quantidadeEmEstoque) {
            throw new RegraNegocioException("O novo saldo é igual ao saldo atual; nada a ajustar.");
        }
        int diferenca = Math.abs(novoSaldo - quantidadeEmEstoque);
        return alterarSaldo(TipoMovimentacao.AJUSTE, diferenca, novoSaldo, null, justificativa);
    }

    private Movimentacao alterarSaldo(TipoMovimentacao tipo, int quantidade, int novoSaldo,
                                      Fornecedor fornecedor, String observacao) {
        int saldoAnterior = this.quantidadeEmEstoque;
        this.quantidadeEmEstoque = novoSaldo;
        var movimentacao = new Movimentacao(this, tipo, quantidade, saldoAnterior, novoSaldo,
                fornecedor, observacao);
        movimentacoes.add(movimentacao);
        return movimentacao;
    }

    private void garantirAtivo() {
        if (!ativo) {
            throw new RegraNegocioException("O produto " + sku + " está inativo e não pode ser movimentado.");
        }
    }

    private static void garantirQuantidadePositiva(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }
    }

    // ------------------------------------------------------------------
    // Estado do produto
    // ------------------------------------------------------------------

    /** Regra: produto com saldo não pode ser inativado (saldo ficaria "perdido"). */
    public void inativar() {
        if (!ativo) {
            throw new RegraNegocioException("O produto já está inativo.");
        }
        if (quantidadeEmEstoque > 0) {
            throw new RegraNegocioException("Não é possível inativar um produto com saldo em estoque ("
                    + quantidadeEmEstoque + " unidades). Zere o saldo antes.");
        }
        this.ativo = false;
    }

    public void ativar() {
        if (ativo) {
            throw new RegraNegocioException("O produto já está ativo.");
        }
        this.ativo = true;
    }

    /** Alerta de reposição: saldo atingiu ou ficou abaixo do mínimo. */
    public boolean precisaReposicao() {
        return ativo && quantidadeEmEstoque <= estoqueMinimo;
    }

    public BigDecimal valorEmEstoque() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidadeEmEstoque));
    }

    /** Produto com histórico não pode ser apagado (perderia a rastreabilidade). */
    public boolean possuiMovimentacoes() {
        return !movimentacoes.isEmpty();
    }

    // ------------------------------------------------------------------
    // Vínculo N:N com fornecedores (mantém os dois lados sincronizados)
    // ------------------------------------------------------------------

    public void vincularFornecedor(Fornecedor fornecedor) {
        if (!fornecedores.add(fornecedor)) {
            throw new RegraNegocioException("O fornecedor já está vinculado a este produto.");
        }
        fornecedor.adicionarProduto(this);
    }

    public void desvincularFornecedor(Fornecedor fornecedor) {
        if (fornecedores.remove(fornecedor)) {
            fornecedor.removerProduto(this);
        }
    }

    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Set<Fornecedor> getFornecedores() {
        return Collections.unmodifiableSet(fornecedores);
    }

    public List<Movimentacao> getMovimentacoes() {
        return Collections.unmodifiableList(movimentacoes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto outro)) return false;
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Produto.class.hashCode();
    }
}
