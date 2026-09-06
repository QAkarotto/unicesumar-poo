package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    @DisplayName("Deve concluir matrícula aprovação")
    void deveConcluirMatriculaAprovacao() {
        // Arrange
        var aluno = new Aluno("RA002", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve concluir matrícula reprovação")
    void deveConcluirMatriculaReprovacao() {
        // Arrange
        var aluno = new Aluno("RA002", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve iniciar matrícula com resultado nulo (em curso)")
    void deveIniciarMatriculaComoAtiva() {
        // Arrange
        var aluno = new Aluno("RA001", "Priscila Gomes Reksua", "priscila@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve impedir concluir matrícula já concluída")
    void deveImpedirConcluirMatriculaJaConcluida() {
        // Arrange
        var aluno = new Aluno("RA005", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.REPROVADO));
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve manter a mesma referência do aluno na matrícula")
    void deveManterMesmoAlunoNaMatricula() {
        // Arrange
        var aluno = new Aluno("RA005", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertSame(aluno, matricula.getAluno());
    }

    @Test
    @DisplayName("Deve criar matrícula usando a oferta")
    void deveCriarMatriculaUsandoTurma() {
        // Arrange
        var aluno = new Aluno("RA004", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Assert
        assertSame(oferta, matricula.getOferta());
        assertSame(aluno, matricula.getAluno());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar matrícula com dados inválidos")
    void deveLancarExcecaoMatriculaDadosInvalidos() {
        // Arrange
        var aluno = new Aluno("RA001", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Matricula(null, aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("", aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-01", null, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-01", aluno, null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao concluir matrícula com resultado nulo")
    void deveLancarExcecaoResultadoNulo() {
        // Arrange
        var aluno = new Aluno("RA001", "Nathaly Vieira Pereira", "nathaly@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    @DisplayName("Deve incrementar próximo número da matrícula")
    void deveTestarProximoNum() {
        // Arrange & Act
        int num1 = Matricula.proximoNum();
        int num2 = Matricula.proximoNum();

        // Assert
        assertEquals(num1 + 1, num2);
    }
}