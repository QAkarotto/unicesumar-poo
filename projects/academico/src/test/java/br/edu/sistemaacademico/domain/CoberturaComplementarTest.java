package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoberturaComplementarTest {

    private static final PeriodoLetivo PERIODO = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

    private Aluno aluno() {
        return new Aluno("RA1", "Ana", "ana@email.com");
    }

    private Disciplina disciplina() {
        return new Disciplina("POO", "Programacao", 80);
    }

    private OfertaDisciplina oferta() {
        return new Turma("T1", PERIODO).ofertarDisciplina(disciplina());
    }

    @Test
    void trancarEcancelarMatricula() {
        var trancada = oferta().matricular(aluno());
        trancada.trancar();
        assertEquals(SituacaoMatricula.TRANCADA, trancada.getSituacao());
        assertThrows(IllegalStateException.class, trancada::cancelar);

        var cancelada = oferta().matricular(aluno());
        cancelada.cancelar();
        assertEquals(SituacaoMatricula.CANCELADA, cancelada.getSituacao());
        assertThrows(IllegalStateException.class, cancelada::trancar);
    }

    @Test
    void concluirExigeResultado() {
        var matricula = oferta().matricular(aluno());
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    void alterarEmail() {
        var aluno = aluno();
        aluno.setEmail("novo@email.com");
        assertEquals("novo@email.com", aluno.getEmail());
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
    }

    @Test
    void matriculaPelaTurma() {
        var turma = new Turma("T2", disciplina(), PERIODO);
        var matricula = new Matricula("M1", aluno(), turma);
        assertEquals("POO", matricula.getOfertaDisciplina().getDisciplina().getCodigo());
        assertEquals(turma, matricula.getTurma());
    }

    @Test
    void matriculaPelaTurmaExigeUmaUnicaOferta() {
        var turma = new Turma("T3", PERIODO);
        turma.ofertarDisciplina(disciplina());
        turma.ofertarDisciplina(new Disciplina("BD", "Banco", 80));
        var aluno = aluno();
        assertThrows(IllegalStateException.class, () -> new Matricula("M1", aluno, turma));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M1", aluno, (Turma) null));
    }

    @Test
    void matriculaValidaArgumentos() {
        var oferta = oferta();
        var aluno = aluno();
        assertThrows(IllegalArgumentException.class, () -> new Matricula(" ", aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M1", null, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("M1", aluno, (OfertaDisciplina) null));
    }

    @Test
    void igualdadeEHashCode() {
        assertEquals(aluno(), new Aluno("RA1", "Outro Nome", "outro@email.com"));
        assertEquals(aluno().hashCode(), new Aluno("RA1", "X", "x@email.com").hashCode());
        assertFalse(aluno().equals(new Aluno("RA2", "Ana", "ana@email.com")));
        assertFalse(aluno().equals(null));

        assertEquals(disciplina(), new Disciplina("POO", "Outro", 40));
        assertFalse(disciplina().equals(new Disciplina("BD", "Banco", 80)));
        assertEquals(disciplina().hashCode(), new Disciplina("POO", "Y", 10).hashCode());

        assertEquals(PERIODO, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        assertFalse(PERIODO.equals(new PeriodoLetivo(2026, Semestre.SEGUNDO)));
        assertEquals(PERIODO.hashCode(), new PeriodoLetivo(2026, Semestre.PRIMEIRO).hashCode());
    }

    @Test
    void representacoesEmTexto() {
        var matricula = oferta().matricular("M1", aluno());
        assertEquals("RA1 - Ana", aluno().toString());
        assertEquals("POO - Programacao (80h)", disciplina().toString());
        assertEquals("2026/1", PERIODO.toString());
        assertEquals("T1 - 2026/1", new Turma("T1", PERIODO).toString());
        assertEquals("POO - T1 - 2026/1", matricula.getOfertaDisciplina().toString());
        assertEquals("M1 - RA1 - POO - ATIVA", matricula.toString());
        assertTrue(matricula.getOfertaDisciplina().getMatriculas().contains(matricula));
    }

    @Test
    void validacoesDeTurmaDisciplinaEPeriodo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma(" ", PERIODO));
        assertThrows(IllegalArgumentException.class, () -> new Turma("T1", (PeriodoLetivo) null));
        assertThrows(IllegalArgumentException.class, () -> new Turma("T1", PERIODO).ofertarDisciplina(null));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO", "Programacao", 0));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
    }
}