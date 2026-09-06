package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void deveOfertarUmaDisciplina() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("ESOFT4S-NB", periodo);
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Act
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertNotNull(oferta);
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
    }

    @Test
    void naoDevePermitirDisciplinaDuplicada() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("ESOFT4S-NB", periodo);
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );

        // Goku não conseguiria usar duas ofertas iguais como se fossem diferentes.
    }

    @Test
    void naoDevePermitirDisciplinaNula() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("ESOFT4S-NB", periodo);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDeveCriarTurmaComCodigoVazio() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("", periodo)
        );
    }

    @Test
    void naoDeveCriarTurmaSemPeriodoLetivo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NB", null)
        );
    }
}