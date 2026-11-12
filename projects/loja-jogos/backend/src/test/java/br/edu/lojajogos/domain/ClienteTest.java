package br.edu.lojajogos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteTest {

    @Test
    @DisplayName("Não deve cadastrar cliente com e-mail inválido")
    void naoDeveCadastrarEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("Maria", "maria.email.com"));
    }

    @Test
    @DisplayName("E-mail deve ser guardado em minúsculas para evitar duplicidade")
    void emailDeveSerNormalizado() {
        // Act
        var cliente = new Cliente("Maria", "  Maria@Email.COM ");

        // Assert
        assertEquals("maria@email.com", cliente.getEmail());
    }
}