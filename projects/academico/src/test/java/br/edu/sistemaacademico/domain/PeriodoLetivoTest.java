
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
        assertEquals("2026/1", periodo.toString());
    }

    @Test
    void deveRejeitarAnoInvalido() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );

        assertEquals("O ano deve ser positivo.", erro.getMessage());
    }

    @Test
    void deveRejeitarSemestreNulo() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );

        assertEquals("O semestre é obrigatório.", erro.getMessage());
    }

    @Test
    void deveCompararPeriodosIguais() {
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
        assertTrue(primeiro.equals(primeiro));
    }

    @Test
    void deveIdentificarPeriodosDiferentes() {
        PeriodoLetivo primeiro = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo segundo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        PeriodoLetivo terceiro = new PeriodoLetivo(
                2025,
                Semestre.PRIMEIRO
        );

        assertNotEquals(primeiro, segundo);
        assertNotEquals(primeiro, terceiro);
        assertNotEquals(primeiro, null);
        assertNotEquals(primeiro, "2026/1");
    }

    @Test
    void deveRejeitarAnoNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO)
        );
    }
}
