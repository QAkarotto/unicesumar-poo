package br.edu.sistemaacademico.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OfertaDisciplinaTest {

    @Test
    void deveCriarOferta() {
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);

        OfertaDisciplina o = t.ofertarDisciplina(d);

        assertEquals(d, o.getDisciplina());
        assertEquals(t, o.getTurma());
    }

    @Test
    void deveMatricularAluno() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        Matricula m = o.matricular(a);

        assertNotNull(m);
        assertEquals(a, m.getAluno());
        assertEquals(1, o.getMatriculas().size());
    }

    @Test
    void naoDeveMatricularAlunoDuplicado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        o.matricular(a);

        assertThrows(IllegalArgumentException.class, () ->
            o.matricular(a));
    }

    @Test
    void naoDeveMatricularAposAprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        Matricula m = o.matricular(a);
        m.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, () ->
            o.matricular(a));
    }

    @Test
    void devePermitirNovaMatriculaAposReprovado() {
        Aluno a = new Aluno("RA123", "João", "joao@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma t1 = new Turma("T1", p);
        OfertaDisciplina o1 = t1.ofertarDisciplina(d);
        Matricula m1 = o1.matricular(a);
        m1.concluir(ResultadoAcademico.REPROVADO);

        // aluno reprovado deve poder se matricular de novo, em outra oferta da mesma disciplina
        Turma t2 = new Turma("T2", p);
        OfertaDisciplina o2 = t2.ofertarDisciplina(d);

        assertDoesNotThrow(() -> o2.matricular(a));
    }

    @Test
    void devePermitirMatriculaEmOutroAluno() {
        Aluno a1 = new Aluno("RA123", "João", "joao@email.com");
        Aluno a2 = new Aluno("RA456", "Maria", "maria@email.com");
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T1", p);
        OfertaDisciplina o = t.ofertarDisciplina(d);

        o.matricular(a1);
        assertDoesNotThrow(() -> o.matricular(a2));
        assertEquals(2, o.getMatriculas().size());
    }
}
