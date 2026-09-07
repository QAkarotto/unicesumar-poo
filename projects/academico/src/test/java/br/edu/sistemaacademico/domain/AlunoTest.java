package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("RA9000", "Vegeta", "vegeta@sayajin.com");
    }

    @Test
    @DisplayName("Deve criar aluno com dados validos")
    void deveCriarAlunoComSucesso() {
        assertEquals("RA9000", aluno.getRegistroAcademico());
        assertEquals("Vegeta", aluno.getNome());
        assertEquals("vegeta@sayajin.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Deve alterar email do aluno quando for valido")
    void deveAlterarEmail() {
        aluno.setEmail("prince.vegeta@capsule.com");
        assertEquals("prince.vegeta@capsule.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar cadastrar dados invalidos")
    void deveValidarCamposObrigatoriosEEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Aluno("", "Vegeta", "vegeta@sayajin.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA9000", "  ", "vegeta@sayajin.com"));
        assertThrows(IllegalArgumentException.class, () -> new Aluno("RA9000", "Vegeta", "email_invalido"));
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("email_invalido"));
    }

    @Test
    @DisplayName("Deve comparar alunos pelo Registro Academico")
    void deveValidarEqualsEHashCode() {
        Aluno aluno2 = new Aluno("RA9000", "Outro Nome", "outro@email.com");
        Aluno alunoDiferente = new Aluno("RA9001", "Vegeta", "vegeta@sayajin.com");

        assertEquals(aluno, aluno2);
        assertEquals(aluno.hashCode(), aluno2.hashCode());
        assertNotEquals(aluno, alunoDiferente);
        assertNotEquals(aluno, "TextoQualquer");
    }

    @Test
    @DisplayName("Deve retornar toString formatado")
    void deveRetornarToString() {
        assertEquals("RA9000 - Vegeta", aluno.toString());
    }
}