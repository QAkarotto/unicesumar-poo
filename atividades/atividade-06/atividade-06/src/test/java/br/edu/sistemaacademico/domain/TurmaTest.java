package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    @Test
    
    void testOfertarDisciplina() {
        Turma turma = new Turma("TURMA01", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("CALC101", "Cálculo", 90);

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    
    void testNaoOfertarDuasVezes() {
        Turma turma = new Turma("TURMA02", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("PHYS101", "Física", 75);

        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    
    void testNaoCriarComCodigoNulo() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
    }

    @Test
    
    void testNaoCriarComCodigoBranco() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma("   ", periodo));
    }

    @Test
    
    void testNaoCriarComPeriodoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("TURMA03", null));
    }

    @Test
    
    void testNaoOfertarDisciplinaNula() {
        Turma turma = new Turma("TURMA04", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    
    void testGetCodigo() {
        Turma turma = new Turma("TURMA05", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertEquals("TURMA05", turma.getCodigo());
    }

    @Test
    
    void testGetPeriodoLetivo() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        Turma turma = new Turma("TURMA06", periodo);

        assertEquals(periodo, turma.getPeriodoLetivo());
    }

    @Test
    
    void testGetOfertasVazia() {
        Turma turma = new Turma("TURMA07", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    
    void testGetOfertasImutavel() {
        Turma turma = new Turma("TURMA08", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("MATH101", "Matemática", 80);
        turma.ofertarDisciplina(disciplina);

        var ofertas = turma.getOfertas();

        assertThrows(UnsupportedOperationException.class, () -> ofertas.add(null));
    }

    @Test
    
    void testToStringContemDados() {
        Turma turma = new Turma("TURMA09", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d1 = new Disciplina("ENG101", "Engenharia", 85);
        turma.ofertarDisciplina(d1);

        String texto = turma.toString();

        assertTrue(texto.contains("TURMA09"));
        assertTrue(texto.contains("1"));
    }

    @Test
    
    void testMultiplasOfertas() {
        Turma turma = new Turma("TURMA10", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d1 = new Disciplina("LANG101", "Linguagem", 60);
        Disciplina d2 = new Disciplina("ART101", "Artes", 50);
        Disciplina d3 = new Disciplina("MUS101", "Música", 55);

        turma.ofertarDisciplina(d1);
        turma.ofertarDisciplina(d2);
        turma.ofertarDisciplina(d3);

        assertEquals(3, turma.getOfertas().size());
    }

    @Test
    
    void testOfertarMesmaDisciplinaEmTurmasDiferentes() {
        Turma turma1 = new Turma("TURMA11", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Turma turma2 = new Turma("TURMA12", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        Disciplina disciplina = new Disciplina("COMP101", "Computação", 100);

        OfertaDisciplina oferta1 = turma1.ofertarDisciplina(disciplina);
        OfertaDisciplina oferta2 = turma2.ofertarDisciplina(disciplina);

        assertEquals(1, turma1.getOfertas().size());
        assertEquals(1, turma2.getOfertas().size());
        assertEquals(oferta1.getDisciplina(), oferta2.getDisciplina());
    }

    @Test
    
    void testCodigoComCaracteresEspeciais() {
        Turma turma = new Turma("T-2026-01", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertEquals("T-2026-01", turma.getCodigo());
    }

    @Test
    
    void testGetTotalDisciplinasOfertadas() {
        Turma turma = new Turma("TURMA-TOTAL", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d1 = new Disciplina("D001", "Disciplina 1", 40);
        Disciplina d2 = new Disciplina("D002", "Disciplina 2", 45);
        Disciplina d3 = new Disciplina("D003", "Disciplina 3", 50);

        assertEquals(0, turma.getTotalDisciplinasOfertadas());
        turma.ofertarDisciplina(d1);
        assertEquals(1, turma.getTotalDisciplinasOfertadas());
        turma.ofertarDisciplina(d2);
        turma.ofertarDisciplina(d3);
        assertEquals(3, turma.getTotalDisciplinasOfertadas());
    }
}
