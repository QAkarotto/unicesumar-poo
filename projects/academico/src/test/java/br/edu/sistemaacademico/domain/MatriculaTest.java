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
    void naoDeveCriarMatriculaComAlunoNulo() {
        var oferta = turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> new Matricula(null, oferta));
    }

    @Test
    void naoDeveCriarMatriculaComOfertaNula() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula(aluno, null));
    }

    @Test
    void matriculaDeveComecarSemResultado() {
        var oferta = turma.ofertarDisciplina(disciplina);

        var matricula = oferta.matricular(aluno);

        assertNull(matricula.getResultado());
    }

    @Test
    void deveConcluirMatriculaComResultadoAprovado() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void deveConcluirMatriculaComResultadoReprovado() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    void naoDeveConcluirMatriculaQueJaFoiConcluida() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO));
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void toStringDeveIndicarEmAndamentoQuandoSemResultado() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        var texto = matricula.toString();

        assertTrue(texto.contains("EM ANDAMENTO"));
    }

    @Test
    void toStringDeveRefletirResultadoAposConclusao() {
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        var texto = matricula.toString();

        assertTrue(texto.contains("APROVADO"));
    }
}