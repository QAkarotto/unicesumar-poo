package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar um período letivo válido")
    void deveCriarPeriodoLetivoValido() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    @DisplayName("Deve rejeitar ano menor ou igual a zero")
    void deveRejeitarAnoInvalido() {
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
    @DisplayName("Deve rejeitar semestre nulo")
    void deveRejeitarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Períodos letivos com mesmo ano e semestre devem ser iguais")
    void deveConsiderarPeriodosIguaisPeloAnoESemestre() {
        var periodo1 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var periodo2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var periodoAnoDiferente = new PeriodoLetivo(2027, Semestre.SEGUNDO);
        var periodoSemestreDiferente = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(periodo1, periodo1);
        assertEquals(periodo1, periodo2);
        assertEquals(periodo1.hashCode(), periodo2.hashCode());
        assertNotEquals(periodo1, periodoAnoDiferente);
        assertNotEquals(periodo1, periodoSemestreDiferente);
        assertNotEquals(periodo1, null);
        assertFalse(periodo1.equals("2026/2"));
    }

    @Test
    @DisplayName("O toString deve conter o ano e o número do semestre")
    void toStringDeveConterAnoENumeroDoSemestre() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals("2026/2", periodo.toString());
    }

    @Test
    @DisplayName("O número do primeiro semestre deve ser 1")
    void numeroDoPrimeiroSemestreDeveSerUm() {
        assertEquals(1, Semestre.PRIMEIRO.getNumero());
    }
}
