package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Período letivo")
class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar um período letivo válido")
    void deveCriarPeriodoLetivoValido() {
        // Arrange / Act
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        // Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    @DisplayName("Deve recusar ano zerado ou negativo")
    void deveRecusarAnoInvalido() {
        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );

        assertEquals("O ano deve ser positivo.", erro.getMessage());
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO)
        );
    }

    @Test
    @DisplayName("Deve recusar semestre nulo")
    void deveRecusarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Deve considerar iguais dois períodos com mesmo ano e semestre")
    void deveCompararPeriodosPorAnoESemestre() {
        // Arrange
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var igual = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroSemestre = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var outroAno = new PeriodoLetivo(2025, Semestre.SEGUNDO);

        // Assert
        assertEquals(periodo, igual);
        assertEquals(periodo.hashCode(), igual.hashCode());
        assertNotEquals(periodo, outroSemestre);
        assertNotEquals(periodo, outroAno);
        assertTrue(periodo.equals(periodo));
        assertFalse(periodo.equals("2026/2"));
        assertFalse(periodo.equals(null));
    }

    @Test
    @DisplayName("Deve apresentar o período no formato ano/semestre")
    void deveApresentarRepresentacaoTextual() {
        assertEquals("2026/2", new PeriodoLetivo(2026, Semestre.SEGUNDO).toString());
        assertEquals("2025/1", new PeriodoLetivo(2025, Semestre.PRIMEIRO).toString());
    }

    @Test
    @DisplayName("Deve aceitar apenas os semestres previstos pelo enum")
    void deveAceitarApenasSemestresPrevistos() {
        assertEquals(2, Semestre.values().length);
        assertEquals(1, Semestre.PRIMEIRO.getNumero());
        assertEquals(2, Semestre.SEGUNDO.getNumero());
        assertEquals(Semestre.PRIMEIRO, Semestre.valueOf("PRIMEIRO"));
    }
}
