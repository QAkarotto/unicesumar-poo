package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MatriculaTest {

    @Test
    @DisplayName("Deve criar matrícula vinculando o aluno e a turma corretos")
    void deveCriarMatriculaCorretamente() {
        var aluno = new Aluno("ALUNO1", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO001", "POO", 80);
        var turma = new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.SEGUNDO));

        var matricula = new Matricula("MAT1", aluno, turma);

        assertEquals("MAT1", matricula.getCodigo());
        assertSame(aluno, matricula.getAluno());
        assertSame(turma, matricula.getTurma());
    }
}