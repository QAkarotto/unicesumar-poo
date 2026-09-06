package br.edu.sistemaacademico.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void deveCriarAluno() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        assertEquals("RA123", a.getRa());
        assertEquals("João", a.getNome());
        assertEquals("joao@email.com", a.getEmail());
    }

    @Test
    void deveAlterarEmail() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        a.setEmail("novo@email.com");
        assertEquals("novo@email.com", a.getEmail());
    }

    @Test
    void deveListarMatriculas() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        assertTrue(a.getMatriculas().isEmpty());

        o.matricular(a);
        assertEquals(1, a.getMatriculas().size());
    }

    @Test
    void deveDetectarAprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        assertFalse(a.jaFoiAprovadoEm(d));

        Matricula m = o.matricular(a);
        m.concluir(ResultadoAcademico.APROVADO);
        assertTrue(a.jaFoiAprovadoEm(d));
    }

    @Test
    void deveRetornarFalsoApósReprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        Matricula m = o.matricular(a);
        m.concluir(ResultadoAcademico.REPROVADO);
        assertFalse(a.jaFoiAprovadoEm(d));
    }
}
