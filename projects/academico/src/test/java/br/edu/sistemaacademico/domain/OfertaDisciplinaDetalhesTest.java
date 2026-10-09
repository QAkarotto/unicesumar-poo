package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfertaDisciplinaDetalhesTest {

    @Test
    @DisplayName("Deve expor turma e disciplina e formatar oferta")
    void deveExporDadosDaOferta() {
        var turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var disciplina = new Disciplina("POO", "Programação", 80);
        var oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals("POO - T1 - 2026/1", oferta.toString());
        assertThrows(UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear());
    }
}
