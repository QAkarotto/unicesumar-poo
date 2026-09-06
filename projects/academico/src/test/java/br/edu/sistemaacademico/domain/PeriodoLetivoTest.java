package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar um periodo letivo valido")
    void deveCriarUmPeriodoLetivoValido() {
        // Arrange / Act
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        // Assert
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals(2, periodo.getSemestre().getNumero());
        assertEquals("2026/2", periodo.toString());
    }

    @Test
    @DisplayName("Nao deve criar periodo letivo com ano zerado")
    void naoDeveCriarPeriodoLetivoComAnoZerado() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PeriodoLetivo(0, Semestre.PRIMEIRO);
        });
    }

    @Test
    @DisplayName("Nao deve criar periodo letivo sem semestre")
    void naoDeveCriarPeriodoLetivoSemSemestre() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PeriodoLetivo(2026, null);
        });
    }

    @Test
    @DisplayName("Dois periodos com mesmo ano e semestre sao iguais")
    void doisPeriodosComMesmoAnoESemestreSaoIguais() {
        // Arrange
        PeriodoLetivo primeiroSemestre = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo igual = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo segundoSemestre = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        PeriodoLetivo outroAno = new PeriodoLetivo(2025, Semestre.PRIMEIRO);

        // Assert
        assertEquals(primeiroSemestre, igual);
        assertEquals(primeiroSemestre.hashCode(), igual.hashCode());
        assertFalse(primeiroSemestre.equals(segundoSemestre));
        assertFalse(primeiroSemestre.equals(outroAno));
        assertFalse(primeiroSemestre.equals("2026/1"));
        assertEquals(1, Semestre.PRIMEIRO.getNumero());
    }
}
