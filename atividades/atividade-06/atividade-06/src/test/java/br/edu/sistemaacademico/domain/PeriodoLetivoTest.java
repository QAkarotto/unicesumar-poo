package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodoLetivoTest {

    @Test
    
    void testCriarPeriodoValido() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    
    void testCriarComSegundoSemestre() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    
    void testNaoCriarComAnoZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
    }

    @Test
    
    void testNaoCriarComAnoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO));
    }

    @Test
    
    void testNaoCriarComSemestreNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }

    @Test
    
    void testAceitarAnoMinimoValido() {
        PeriodoLetivo periodo = new PeriodoLetivo(1, Semestre.PRIMEIRO);

        assertEquals(1, periodo.getAno());
    }

    @Test
    
    void testAceitarAnoGrande() {
        PeriodoLetivo periodo = new PeriodoLetivo(9999, Semestre.SEGUNDO);

        assertEquals(9999, periodo.getAno());
    }

    @Test
    
    void testGetAno() {
        PeriodoLetivo periodo = new PeriodoLetivo(2025, Semestre.PRIMEIRO);

        assertEquals(2025, periodo.getAno());
    }

    @Test
    
    void testGetSemestre() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    
    void testToStringContemAno() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        String texto = periodo.toString();

        assertTrue(texto.contains("2026"));
    }

    @Test
    
    void testToStringContemSemestre() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        String texto = periodo.toString();

        assertTrue(texto.contains("PRIMEIRO"));
    }

    @Test
    
    void testToStringCompletoMesmos() {
        PeriodoLetivo periodo = new PeriodoLetivo(2027, Semestre.SEGUNDO);

        String texto = periodo.toString();

        assertTrue(texto.contains("2027"));
        assertTrue(texto.contains("SEGUNDO"));
    }

    @Test
    
    void testCriarPeriodo2024Primeiro() {
        PeriodoLetivo periodo = new PeriodoLetivo(2024, Semestre.PRIMEIRO);

        assertEquals(2024, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    
    void testCriarPeriodo2025Segundo() {
        PeriodoLetivo periodo = new PeriodoLetivo(2025, Semestre.SEGUNDO);

        assertEquals(2025, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }
}
