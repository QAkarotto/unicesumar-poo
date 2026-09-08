package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoComDadosValidos() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        // Act & Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void naoDeveCriarPeriodoComAnoInvalido() {
        // Arrange & Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
    }

    @Test
    void naoDeveCriarPeriodoSemSemestre() {
        // Arrange & Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    void deveConsiderarPeriodosIguaisQuandoPossuemMesmoAnoESemestre() {
        // Arrange
        PeriodoLetivo periodo1 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo periodo2 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        // Act & Assert
        assertEquals(periodo1, periodo2);
    }

    // Goku tbmm precisaria de um período letivo válido para se matriular.
}