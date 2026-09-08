package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlunoTest {

    @Test
    @DisplayName("Deve criar um aluno valido")
    void deveCriarUmAlunoValido() {
        // Arrange / Act
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Assert
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertEquals(0, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Nao deve criar aluno sem registro academico")
    void naoDeveCriarAlunoSemRegistroAcademico() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Aluno("   ", "Ana Souza", "ana@email.com");
        });
    }

    @Test
    @DisplayName("Nao deve criar aluno sem nome")
    void naoDeveCriarAlunoSemNome() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Aluno("RA001", null, "ana@email.com");
        });
    }

    @Test
    @DisplayName("Nao deve criar aluno com e-mail invalido")
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Aluno("RA001", "Ana Souza", "ana.email.com");
        });
    }

    @Test
    @DisplayName("Deve trocar o e-mail do aluno")
    void deveTrocarOEmailDoAluno() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act
        aluno.setEmail("ana.souza@email.com");

        // Assert
        assertEquals("ana.souza@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Nao deve trocar para um e-mail invalido")
    void naoDeveTrocarParaUmEmailInvalido() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            aluno.setEmail("ana@email");
        });
        assertEquals("ana@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Dois alunos com o mesmo registro academico sao iguais")
    void doisAlunosComOMesmoRegistroAcademicoSaoIguais() {
        // Arrange
        Aluno primeiro = new Aluno("RA001", "Ana Souza", "ana@email.com");
        Aluno segundo = new Aluno("RA001", "Ana S. Souza", "ana.s@email.com");
        Aluno outro = new Aluno("RA002", "Bia Lima", "bia@email.com");

        // Assert
        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
        assertFalse(primeiro.equals(outro));
        assertFalse(primeiro.equals("RA001"));
        assertEquals("RA001 - Ana Souza", primeiro.toString());
    }
}
