package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void deveRejeitarAnoZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
    }

    @Test
    void deveRejeitarAnoNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-1, Semestre.PRIMEIRO)
        );
    }

    @Test
    void deveRejeitarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    void periodosIguaisDevemSerIguais() {
        PeriodoLetivo primeiro = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo segundo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
    }

    @Test
    void periodosDiferentesNaoDevemSerIguais() {
        PeriodoLetivo primeiro = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo segundo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        assertNotEquals(primeiro, segundo);
    }

    @Test
    void deveRepresentarPeriodoComoTexto() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        assertEquals("2026/2", periodo.toString());
    }
}