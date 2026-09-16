package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar período letivo com ano e semestre corretos")
    void deveCriarPeriodoLetivoCorretamente() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }
}