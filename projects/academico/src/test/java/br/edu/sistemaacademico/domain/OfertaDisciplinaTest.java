package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfertaDisciplinaTest {

    @Test
    @DisplayName("Deve registrar a mesma matrícula no aluno e na oferta")
    void deveRegistrarMatriculaNoAlunoENaOferta() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act
        var matricula = oferta.matricular("MAT-001", aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertSame(matricula, oferta.getMatriculas().get(0));
        assertSame(matricula, aluno.getMatriculas().get(0));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        var aluno = new Aluno("RA002", "Alexandre Gaia", "alexandre@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 40)
        );
        oferta.matricular("MATRICULA-001", aluno);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MATRICULA-002", aluno)
        );
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }
}