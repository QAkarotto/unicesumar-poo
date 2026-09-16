package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlunoTest {

    @Test
    void deveCriarAlunoValido() {
        // Arrange
        String identificador = "2026001";
        String nome = "Maria Silva";
        String email = "maria.silva@exemplo.com";

        // Act
        Aluno aluno = new Aluno(identificador, nome, email);

        // Assert
        assertEquals(identificador, aluno.getIdentificadorAcademico());
        assertEquals(nome, aluno.getNome());
        assertEquals(email, aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoComIdentificadorNuloOuVazio() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Maria Silva", "maria@exemplo.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("  ", "Maria Silva", "maria@exemplo.com"));
    }

    @Test
    void naoDeveCriarAlunoComNomeNuloOuVazio() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", null, "maria@exemplo.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", "", "maria@exemplo.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailNuloOuVazio() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", "Maria Silva", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", "Maria Silva", " "));
    }

    @Test
    void naoDeveCriarAlunoComEmailEmFormatoInvalido() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", "Maria Silva", "email-invalido"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("2026001", "Maria Silva", "maria@exemplo"));
    }

    @Test
    void deveAlterarNomeQuandoValido() {
        // Arrange
        Aluno aluno = new Aluno("2026001", "Maria Silva", "maria@exemplo.com");

        // Act
        aluno.alterarNome("Maria Souza");

        // Assert
        assertEquals("Maria Souza", aluno.getNome());
    }

    @Test
    void naoDeveAlterarNomeParaValorInvalido() {
        // Arrange
        Aluno aluno = new Aluno("2026001", "Maria Silva", "maria@exemplo.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> aluno.alterarNome(""));
    }

    @Test
    void deveAlterarEmailQuandoValido() {
        // Arrange
        Aluno aluno = new Aluno("2026001", "Maria Silva", "maria@exemplo.com");

        // Act
        aluno.alterarEmail("maria.nova@exemplo.com");

        // Assert
        assertEquals("maria.nova@exemplo.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarEmailParaValorInvalido() {
        // Arrange
        Aluno aluno = new Aluno("2026001", "Maria Silva", "maria@exemplo.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> aluno.alterarEmail("email-invalido"));
    }
}
