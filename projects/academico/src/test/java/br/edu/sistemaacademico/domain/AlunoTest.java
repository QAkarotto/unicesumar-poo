package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        // Act
        String nome = aluno.getNome();

        // Assert
        assertEquals("Igor", nome);
        assertEquals("RA001", aluno.getIdentificadorAcademico());
        assertEquals("igor@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoSemIdentificador() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "Igor", "igor@email.com")
        );
    }

    @Test
    void naoDeveCriarAlunoSemNome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "igor@email.com")
        );
    }

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Igor", "email-invalido")
        );
    }

    @Test
    void deveAlterarEmailValido() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarEmailParaValorInvalido() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );
    }

    @Test
    void naoDeveAdicionarMatriculaNula() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.adicionarMatricula(null)
        );
    }
}