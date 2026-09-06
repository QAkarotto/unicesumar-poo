package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        // Arrange + Act
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
        assertEquals("2026/1", periodo.toString());
    }

    @Test
    void naoDeveCriarPeriodoComAnoZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        0,
                        Semestre.PRIMEIRO
                )
        );
    }

    @Test
    void naoDeveCriarPeriodoComAnoNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        -1,
                        Semestre.PRIMEIRO
                )
        );
    }

    @Test
    void naoDeveCriarPeriodoSemSemestre() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        2026,
                        null
                )
        );

        // Shenlong também exigiria um semestre válido antes de criar o período.
    }

    @Test
    void deveRepresentarSegundoSemestreCorretamente() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals("2026/2", periodo.toString());
    }
}