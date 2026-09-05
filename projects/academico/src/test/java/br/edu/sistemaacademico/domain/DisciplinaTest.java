package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        // Arrange & Act
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        // Assert
        assertEquals("POO01", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Programação Orientada a Objetos", 80));
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("   ", "Programação Orientada a Objetos", 80));
    }

    @Test
    void naoDeveCriarDisciplinaComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO01", null, 80));
    }

    @Test
    void naoDeveCriarDisciplinaComNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO01", "  ", 80));
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO01", "Programação Orientada a Objetos", 0));
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO01", "Programação Orientada a Objetos", -10));
    }

    @Test
    void toStringDeveConterNomeCodigoECargaHoraria() {
        // Arrange
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        // Act
        var texto = disciplina.toString();

        // Assert
        assertTrue(texto.contains("Programação Orientada a Objetos"));
        assertTrue(texto.contains("POO01"));
        assertTrue(texto.contains("80"));
    }
}
