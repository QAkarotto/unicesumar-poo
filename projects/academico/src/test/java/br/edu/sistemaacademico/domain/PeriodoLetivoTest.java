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
    }

    @Test
    void periodosIguaisDevemSerIguais() {
        PeriodoLetivo periodo1 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertEquals(periodo1, periodo2);
    }

    @Test
    void periodosComAnosDiferentesNaoDevemSerIguais() {
        PeriodoLetivo periodo1 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(
                        2027,
                        Semestre.PRIMEIRO
                );

        assertNotEquals(periodo1, periodo2);
    }

    @Test
    void periodosComSemestresDiferentesNaoDevemSerIguais() {
        PeriodoLetivo periodo1 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        assertNotEquals(periodo1, periodo2);
    }

    @Test
    void periodoNaoDeveSerIgualANulo() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertNotEquals(periodo, null);
    }

    @Test
    void periodoNaoDeveSerIgualAOutroTipoDeObjeto() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertNotEquals(periodo, "2026/1");
    }

    @Test
    void periodosIguaisDevemPossuirMesmoHashCode() {
        PeriodoLetivo periodo1 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertEquals(
                periodo1.hashCode(),
                periodo2.hashCode()
        );
    }

    @Test
    void deveRetornarToStringCorreto() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        assertEquals(
                "2026/1",
                periodo.toString()
        );
    }
}