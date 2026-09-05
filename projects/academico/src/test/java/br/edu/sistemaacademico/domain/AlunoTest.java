package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        // Arrange & Act
        Aluno aluno = new Aluno("RA001", "Gustavo", "gustavo@email.com");

        // Assert
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Gustavo", aluno.getNome());
        assertEquals("gustavo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoSemNome() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "gustavo@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "gustavo@email.com"));
    }

    @Test
    void naoDeveCriarAlunoSemRegistroAcademico() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("", "Gustavo", "gustavo@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComRegistroAcademicoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Gustavo", "gustavo@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Gustavo", "email-invalido"));
    }

    @Test
    void naoDeveCriarAlunoComEmailNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Gustavo", null));
    }

    @Test
    void naoDeveCriarAlunoComEmailSemArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Gustavo", "emailsemarroba.com"));
    }

    @Test
    void deveAlterarEmailValido() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Gustavo", "antigo@email.com");

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarParaEmailInvalido() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Gustavo", "gustavo@email.com");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> aluno.setEmail("invalido"));
    }

    @Test
    void deveConsiderarAlunosComMesmoRegistroComoIguais() {
        // Arrange
        Aluno aluno1 = new Aluno("RA001", "Gustavo", "gustavo@email.com");
        Aluno aluno2 = new Aluno("RA001", "Outro Nome", "outro@email.com");

        // Act & Assert
        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void deveConsiderarAlunosComRegistrosDiferentesComoDesiguais() {
        // Arrange
        Aluno aluno1 = new Aluno("RA001", "Gustavo", "gustavo@email.com");
        Aluno aluno2 = new Aluno("RA002", "Gustavo", "gustavo@email.com");

        // Act & Assert
        assertNotEquals(aluno1, aluno2);
    }

    @Test
    void deveRetornarListaDeMatriculasVaziaInicialmente() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Gustavo", "gustavo@email.com");

        // Act & Assert
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveRetornarToStringComRegistroENome() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Gustavo", "gustavo@email.com");

        // Act
        String resultado = aluno.toString();

        // Assert
        assertTrue(resultado.contains("RA001"));
        assertTrue(resultado.contains("Gustavo"));
    }
}
