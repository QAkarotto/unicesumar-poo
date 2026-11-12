package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidacoesDominioTest {
    @Test
    void rejeitaDadosInvalidosDeAlunoEDisciplina() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno(null, "Aluno", "aluno@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA", " ", "aluno@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA", "Aluno", "invalido"));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina(" ", "POO", 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO", null, 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO", "POO", 0));
        var aluno = new Aluno(" RA ", " Aluno ", "aluno@example.com");
        aluno.setEmail("novo@example.com");
        assertEquals("RA", aluno.getRegistroAcademico());
        assertEquals("Aluno", aluno.getNome());
        assertEquals("novo@example.com", aluno.getEmail());
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail(null));
        assertEquals("novo@example.com", aluno.getEmail());
    }

    @Test
    void validaTurmaPeriodoEOferta() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("T", (PeriodoLetivo) null));
        var turma = new Turma("T", periodo);
        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
        var aluno = new Aluno("RA", "Aluno", "aluno@example.com");
        assertThrows(IllegalStateException.class, () -> new Matricula("M", aluno, turma));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M", aluno, (Turma) null));
        assertEquals(2, periodo.getSemestre().getNumero());
        assertEquals("2026/2", periodo.toString());
    }

    @Test
    void protegeHistoricoEImpedeOperacoesAposTrancamentoOuCancelamento() {
        var aluno = new Aluno("RA", "Aluno", "aluno@example.com");
        var turma = new Turma("T", new Disciplina("POO", "POO", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = new Matricula("M", aluno, turma);
        assertThrows(UnsupportedOperationException.class, () -> aluno.getMatriculas().clear());
        assertThrows(UnsupportedOperationException.class, () -> turma.getOfertas().getFirst().getMatriculas().clear());
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        matricula.trancar();
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertThrows(IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.APROVADO));
        var outro = turma.getOfertas().getFirst().matricular("M2", new Aluno("RA2", "Outro", "outro@example.com"));
        outro.cancelar();
        assertEquals(SituacaoMatricula.CANCELADA, outro.getSituacao());
        assertThrows(IllegalStateException.class, outro::trancar);
        assertNull(outro.getResultado());
    }

    @Test
    void rejeitaMatriculaIncompletaSemRegistrarNoDominio() {
        var aluno = new Aluno("RA", "Aluno", "aluno@example.com");
        var turma = new Turma("T", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(" ", aluno));
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular("M", null));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M", aluno, (OfertaDisciplina) null));
        assertTrue(aluno.getMatriculas().isEmpty());
        assertTrue(oferta.getMatriculas().isEmpty());
    }
}
