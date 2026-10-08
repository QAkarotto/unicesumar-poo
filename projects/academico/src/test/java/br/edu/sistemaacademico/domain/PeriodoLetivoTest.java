package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar período letivo válido")
    void deveCriarPeriodoLetivo() {
        //Arrange Act
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        //Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    @DisplayName("Deve impedir criação de período letivo com ano inválido ou semestre ausente")
    void deveRejeitarAnoOuSemestreInvalidos() {
        // Act e Assert
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO));
    }
}
