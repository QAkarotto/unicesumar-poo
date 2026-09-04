package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    @Test
    void devePermitirConcluirMatriculaComoAprovado() {

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

        var oferta = turma.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Assert
        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void devePermitirConcluirMatriculaComoReprovado() {

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

        var oferta = turma.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        // Assert
        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void naoDevePermitirConcluirMatriculaDuasVezes() {

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

        var oferta = turma.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        var matricula = oferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(
                        ResultadoAcademico.REPROVADO
                )
        );
    }

    @Test
    void naoDevePermitirResultadoNulo() {

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

        var oferta = turma.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        var matricula = oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }
}