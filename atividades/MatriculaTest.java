package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    private Turma criarTurma() {
        Disciplina disciplina = new Disciplina("ALG1", "Algoritmos I", 60);
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        return new Turma("T01", disciplina, periodo);
    }

    private Aluno criarAluno() {
        return new Aluno("2026001", "Maria Silva", "maria@exemplo.com");
    }

    @Test
    void deveCriarMatriculaValidaComSituacaoEmCurso() {
        // Arrange
        Turma turma = criarTurma();
        Aluno aluno = criarAluno();

        // Act
        Matricula matricula = new Matricula("M001", aluno, turma);

        // Assert
        assertEquals(aluno, matricula.getAluno());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.EM_CURSO, matricula.getSituacao());
    }

    @Test
    void naoDeveCriarMatriculaComCodigoNuloOuVazio() {
        // Arrange
        Turma turma = criarTurma();
        Aluno aluno = criarAluno();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(null, aluno, turma));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("", aluno, turma));
    }

    @Test
    void naoDeveCriarMatriculaComAlunoNulo() {
        // Arrange
        Turma turma = criarTurma();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("M001", null, turma));
    }

    @Test
    void naoDeveCriarMatriculaComTurmaNula() {
        // Arrange
        Aluno aluno = criarAluno();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("M001", aluno, null));
    }

    @Test
    void deveAprovarMatriculaEmCurso() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());

        // Act
        matricula.aprovar();

        // Assert
        assertEquals(SituacaoMatricula.APROVADO, matricula.getSituacao());
    }

    @Test
    void deveReprovarMatriculaEmCurso() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());

        // Act
        matricula.reprovar();

        // Assert
        assertEquals(SituacaoMatricula.REPROVADO, matricula.getSituacao());
    }

    @Test
    void naoDeveAprovarMatriculaJaAprovada() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());
        matricula.aprovar();

        // Act / Assert
        assertThrows(IllegalStateException.class, matricula::aprovar);
    }

    @Test
    void naoDeveAprovarMatriculaJaReprovada() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());
        matricula.reprovar();

        // Act / Assert
        assertThrows(IllegalStateException.class, matricula::aprovar);
    }

    @Test
    void naoDeveReprovarMatriculaJaReprovada() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());
        matricula.reprovar();

        // Act / Assert
        assertThrows(IllegalStateException.class, matricula::reprovar);
    }

    @Test
    void naoDeveReprovarMatriculaJaAprovada() {
        // Arrange
        Matricula matricula = new Matricula("M001", criarAluno(), criarTurma());
        matricula.aprovar();

        // Act / Assert
        assertThrows(IllegalStateException.class, matricula::reprovar);
    }
}
