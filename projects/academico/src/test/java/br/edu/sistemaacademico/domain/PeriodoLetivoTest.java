package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertEquals(2026, periodo.getAno());
        assertEquals(
                Semestre.PRIMEIRO,
                periodo.getSemestre()
        );
    }

    @Test
    void naoDeveCriarPeriodoComAnoInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        0,
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
    }

    @Test
    void deveRepresentarPrimeiroSemestreComo1() {

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertEquals("2026/1", periodo.toString());
    }

    @Test
    void deveRepresentarSegundoSemestreComo2() {

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        assertEquals("2026/2", periodo.toString());
    }
}