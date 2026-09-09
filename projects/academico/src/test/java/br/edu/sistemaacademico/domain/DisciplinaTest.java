package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaTest {

    @Test
    @DisplayName("Código e nome da disciplina são obrigatórios")
    void deveExigirCodigoENome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("", "Banco de Dados", 60)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("BD", "   ", 60)
        );
    }

    @Test
    @DisplayName("Carga horária precisa ser positiva")
    void deveExigirCargaHorariaPositiva() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("BD", "Banco de Dados", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("BD", "Banco de Dados", -60)
        );
    }

    @Test
    @DisplayName("Espaços em volta do código e do nome são descartados")
    void deveLimparEspacos() {
        var disciplina = new Disciplina("  BD ", " Banco de Dados ", 60);

        assertEquals("BD", disciplina.getCodigo());
        assertEquals("Banco de Dados", disciplina.getNome());
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Disciplinas são comparadas pelo código, não pelo nome")
    void disciplinaEIdentificadaPeloCodigo() {
        // Arrange
        var disciplina = new Disciplina("BD", "Banco de Dados", 60);
        var mesmoCodigo = new Disciplina("BD", "Banco de Dados I", 80);
        var outroCodigo = new Disciplina("BD2", "Banco de Dados", 60);

        // Assert
        assertTrue(disciplina.equals(mesmoCodigo));
        assertEquals(disciplina.hashCode(), mesmoCodigo.hashCode());
        assertFalse(disciplina.equals(outroCodigo));
        assertFalse(disciplina.equals(null));
    }

    @Test
    @DisplayName("toString da disciplina traz código, nome e carga horária")
    void toStringDescreveADisciplina() {
        var disciplina = new Disciplina("BD", "Banco de Dados", 60);

        assertEquals("BD - Banco de Dados (60h)", disciplina.toString());
    }
}
