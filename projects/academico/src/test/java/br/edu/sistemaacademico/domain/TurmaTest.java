package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

    @Test
    void deveCriarTurmaComDadosValidos() {
        // Arrange & Act
        var turma = new Turma("T1", periodo);

        // Assert
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
        // Arrange
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        // Act
        var oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
        assertTrue(turma.getOfertas().contains(oferta));
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        // Arrange
        var turma = new Turma("T1", periodo);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    void naoDevePermitirOfertarMesmaDisciplinaDuasVezesNaMesmaTurma() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void devePermitirOfertarDisciplinasDiferentesNaMesmaTurma() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var poo = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD01", "Banco de Dados", 60);

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);

        // Assert
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void getOfertasNaoDeveExporColecaoInternaParaModificacao() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var oferta = turma.ofertarDisciplina(disciplina);

        // Act
        var ofertas = turma.getOfertas();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> ofertas.add(oferta));
    }

    @Test
    void toStringDeveConterCodigoEPeriodoLetivo() {
        // Arrange
        var turma = new Turma("T1", periodo);

        // Act
        var texto = turma.toString();

        // Assert
        assertTrue(texto.contains("T1"));
        assertTrue(texto.contains("2026/1"));
    }
}
