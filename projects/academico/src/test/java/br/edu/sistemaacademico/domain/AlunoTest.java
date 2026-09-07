package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    @DisplayName("Deve criar aluno com dados validos")
    void deveCriarAlunoComDadosValidos() {
        // Arrange
        var ra = "RA123";
        var nome = "Ketely";
        var email = "ketely@email.com";

        // Act
        var aluno = new Aluno(ra, nome, email);

        // Assert
        assertEquals(ra, aluno.getRegistroAcademico());
        assertEquals(nome, aluno.getNome());
        assertEquals(email, aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }
}