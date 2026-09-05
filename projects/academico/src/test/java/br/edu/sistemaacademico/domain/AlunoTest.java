package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoValido() {
        var aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");

        assertEquals("RA001", aluno.getIdentificador());
        assertEquals("Paola Oliveira", aluno.getNome());
        assertEquals("paola@email.com", aluno.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoIdentificadorForNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno(null, "Nome", "email@email.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno(" ", "Nome", "email@email.com"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA001", null, "email@email.com"));
    }

    @Test
    void deveLancarExcecaoQuandoEmailForInvalidoNaCriacao() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA001", "Nome", null));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA001", "Nome", "email-sem-arroba"));
    }

    @Test
    void deveAtualizarEmailQuandoNovoEmailForValido() {
        var aluno = new Aluno("RA001", "Nome", "antigo@email.com");

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarEmailQuandoNovoEmailForInvalido() {
        var aluno = new Aluno("RA001", "Nome", "valido@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("email-invalido"));
        assertEquals("valido@email.com", aluno.getEmail());
    }

    @Test
    void deveComecarSemHistoricoDeMatriculas() {
        var aluno = new Aluno("RA001", "Nome", "email@email.com");

        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveRetornarFalsoParaJaFoiAprovadoQuandoNuncaSeMatriculou() {
        var aluno = new Aluno("RA001", "Nome", "email@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertFalse(aluno.jaFoiAprovadoEm(disciplina));
    }
}