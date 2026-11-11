package br.edu.lojajogos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JogoTest {

    private Jogo novoJogo(int estoque) {
        return new Jogo("Minecraft", "PC", new BigDecimal("99.90"), estoque);
    }

    @Test
    @DisplayName("Não deve cadastrar jogo com preço zero")
    void naoDeveCadastrarJogoComPrecoZero() {
        // Arrange
        var precoZero = BigDecimal.ZERO;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Jogo("Minecraft", "PC", precoZero, 5));
    }

    @Test
    @DisplayName("Não deve cadastrar jogo com estoque negativo")
    void naoDeveCadastrarJogoComEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Jogo("Minecraft", "PC", new BigDecimal("99.90"), -1));
    }

    @Test
    @DisplayName("Baixar estoque deve diminuir a quantidade disponível")
    void baixarEstoqueDeveDiminuirQuantidade() {
        // Arrange
        var jogo = novoJogo(5);

        // Act
        jogo.baixarEstoque(2);

        // Assert
        assertEquals(3, jogo.getEstoque());
    }

    @Test
    @DisplayName("Não deve vender mais do que existe em estoque")
    void naoDeveVenderMaisQueOEstoque() {
        // Arrange
        var jogo = novoJogo(1);

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> jogo.baixarEstoque(2));
        assertEquals(1, jogo.getEstoque(), "O estoque não pode mudar quando a venda é recusada");
    }

    @Test
    @DisplayName("Alteração inválida não deve mudar o jogo")
    void alteracaoInvalidaNaoDeveMudarOJogo() {
        // Arrange
        var jogo = novoJogo(5);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> jogo.atualizar("Minecraft", "PC", new BigDecimal("-10"), 5));
        assertEquals(new BigDecimal("99.90"), jogo.getPreco());
    }
}