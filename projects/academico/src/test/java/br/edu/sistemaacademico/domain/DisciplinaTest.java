package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Disciplina")
class DisciplinaTest {

    @Test
    @DisplayName("Deve criar uma disciplina válida removendo espaços das extremidades")
    void deveCriarDisciplinaValida() {
        // Arrange / Act
        var disciplina = new Disciplina("  POO  ", "  Programação Orientada a Objetos  ", 80);

        // Assert
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve recusar código nulo ou em branco")
    void deveRecusarCodigoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "Banco de Dados", 80)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("   ", "Banco de Dados", 80)
        );
    }

    @Test
    @DisplayName("Deve recusar nome nulo ou em branco")
    void deveRecusarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("BD", null, 80)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("BD", "  ", 80)
        );
    }

    @Test
    @DisplayName("Deve recusar carga horária zerada ou negativa")
    void deveRecusarCargaHorariaInvalida() {
        // Act
        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("SO", "Sistemas Operacionais", 0)
        );

        // Assert
        assertEquals("A carga horária deve ser positiva.", erro.getMessage());
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("SO", "Sistemas Operacionais", -40)
        );
    }

    @Test
    @DisplayName("Deve considerar iguais duas disciplinas com o mesmo código")
    void deveCompararDisciplinasPeloCodigo() {
        // Arrange
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmoCodigo = new Disciplina("POO", "Nome diferente", 40);
        var outra = new Disciplina("BD", "Banco de Dados", 80);

        // Assert
        assertEquals(poo, mesmoCodigo);
        assertEquals(poo.hashCode(), mesmoCodigo.hashCode());
        assertNotEquals(poo, outra);
        assertTrue(poo.equals(poo));
        assertFalse(poo.equals("POO"));
        assertFalse(poo.equals(null));
    }

    @Test
    @DisplayName("Deve apresentar código, nome e carga horária no toString")
    void deveApresentarRepresentacaoTextual() {
        var disciplina = new Disciplina("ER", "Engenharia de Requisitos", 60);

        assertEquals("ER - Engenharia de Requisitos (60h)", disciplina.toString());
    }
}
