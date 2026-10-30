package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    void naoDeveAtualizarParaEmailInvalido() {
        Aluno aluno = new Aluno("RA004", "Joao", "joao@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
        assertEquals("joao@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoComIdentificadorVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno(" ", "Ana", "ana@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA006", "Ana", " "));
    }

    @Test
    void deveAtualizarEmailComValorValido() {
        Aluno aluno = new Aluno("RA007", "Joao", "joao@email.com");

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveConsiderarIguaisAlunosComMesmoIdentificador() {
        Aluno aluno1 = new Aluno("RA008", "Joao", "joao@email.com");
        Aluno aluno2 = new Aluno("RA008", "Joao Outro", "outro@email.com");

        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
        assertEquals(aluno1, aluno1);
    }

    @Test
    void naoDeveConsiderarIguaisAlunosComIdentificadorDiferente() {
        Aluno aluno1 = new Aluno("RA009", "Joao", "joao@email.com");
        Aluno aluno2 = new Aluno("RA010", "Joao", "joao@email.com");

        assertNotEquals(aluno1, aluno2);
        assertNotEquals(aluno1, "RA009");
    }

    @Test
    void representacaoTextualContemDadosDoAluno() {
        Aluno aluno = new Aluno("RA011", "Joao", "joao@email.com");

        String texto = aluno.toString();

        assertTrue(texto.contains("RA011"));
        assertTrue(texto.contains("Joao"));
        assertTrue(texto.contains("joao@email.com"));
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
