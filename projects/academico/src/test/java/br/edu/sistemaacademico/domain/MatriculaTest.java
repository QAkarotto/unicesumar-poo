package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    @DisplayName("Deve permitir cancelar matrícula ativa")
    void deveCancelarMatricula() {
        // Arrange
        var aluno = new Aluno("RA001", "Danilo Machado", "danilo@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = turma.getOfertas().get(0).matricular(aluno);

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve permitir trancar matrícula ativa")
    void deveTrancarMatricula() {
        // Arrange
        var aluno = new Aluno("RA002", "Doris Daniella", "doris@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = turma.getOfertas().get(0).matricular(aluno);

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve concluir matrícula aprovação")
    void deveConcluirMatriculaAprovacao() {
        // Arrange
        var aluno = new Aluno("RA001", "Danilo Machado", "danilo@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = turma.getOfertas().get(0).matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve concluir matrícula reprovação")
    void deveConcluirMatriculaReprovacao() {
        // Arrange
        var aluno = new Aluno("RA001", "Danilo Machado", "danilo@email.com");
        var turma = new Turma( "ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO) );
        var matricula = turma.getOfertas().get(0).matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado()); }

    @Test
    @DisplayName("Deve iniciar matrícula como ativa")
    void deveIniciarMatriculaComoAtiva() {
        // Arrange
        var aluno = new Aluno("RA002", "Doris Daniella", "doris@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));

        // Act
        var matricula = turma.getOfertas().get(0).matricular(aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir concluir matrícula já concluída")
    void deveImpedirConcluirMatriculaJaConcluida() {
        // Arrange
        var aluno = new Aluno("RA005", "Danilo Machado", "danilo@email.com");
        var turma = new Turma( "ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO) );
        var matricula = turma.getOfertas().get(0).matricular(aluno); matricula.concluir(ResultadoAcademico.APROVADO);

        // Act
        assertThrows( IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.REPROVADO) );
        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve impedir concluir matrícula trancada")
    void deveImpedirConcluirMatriculaTrancada() {
        // Arrange
        var aluno = new Aluno("RA004", "Doris Daniella", "doris@email.com");
        var turma = new Turma( "ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO) );
        var matricula = turma.getOfertas().get(0).matricular(aluno); matricula.trancar();

        // Act
        assertThrows( IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.APROVADO) );

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve manter a mesma referência do aluno na matrícula")
    void deveManterMesmoAlunoNaMatricula() {
        // Arrange
        var aluno = new Aluno("RA005", "Danilo Machado", "danilo@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.getOfertas().get(0);

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertSame(aluno, matricula.getAluno());
    }

    @Test
    @DisplayName("Deve aceitar código de matrícula com espaços")
    void deveAceitarCodigoComEspacos() {
        //Arrange
        var aluno = new Aluno("RA003", "Doris Daniella", "doris@email.com");
        var turma = new Turma( "ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO) );
        // Act
        var matricula = new Matricula( " MAT-001 ", aluno, turma );

        // Assert
        assertEquals("MAT-001", matricula.getCodigo());
    }

    @Test
    @DisplayName("Deve criar matrícula usando a turma")
    void deveCriarMatriculaUsandoTurma() {
        // Arrange
        var aluno = new Aluno("RA004", "Danilo Machado", "danilo@email.com");
        var turma = new Turma( "ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO) );

        // Act
        var matricula = new Matricula("MAT-001", aluno, turma);

        // Assert
        assertSame(turma, matricula.getTurma());
        assertSame(aluno, matricula.getAluno());
    }
}
