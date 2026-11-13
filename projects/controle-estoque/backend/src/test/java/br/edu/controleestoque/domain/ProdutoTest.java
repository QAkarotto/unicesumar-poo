package br.edu.controleestoque.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de unidade das regras de negócio do domínio (sem Spring, sem banco).
 */
class ProdutoTest {

    private Produto produto;
    private Fornecedor fornecedorVinculado;
    private Fornecedor outroFornecedor;

    @BeforeEach
    void setUp() {
        var categoria = new Categoria("Informática", null);
        produto = new Produto("inf-01", "Mouse", null, new BigDecimal("50.00"), 5, categoria);
        fornecedorVinculado = new Fornecedor("Tech Ltda", "11.222.333/0001-81", null, null);
        outroFornecedor = new Fornecedor("Outra Ltda", "44555666000199", null, null);
        produto.vincularFornecedor(fornecedorVinculado);
    }

    @Test
    @DisplayName("Entrada soma ao saldo e registra saldo anterior e posterior")
    void entradaDeveSomarAoSaldo() {
        // Act
        var mov = produto.registrarEntrada(10, fornecedorVinculado, "Compra");

        // Assert
        assertThat(produto.getQuantidadeEmEstoque()).isEqualTo(10);
        assertThat(mov.getTipo()).isEqualTo(TipoMovimentacao.ENTRADA);
        assertThat(mov.getSaldoAnterior()).isZero();
        assertThat(mov.getSaldoPosterior()).isEqualTo(10);
        assertThat(produto.possuiMovimentacoes()).isTrue();
    }

    @Test
    @DisplayName("Não deve permitir saída maior que o saldo (estoque negativo)")
    void naoDevePermitirSaidaMaiorQueSaldo() {
        // Arrange
        produto.registrarEntrada(3, fornecedorVinculado, null);

        // Act + Assert
        assertThatThrownBy(() -> produto.registrarSaida(4, null))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("saldo atual 3");
        assertThat(produto.getQuantidadeEmEstoque()).isEqualTo(3);
    }

    @Test
    @DisplayName("Saída válida subtrai do saldo")
    void saidaDeveSubtrairDoSaldo() {
        produto.registrarEntrada(8, fornecedorVinculado, null);

        var mov = produto.registrarSaida(8, "Venda");

        assertThat(produto.getQuantidadeEmEstoque()).isZero();
        assertThat(mov.getSaldoAnterior()).isEqualTo(8);
        assertThat(mov.getSaldoPosterior()).isZero();
    }

    @Test
    @DisplayName("Não deve aceitar entrada de fornecedor não vinculado ao produto")
    void naoDeveAceitarEntradaDeFornecedorNaoVinculado() {
        assertThatThrownBy(() -> produto.registrarEntrada(5, outroFornecedor, null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("não está vinculado");
        assertThat(produto.getQuantidadeEmEstoque()).isZero();
    }

    @Test
    @DisplayName("Entrada sem fornecedor não é permitida")
    void entradaExigeFornecedor() {
        assertThatThrownBy(() -> produto.registrarEntrada(5, null, null))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    @DisplayName("Quantidade da movimentação deve ser maior que zero")
    void quantidadeDeveSerPositiva() {
        assertThatThrownBy(() -> produto.registrarEntrada(0, fornecedorVinculado, null))
                .isInstanceOf(RegraNegocioException.class);
        assertThatThrownBy(() -> produto.registrarSaida(-1, null))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    @DisplayName("Produto inativo não pode ser movimentado")
    void produtoInativoNaoPodeSerMovimentado() {
        produto.inativar();

        assertThatThrownBy(() -> produto.registrarEntrada(1, fornecedorVinculado, null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("inativo");
    }

    @Test
    @DisplayName("Não deve inativar produto com saldo em estoque")
    void naoDeveInativarProdutoComSaldo() {
        produto.registrarEntrada(2, fornecedorVinculado, null);

        assertThatThrownBy(() -> produto.inativar())
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("saldo");
        assertThat(produto.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Ajuste define o novo saldo e registra a diferença")
    void ajusteDeveDefinirNovoSaldo() {
        produto.registrarEntrada(10, fornecedorVinculado, null);

        var mov = produto.ajustarEstoque(7, "Contagem física: 3 unidades avariadas");

        assertThat(produto.getQuantidadeEmEstoque()).isEqualTo(7);
        assertThat(mov.getTipo()).isEqualTo(TipoMovimentacao.AJUSTE);
        assertThat(mov.getQuantidade()).isEqualTo(3);
    }

    @Test
    @DisplayName("Ajuste exige justificativa e saldo diferente do atual")
    void ajusteExigeJustificativaESaldoDiferente() {
        produto.registrarEntrada(10, fornecedorVinculado, null);

        assertThatThrownBy(() -> produto.ajustarEstoque(5, "  "))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("justificativa");
        assertThatThrownBy(() -> produto.ajustarEstoque(10, "Recontagem"))
                .isInstanceOf(RegraNegocioException.class);
        assertThat(produto.getQuantidadeEmEstoque()).isEqualTo(10);
    }

    @Test
    @DisplayName("Indica reposição quando o saldo atinge o estoque mínimo")
    void deveIndicarReposicaoNoEstoqueMinimo() {
        produto.registrarEntrada(6, fornecedorVinculado, null);
        assertThat(produto.precisaReposicao()).isFalse();

        produto.registrarSaida(1, null); // saldo 5 = mínimo

        assertThat(produto.precisaReposicao()).isTrue();
    }

    @Test
    @DisplayName("Valor em estoque = preço x quantidade")
    void deveCalcularValorEmEstoque() {
        produto.registrarEntrada(4, fornecedorVinculado, null);

        assertThat(produto.valorEmEstoque()).isEqualByComparingTo("200.00");
    }

    @Test
    @DisplayName("Vínculo N:N mantém os dois lados sincronizados e não duplica")
    void vinculoComFornecedorDeveSerBidirecional() {
        assertThat(fornecedorVinculado.getProdutos()).contains(produto);
        assertThatThrownBy(() -> produto.vincularFornecedor(fornecedorVinculado))
                .isInstanceOf(RegraNegocioException.class);

        produto.desvincularFornecedor(fornecedorVinculado);

        assertThat(produto.getFornecedores()).isEmpty();
        assertThat(fornecedorVinculado.getProdutos()).isEmpty();
    }
}
