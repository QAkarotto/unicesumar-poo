package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    @Test
    @DisplayName("Deve registrar a mesma matrícula no aluno e na oferta")
    void deveRegistrarMatriculaNoAlunoENaOferta() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        var matricula = oferta.matricular(aluno);

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertEquals(matricula, oferta.getMatriculas().get(0));
        assertEquals(matricula, aluno.getMatriculas().get(0));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        var aluno = new Aluno("RA002", "Alexandre Gaia", "alexandre@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 40));
        oferta.matricular("MATRICULA-001", aluno);

        assertThrows(IllegalArgumentException.class, () -> oferta.matricular("MATRICULA-002", aluno));
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve permitir nova matricula em ofertas de turmas diferentes")
    void devePermitirNovaMatriculaEmOfertasDiferentes() {
        var aluno = new Aluno("RA003", "Carlos Silva", "carlos@email.com");
        var turma1 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var oferta1 = turma1.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        var matricula1 = oferta1.matricular("MATRICULA-003", aluno);

        var turma2 = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta2 = turma2.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));
        var matricula2 = oferta2.matricular("MATRICULA-004", aluno);

        assertEquals(SituacaoMatricula.ATIVA, matricula1.getSituacao());
        assertEquals(SituacaoMatricula.ATIVA, matricula2.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar matricular aluno nulo")
    void deveLancarExcecaoAoMatricularAlunoNulo() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));

        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(null));
    }

    @Test
    @DisplayName("Deve alterar estado da matricula ao cancelar")
    void deveAlterarEstadoAoCancelarMatricula() {
        var aluno = new Aluno("RA006", "Beatriz Rocha", "beatriz@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        var matricula = oferta.matricular("MATRICULA-009", aluno);

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }
}