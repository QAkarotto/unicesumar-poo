package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        // Arrange
        int ano = 2026;
        Semestre semestre = Semestre.PRIMEIRO;

        // Act
        PeriodoLetivo periodo = new PeriodoLetivo(ano, semestre);

        // Assert
        assertEquals(ano, periodo.getAno());
        assertEquals(semestre, periodo.getSemestre());
    }

    @Test
    void naoDeveCriarPeriodoLetivoComAnoZero() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
    }

    @Test
    void naoDeveCriarPeriodoLetivoComAnoNegativo() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO));
    }

    @Test
    void naoDeveCriarPeriodoLetivoComSemestreNulo() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }
}
