package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve expor ano e semestre e formatar como ano/semestre")
    void deveFormatarPeriodo() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals(2, periodo.getSemestre().getNumero());
        assertEquals("2026/2", periodo.toString());
    }

    @Test
    @DisplayName("Deve rejeitar ano inválido")
    void deveRejeitarAnoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(1800, Semestre.PRIMEIRO)
        );
    }

    @Test
    @DisplayName("Deve rejeitar semestre nulo")
    void deveRejeitarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Deve comparar períodos por ano e semestre")
    void deveCompararPeriodosPorValor() {
        var primeiro = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var igual = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var outroSemestre = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroAno = new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        assertTrue(primeiro.equals(igual));
        assertEquals(primeiro.hashCode(), igual.hashCode());
        assertFalse(primeiro.equals(outroSemestre));
        assertFalse(primeiro.equals(outroAno));
        assertFalse(primeiro.equals("2026/1"));
    }
}