package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar período letivo válido")
    void deveCriarPeriodoValido() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals(2, periodo.getSemestre().getNumero());
    }

    @Test
    @DisplayName("Não deve aceitar ano menor ou igual a zero")
    void naoDeveAceitarAnoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO)
        );
    }

    @Test
    @DisplayName("Não deve aceitar semestre nulo")
    void naoDeveAceitarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Períodos com mesmo ano e semestre devem ser iguais")
    void deveCompararPeriodosPorAnoESemestre() {
        var primeiro = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var mesmoPrimeiro = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var segundo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroAno = new PeriodoLetivo(2025, Semestre.PRIMEIRO);

        assertEquals(primeiro, mesmoPrimeiro);
        assertEquals(primeiro.hashCode(), mesmoPrimeiro.hashCode());
        assertNotEquals(primeiro, segundo);
        assertNotEquals(primeiro, outroAno);
        assertTrue(primeiro.equals(primeiro));
        assertFalse(primeiro.equals("2026/1"));
    }

    @Test
    @DisplayName("toString deve seguir o formato ano/semestre")
    void toStringDeveSeguirFormato() {
        assertEquals("2026/1", new PeriodoLetivo(2026, Semestre.PRIMEIRO).toString());
        assertEquals("2026/2", new PeriodoLetivo(2026, Semestre.SEGUNDO).toString());
    }
}
