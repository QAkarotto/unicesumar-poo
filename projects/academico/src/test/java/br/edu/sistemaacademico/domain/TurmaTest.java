package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

    @Test
    void deveCriarTurmaComDadosValidos() {
        var turma = new Turma("T1", periodo);

        assertEquals("T1", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
    }

    @Test
    void naoDeveCriarTurmaComCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma(null, periodo));
    }

    @Test
    void naoDeveCriarTurmaComCodigoVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("   ", periodo));
    }

    @Test
    void naoDeveCriarTurmaComPeriodoLetivoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("T1", null));
    }

    @Test
    void deveOfertarDisciplinaNaTurma() {
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        var oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
        assertTrue(turma.getOfertas().contains(oferta));
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        var turma = new Turma("T1", periodo);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    void naoDevePermitirOfertarMesmaDisciplinaDuasVezesNaMesmaTurma() {
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void devePermitirOfertarDisciplinasDiferentesNaMesmaTurma() {
        var turma = new Turma("T1", periodo);
        var poo = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD01", "Banco de Dados", 60);

        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);

        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void getOfertasNaoDeveExporColecaoInternaParaModificacao() {
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var oferta = turma.ofertarDisciplina(disciplina);

        var ofertas = turma.getOfertas();

        assertThrows(UnsupportedOperationException.class, () -> ofertas.add(oferta));
    }

    @Test
    void toStringDeveConterCodigoEPeriodoLetivo() {
        var turma = new Turma("T1", periodo);

        var texto = turma.toString();

        assertTrue(texto.contains("T1"));
        assertTrue(texto.contains("2026/1"));
    }
}