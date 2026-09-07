package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {

        int ano = 2026;
        Semestre semestre = Semestre.PRIMEIRO;

        PeriodoLetivo periodo = new PeriodoLetivo(ano, semestre);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void deveRejeitarAnoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
    }

    @Test
    void deveRejeitarSemestreNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    void deveCompararPeriodosIguais() {
        PeriodoLetivo periodo1 =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(periodo1, periodo2);
        assertEquals(periodo1.hashCode(), periodo2.hashCode());
    }

    @Test
    void deveGerarTextoDoPeriodo() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals("2026 - PRIMEIRO", periodo.toString());
    }
}