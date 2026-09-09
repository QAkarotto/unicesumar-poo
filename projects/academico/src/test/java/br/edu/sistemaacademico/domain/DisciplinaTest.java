package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisciplinaTest {

    @Test
    @DisplayName("Deve exigir código e nome da disciplina")
    void deveExigirCodigoENome() {
        // Arrange
        var cargaHoraria = 80;

        // Act
        var semCodigo = assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "Programação Orientada a Objetos", cargaHoraria)
        );
        var semNome = assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "   ", cargaHoraria)
        );

        // Assert
        assertEquals("O código da disciplina é obrigatório.", semCodigo.getMessage());
        assertEquals("O nome da disciplina é obrigatório.", semNome.getMessage());
    }

    @Test
    @DisplayName("Deve recusar carga horária zerada ou negativa")
    void deveRecusarCargaHorariaInvalida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", -80)
        );
    }

    @Test
    @DisplayName("Deve identificar a disciplina pelo código")
    void deveIdentificarDisciplinaPeloCodigo() {
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmoCodigo = new Disciplina("POO", "Programação Orientada a Objetos II", 40);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        assertEquals(poo, mesmoCodigo);
        assertEquals(poo.hashCode(), mesmoCodigo.hashCode());
        assertNotEquals(poo, bancoDados);
        assertNotEquals(poo, "POO");
    }

    @Test
    @DisplayName("Deve remover os espaços em volta do código e do nome")
    void deveRemoverEspacosDoCodigoEDoNome() {
        var disciplina = new Disciplina("  POO  ", "  Programação Orientada a Objetos  ", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
        assertEquals("POO - Programação Orientada a Objetos (80h)", disciplina.toString());
    }
}
