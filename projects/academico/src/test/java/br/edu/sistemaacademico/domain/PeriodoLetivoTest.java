package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void construtorComDadosValidos_deveCriarPeriodoCorretamente() {
        // Arrange / Act
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void construtorComAnoInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO));
    }

    @Test
    void construtorComSemestreNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }
}