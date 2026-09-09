package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        // Arrange / Act
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        // Assert
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Vinicios Eduardo", aluno.getNome());
        assertEquals("vinicios@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveAlterarEmailDoAluno() {
        // Arrange
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarEmailInvalido() {
        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "Vinicios Eduardo",
                        "email-invalido"
                )
        );
    }

    @Test
    void deveRejeitarRegistroAcademicoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "",
                        "Vinicios Eduardo",
                        "vinicios@email.com"
                )
        );
    }

    @Test
    void deveRejeitarNomeVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "",
                        "vinicios@email.com"
                )
        );
    }

    @Test
    void alunosComMesmoRegistroAcademicoDevemSerIguais() {
        // Arrange
        var aluno1 = new Aluno(
                "RA001",
                "Vinicios",
                "vinicios@email.com"
        );

        var aluno2 = new Aluno(
                "RA001",
                "Outro Nome",
                "outro@email.com"
        );

        // Assert
        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }
}