package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void alunoReprovadoDevePoderCursarDisciplinaNovamente() {

        // Arrange
        var periodo1 = new PeriodoLetivo(
                2025,
                Semestre.SEGUNDO
        );

        var periodo2 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var turma1 = new Turma(
                "ESOFT4S-NA",
                periodo1
        );

        var turma2 = new Turma(
                "ESOFT4S-NB",
                periodo2
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var oferta1 = turma1.ofertarDisciplina(poo);
        var oferta2 = turma2.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        // Act
        var primeiraMatricula = oferta1.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        var segundaMatricula = oferta2.matricular(aluno);

        // Assert
        assertNotNull(segundaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void alunoAprovadoNaoDevePoderCursarDisciplinaNovamente() {

        // Arrange
        var periodo1 = new PeriodoLetivo(
                2025,
                Semestre.SEGUNDO
        );

        var periodo2 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var turma1 = new Turma(
                "ESOFT4S-NA",
                periodo1
        );

        var turma2 = new Turma(
                "ESOFT4S-NB",
                periodo2
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var oferta1 = turma1.ofertarDisciplina(poo);
        var oferta2 = turma2.ofertarDisciplina(poo);

        var aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        var matricula = oferta1.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta2.matricular(aluno)
        );
    }

    @Test
    void deveIdentificarAprovacaoNaDisciplina() {

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
        assertTrue(
                aluno.possuiAprovacaoNaDisciplina(poo)
        );
    }

    @Test
    void alunoSemAprovacaoNaoDeveSerConsideradoAprovado() {

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

        // Assert
        assertFalse(
                aluno.possuiAprovacaoNaDisciplina(poo)
        );
    }
}