package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar período letivo válido")
    void deveCriarPeriodoLetivoValido() {
        // Arrange
        var ano = 2026;
        var semestre = Semestre.SEGUNDO;

        // Act
        var periodo = new PeriodoLetivo(ano, semestre);

        // Assert
        assertEquals(ano, periodo.getAno());
        assertEquals(semestre, periodo.getSemestre());
    }

    @Test
    @DisplayName("Deve considerar períodos diferentes quando possuem dados diferentes")
    void deveConsiderarPeriodosDiferentes() {
        // Arrange
        var periodo1 = new PeriodoLetivo( 2026, Semestre.SEGUNDO );
        var periodo2 = new PeriodoLetivo( 2026, Semestre.PRIMEIRO );

        // Act
        var diferentes = periodo1.equals(periodo2);

        // Assert
        assertFalse(diferentes); }
}
