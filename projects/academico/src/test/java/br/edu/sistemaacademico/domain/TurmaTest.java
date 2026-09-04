package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void devePermitirOfertarVariasDisciplinas() {

        // Arrange
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var turma = new Turma(
                "ESOFT4S-NB",
                periodo
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void naoDevePermitirDisciplinaDuplicada() {

        // Arrange
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var turma = new Turma(
                "ESOFT4S-NB",
                periodo
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        turma.ofertarDisciplina(poo);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }

    @Test
    void deveCriarOfertaComDisciplinaCorreta() {

        // Arrange
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var turma = new Turma(
                "ESOFT4S-NB",
                periodo
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Act
        var oferta = turma.ofertarDisciplina(poo);

        // Assert
        assertEquals(poo, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }
}