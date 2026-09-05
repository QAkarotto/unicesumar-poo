package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Deve criar período letivo com dados válidos")
    void deveCriarPeriodoLetivo() {

        // Cria um período letivo utilizando ano e semestre válidos
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        // Confirma que o ano foi armazenado corretamente
        assertEquals(2026, periodo.getAno());

        // Confirma que o semestre foi armazenado corretamente
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    @DisplayName("Não deve permitir ano zero")
    void naoDevePermitirAnoZero() {

        // Tenta criar um período utilizando ano zero
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.SEGUNDO)
        );
    }

    @Test
    @DisplayName("Não deve permitir ano negativo")
    void naoDevePermitirAnoNegativo() {

        // Tenta criar um período utilizando um ano negativo
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.SEGUNDO)
        );
    }

    @Test
    @DisplayName("Não deve permitir semestre nulo")
    void naoDevePermitirSemestreNulo() {

        // Tenta criar um período sem informar o semestre
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Períodos com mesmo ano e semestre devem ser iguais")
    void deveCompararPeriodos() {

        // Cria dois períodos com os mesmos dados
        var periodo1 = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        var periodo2 = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        // Confirma que os dois períodos são considerados iguais
        assertEquals(periodo1, periodo2);

        // Objetos considerados iguais devem possuir o mesmo hashCode
        assertEquals(periodo1.hashCode(), periodo2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar texto do período letivo")
    void deveGerarToString() {

        // Cria um período letivo para testar sua representação em texto
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        // Confirma o formato utilizado pelo método toString()
        assertEquals(
                "2026/" + Semestre.SEGUNDO.getNumero(),
                periodo.toString()
        );
    }
}