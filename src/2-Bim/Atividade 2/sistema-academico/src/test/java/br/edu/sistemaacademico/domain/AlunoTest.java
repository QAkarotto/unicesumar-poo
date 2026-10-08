package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        Aluno aluno = new Aluno("RA001", "Lucas", "lucas@email.com");

        assertEquals("RA001", aluno.getIdentificadorAcademico());
        assertEquals("Lucas", aluno.getNome());
        assertEquals("lucas@email.com", aluno.getEmail());
        assertTrue(aluno.getHistorico().isEmpty());
    }

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA002", "Ana", "email-invalido"));
    }

    @Test
    void naoDeveCriarAlunoComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA003", " ", "ana@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComIdentificadorVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno(" ", "Lucas", "lucas@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA006", "Lucas", " "));
    }

    @Test
    void deveConsiderarAlunosComMesmoRaComoIguais() {
        Aluno aluno1 = new Aluno("RA007", "Lucas", "lucas@email.com");
        Aluno aluno2 = new Aluno("RA007", "Outro Nome", "outro@email.com");

        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void naoDeveAtualizarParaEmailInvalido() {
        Aluno aluno = new Aluno("RA004", "Joao", "joao@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
        assertEquals("joao@email.com", aluno.getEmail());
    }

    @Test
    void deveIndicarAprovacaoSomenteAposResultadoAprovado() {
        Aluno aluno = new Aluno("RA005", "Maria", "maria@email.com");
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertFalse(aluno.jaAprovadoEm(disciplina));

        Matricula matricula = oferta.matricular("M001", aluno);
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertTrue(aluno.jaAprovadoEm(disciplina));
    }
}
