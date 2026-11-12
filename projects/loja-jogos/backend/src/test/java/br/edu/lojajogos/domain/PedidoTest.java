package br.edu.lojajogos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    private final Cliente cliente = new Cliente("Maria Souza", "maria@email.com");

    private Jogo novoJogo(int estoque) {
        return new Jogo("God of War Ragnarök", "PlayStation 5", new BigDecimal("299.90"), estoque);
    }

    @Test
    @DisplayName("Pedido válido deve ficar confirmado, calcular o total e baixar o estoque")
    void pedidoValidoDeveSerConfirmado() {
        // Arrange
        var jogo = novoJogo(3);

        // Act
        var pedido = new Pedido(cliente, jogo, 2);

        // Assert
        assertEquals(StatusPedido.CONFIRMADO, pedido.getStatus());
        assertEquals(new BigDecimal("599.80"), pedido.getValorTotal());
        assertEquals(1, jogo.getEstoque());
    }

    @Test
    @DisplayName("Não deve criar pedido sem estoque suficiente")
    void naoDeveCriarPedidoSemEstoque() {
        // Arrange
        var jogo = novoJogo(1);

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> new Pedido(cliente, jogo, 5));
        assertEquals(1, jogo.getEstoque());
    }

    @Test
    @DisplayName("Não deve criar pedido com quantidade zero")
    void naoDeveCriarPedidoComQuantidadeZero() {
        var jogo = novoJogo(3);

        assertThrows(IllegalArgumentException.class, () -> new Pedido(cliente, jogo, 0));
    }

    @Test
    @DisplayName("Cancelar pedido deve devolver os jogos ao estoque")
    void cancelarDeveDevolverEstoque() {
        // Arrange
        var jogo = novoJogo(3);
        var pedido = new Pedido(cliente, jogo, 2);

        // Act
        pedido.cancelar();

        // Assert
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        assertEquals(3, jogo.getEstoque());
    }

    @Test
    @DisplayName("Não deve cancelar um pedido que já foi cancelado")
    void naoDeveCancelarDuasVezes() {
        // Arrange
        var jogo = novoJogo(3);
        var pedido = new Pedido(cliente, jogo, 2);
        pedido.cancelar();

        // Act + Assert
        assertThrows(IllegalStateException.class, pedido::cancelar);
        assertEquals(3, jogo.getEstoque(), "O estoque não pode ser devolvido duas vezes");
    }
}