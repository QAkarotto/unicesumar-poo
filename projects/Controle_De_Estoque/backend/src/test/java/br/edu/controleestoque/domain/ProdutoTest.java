package br.edu.controleestoque.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void criarProduto() {
        produto = new Produto("P001", "Teclado", new Categoria("Periféricos"), 5);
    }

    @Test
    void naoDevePermitirSaidaMaiorQueOSaldo() {
        produto.registrarEntrada(10, "Compra");

        assertThrows(IllegalStateException.class, () -> produto.registrarSaida(11, "Venda"));
        assertThat(produto.getQuantidade()).isEqualTo(10);
    }

    @Test
    void deveAtualizarSaldoEGerarMovimentacaoNaSaida() {
        produto.registrarEntrada(10, "Compra");

        Movimentacao saida = produto.registrarSaida(4, "Venda");

        assertThat(produto.getQuantidade()).isEqualTo(6);
        assertThat(saida.getTipo()).isEqualTo(TipoMovimentacao.SAIDA);
        assertThat(saida.getQuantidade()).isEqualTo(4);
    }

    @Test
    void naoDevePermitirMovimentarProdutoInativo() {
        produto.inativar();

        assertThrows(IllegalStateException.class, () -> produto.registrarEntrada(1, null));
    }

    @Test
    void naoDevePermitirInativarProdutoComEstoque() {
        produto.registrarEntrada(3, null);

        assertThrows(IllegalStateException.class, produto::inativar);
        assertThat(produto.isAtivo()).isTrue();
    }

    @Test
    void naoDevePermitirQuantidadeZeroOuNegativa() {
        assertThrows(IllegalArgumentException.class, () -> produto.registrarEntrada(0, null));
        assertThrows(IllegalArgumentException.class, () -> produto.registrarEntrada(-2, null));
    }

    @Test
    void deveIndicarEstoqueBaixoQuandoSaldoFicaAbaixoDoMinimo() {
        produto.registrarEntrada(6, null);
        assertThat(produto.estoqueBaixo()).isFalse();

        produto.registrarSaida(2, null);
        assertThat(produto.estoqueBaixo()).isTrue();
    }
}
