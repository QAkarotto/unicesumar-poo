package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    private final Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
    private final Turma turma = new Turma("T1", periodo);
    private final Aluno aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

    @Test
    void matriculaDeveComecarSemResultado() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertNull(matricula.getResultado());
    }

    @Test
    void deveConcluirMatriculaComResultadoAprovado() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void deveConcluirMatriculaComResultadoReprovado() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    void naoDeveConcluirMatriculaQueJaFoiConcluida() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO));
        // resultado original permanece inalterado
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void toStringDeveIndicarEmAndamentoQuandoSemResultado() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        // Act
        var texto = matricula.toString();

        // Assert
        assertTrue(texto.contains("EM ANDAMENTO"));
    }

    @Test
    void toStringDeveRefletirResultadoAposConclusao() {
        // Arrange
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act
        var texto = matricula.toString();

        // Assert
        assertTrue(texto.contains("APROVADO"));
    }
}
