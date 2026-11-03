package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainCoverageTest {

    @Test
    void deveExporDadosDosObjetosDoDominio() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var disciplina = new Disciplina(" POO ", " Programação Orientada a Objetos ", 80);
        var turma = new Turma(" ESOFT4S-NA ", disciplina, periodo);
        var aluno = new Aluno(" RA001 ", " Ana Souza ", "ana@email.com");
        var matricula = turma.getOfertas().getFirst().matricular(" MAT-001 ", aluno);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals("2026/2", periodo.toString());
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertSame(periodo, turma.getPeriodoLetivo());
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertEquals("MAT-001", matricula.getCodigo());
        assertSame(aluno, matricula.getAluno());
        assertSame(turma, matricula.getTurma());
        assertSame(turma.getOfertas().getFirst(), matricula.getOfertaDisciplina());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertEquals(null, matricula.getResultado());
        assertTrue(matricula.toString().contains("MAT-001"));
        assertTrue(turma.toString().contains("ESOFT4S-NA"));
        assertTrue(turma.getOfertas().getFirst().toString().contains("POO"));
    }

    @Test
    void deveValidarDadosObrigatoriosEComparacoes() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno(null, "Ana", "ana@email.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA", "", "ana@email.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA", "Ana", "invalido"));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("", "POO", 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO", "", 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO", "POO", 0));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
        assertThrows(IllegalArgumentException.class, () -> new Turma("", new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
        assertThrows(IllegalArgumentException.class, () -> new Turma("T1", (PeriodoLetivo) null));

        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        aluno.setEmail("novo@email.com");
        assertEquals("novo@email.com", aluno.getEmail());
        assertEquals(aluno, new Aluno("RA001", "Outra pessoa", "outra@email.com"));
        assertNotEquals(aluno, new Aluno("RA002", "Ana", "ana@email.com"));
        assertEquals(new Disciplina("POO", "POO", 80), new Disciplina("POO", "Outra", 40));
        assertFalse(new Disciplina("POO", "POO", 80).equals("POO"));
        assertEquals(new PeriodoLetivo(2026, Semestre.PRIMEIRO), new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        assertNotEquals(new PeriodoLetivo(2026, Semestre.PRIMEIRO), new PeriodoLetivo(2026, Semestre.SEGUNDO));
    }

    @Test
    void deveValidarEstadosEConstrutoresDaMatricula() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turmaSemOferta = new Turma("T0", periodo);
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        assertThrows(IllegalStateException.class, () -> new Matricula("MAT-0", aluno, turmaSemOferta));

        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("", aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-1", null, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-1", aluno, (OfertaDisciplina) null));

        var ativa = oferta.matricular("MAT-1", aluno);
        assertThrows(IllegalArgumentException.class, () -> ativa.concluir(null));
        ativa.trancar();
        assertEquals(SituacaoMatricula.TRANCADA, ativa.getSituacao());
        assertThrows(IllegalStateException.class, ativa::cancelar);

        var outroAluno = new Aluno("RA002", "Bia", "bia@email.com");
        var outraOferta = new Turma("T2", periodo)
                .ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));
        var outraMatricula = outraOferta.matricular("MAT-2", outroAluno);
        outraMatricula.cancelar();
        assertEquals(SituacaoMatricula.CANCELADA, outraMatricula.getSituacao());
        assertThrows(UnsupportedOperationException.class, () -> outraOferta.getMatriculas().clear());
        assertThrows(UnsupportedOperationException.class, () -> outroAluno.getMatriculas().clear());
    }
}
