package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    @Test
    void devePermitirMatricularAluno() {

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

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertNotNull(matricula);
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOferta());
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {

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

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );
    }

    @Test
    void deveRegistrarMatriculaNoHistoricoDoAluno() {

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

        // Act
        oferta.matricular(aluno);

        // Assert
        assertEquals(1, aluno.getMatriculas().size());
    }
}