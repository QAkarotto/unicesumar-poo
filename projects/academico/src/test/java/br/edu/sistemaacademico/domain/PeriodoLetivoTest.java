package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve armazenar e exibir ano e semestre")
    void deveArmazenarAnoESemestre() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
        assertEquals("2026/1", periodo.toString());
    }

    @Test
    @DisplayName("Deve rejeitar ano inválido")
    void deveRejeitarAnoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO));
    }

    @Test
    @DisplayName("Deve exigir semestre")
    void deveExigirSemestre() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }

    @Test
    @DisplayName("A igualdade deve considerar ano e semestre")
    void deveCompararAnoESemestre() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var igual = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var outroAno = new PeriodoLetivo(2025, Semestre.PRIMEIRO);
        var outroSemestre = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertTrue(periodo.equals(periodo));
        assertEquals(periodo, igual);
        assertEquals(periodo.hashCode(), igual.hashCode());
        assertNotEquals(periodo, outroAno);
        assertNotEquals(periodo, outroSemestre);
        assertNotEquals(periodo, null);
        assertNotEquals(periodo, "2026/1");
    }
}
