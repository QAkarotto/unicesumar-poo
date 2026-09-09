package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve recusar ano inválido e semestre nulo")
    void deveRecusarAnoInvalidoESemestreNulo() {
        // Arrange
        var semestre = Semestre.PRIMEIRO;

        // Act
        var anoInvalido = assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, semestre)
        );
        var semSemestre = assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );

        // Assert
        assertEquals("O ano deve ser positivo.", anoInvalido.getMessage());
        assertEquals("O semestre é obrigatório.", semSemestre.getMessage());
    }

    @Test
    @DisplayName("Deve considerar iguais os períodos com mesmo ano e semestre")
    void deveCompararPeriodosPeloAnoESemestre() {
        var primeiroDe2026 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var mesmoPeriodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var segundoDe2026 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var primeiroDe2025 = new PeriodoLetivo(2025, Semestre.PRIMEIRO);

        assertEquals(primeiroDe2026, mesmoPeriodo);
        assertEquals(primeiroDe2026.hashCode(), mesmoPeriodo.hashCode());
        assertNotEquals(primeiroDe2026, segundoDe2026);
        assertNotEquals(primeiroDe2026, primeiroDe2025);
        assertNotEquals(primeiroDe2026, "2026/1");
    }

    @Test
    @DisplayName("Deve apresentar o período no formato ano/semestre")
    void deveApresentarPeriodoNoFormatoAnoSemestre() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals(2, periodo.getSemestre().getNumero());
        assertEquals("2026/2", periodo.toString());
    }
}
