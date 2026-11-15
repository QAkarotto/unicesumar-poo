package br.edu.estoque.domain;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.estoque.exception.RegraNegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProdutoTest {
    private Produto produto;

    @BeforeEach
    void setup() {
        produto = new Produto("SKU-1", "Teclado", new BigDecimal("99.90"), 5, new Categoria("Periféricos"));
    }

    @Test
    void entradaAumentaOSaldo() {
        var mov = produto.registrarEntrada(10);
        assertEquals(10, produto.getQuantidade());
        assertEquals(TipoMovimentacao.ENTRADA, mov.getTipo());
    }

    @Test
    void naoDevePermitirSaidaMaiorQueOSaldo() {
        produto.registrarEntrada(3);
        assertThrows(RegraNegocioException.class, () -> produto.registrarSaida(4));
        assertEquals(3, produto.getQuantidade());
    }

    @Test
    void naoDevePermitirQuantidadeZeroOuNegativa() {
        assertThrows(RegraNegocioException.class, () -> produto.registrarEntrada(0));
        assertThrows(RegraNegocioException.class, () -> produto.registrarSaida(-1));
    }

    @Test
    void naoDeveDesativarProdutoComSaldo() {
        produto.registrarEntrada(2);
        assertThrows(RegraNegocioException.class, produto::desativar);
    }

    @Test
    void produtoInativoNaoPodeSerMovimentado() {
        produto.desativar();
        assertThrows(RegraNegocioException.class, () -> produto.registrarEntrada(1));
    }

    @Test
    void sinalizaEstoqueAbaixoDoMinimo() {
        produto.registrarEntrada(5);
        assertFalse(produto.abaixoDoMinimo());
        produto.registrarSaida(1);
        assertTrue(produto.abaixoDoMinimo());
    }
}
