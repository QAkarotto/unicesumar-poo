package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    @Test
    void deveOfertarDisciplinaNaTurma() {
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezesNaTurma() {
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void naoDeveCriarTurmaComCodigoVazio() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(" ", periodo));
    }

    @Test
    void naoDeveCriarTurmaComPeriodoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("T2", null));
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        Turma turma = new Turma("T3", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    void devePermitirConsultarCodigoEPeriodoDaTurma() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("T4", periodo);

        assertEquals("T4", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
    }

    @Test
    void representacaoTextualContemDadosDaTurma() {
        Turma turma = new Turma("T5", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        turma.ofertarDisciplina(new Disciplina("POO01", "Programação", 80));

        String texto = turma.toString();

        assertTrue(texto.contains("T5"));
        assertTrue(texto.contains("1"));
    }
}
