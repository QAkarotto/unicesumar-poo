package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoComDadosValidos() {
        // Arrange & Act
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void naoDeveCriarPeriodoLetivoComAnoZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
    }

    @Test
    void naoDeveCriarPeriodoLetivoComAnoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO));
    }

    @Test
    void naoDeveCriarPeriodoLetivoComSemestreNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }

    @Test
    void toStringDeveRepresentarPrimeiroSemestreComoBarraUm() {
        // Arrange
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Act & Assert
        assertEquals("2026/1", periodo.toString());
    }

    @Test
    void toStringDeveRepresentarSegundoSemestreComoBarraDois() {
        // Arrange
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        // Act & Assert
        assertEquals("2026/2", periodo.toString());
    }
}
