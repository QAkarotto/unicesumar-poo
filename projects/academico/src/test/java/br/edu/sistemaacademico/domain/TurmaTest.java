package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void deveOfertarDisciplinaNaTurma() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        // Act
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertTrue(turma.getOfertas().contains(oferta));
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezes() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        Disciplina disciplina =
                new Disciplina(
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

        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );

        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void listaDeOfertasNaoDevePermitirAlteracaoExterna() {
        // Arrange
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        Disciplina disciplina =
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                );

        turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );

        assertEquals(1, turma.getOfertas().size());
    }
}