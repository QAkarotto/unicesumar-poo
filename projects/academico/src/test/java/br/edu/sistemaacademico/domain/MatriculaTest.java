package br.edu.sistemaacademico.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MatriculaTest {

    @Test
    void deveCriarMatricula() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        Matricula m = o.matricular(a);

        assertEquals(a, m.getAluno());
        assertEquals(o, m.getOferta());
        assertNull(m.getResultado());
    }

    @Test
    void deveConcluirComAprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);
        Matricula m = o.matricular(a);

        m.concluir(ResultadoAcademico.APROVADO);
        assertEquals(ResultadoAcademico.APROVADO, m.getResultado());
    }

    @Test
    void deveConcluirComReprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);
        Matricula m = o.matricular(a);

        m.concluir(ResultadoAcademico.REPROVADO);
        assertEquals(ResultadoAcademico.REPROVADO, m.getResultado());
    }

    @Test
    void naoDeveConcluirDuasVezes() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);
        Matricula m = o.matricular(a);

        m.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, () ->
            m.concluir(ResultadoAcademico.REPROVADO));
    }
}
