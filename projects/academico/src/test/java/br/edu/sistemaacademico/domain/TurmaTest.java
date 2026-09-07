package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void deveCriarTurmaValida() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Act
        Turma turma = new Turma("T01", periodo);

        // Assert
        assertEquals("T01", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
    }

    @Test
    void deveRejeitarCodigoVazio() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("", periodo)
        );
    }

    @Test
    void deveRejeitarPeriodoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("T01", null)
        );
    }

    @Test
    void deveIniciarSemOfertas() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveOfertarDisciplina() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Assert
        assertNotNull(oferta);
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    void deveRejeitarDisciplinaNula() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDevePermitirDisciplinaDuplicadaNaTurma() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );
    }

    @Test
    void listaDeOfertasDeveSerSomenteParaLeitura() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        List<OfertaDisciplina> ofertas = turma.getOfertas();

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                ofertas::clear
        );
    }

    @Test
    void deveGerarTextoDaTurma() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        Disciplina disciplina =
                new Disciplina("POO", "Programação", 80);

        turma.ofertarDisciplina(disciplina);

        // Act
        String resultado = turma.toString();

        // Assert
        assertTrue(resultado.contains("T01"));
        assertTrue(resultado.contains("2026"));
        assertTrue(resultado.contains("Programação"));
    }
}