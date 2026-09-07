package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar periodo letivo com ano e semestre validos")
    void deveCriarPeriodoLetivoComSucesso() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
        assertNotNull(periodo.toString());
    }

    @Test
    @DisplayName("Deve lancar excecao com ano ou semestre invalidos")
    void deveValidarAnoESemestre() {
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
    }

    @Test
    @DisplayName("Deve validar equals e hashCode para periodo letivo")
    void deveValidarEqualsEHashCode() {
        PeriodoLetivo p1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo p2 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo pDiferente = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, pDiferente);
        assertNotEquals(p1, "OutroObjeto");
    }
}