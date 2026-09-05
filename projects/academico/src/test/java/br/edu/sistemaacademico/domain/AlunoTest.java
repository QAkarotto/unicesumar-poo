package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    @DisplayName("Deve criar um aluno válido")
    void deveCriarAlunoValido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar registro acadêmico nulo ou em branco")
    void deveRejeitarRegistroAcademicoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(null, "Ana Souza", "ana@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("  ", "Ana Souza", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo ou em branco")
    void deveRejeitarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "ana@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", " ", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Deve rejeitar e-mail nulo ou em formato inválido")
    void deveRejeitarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana-email.com")
        );
    }

    @Test
    @DisplayName("Deve permitir alterar o e-mail do aluno")
    void devePermitirAlterarEmail() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        aluno.setEmail("ana.souza@email.com");

        assertEquals("ana.souza@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve rejeitar alteração para e-mail inválido")
    void deveRejeitarAlteracaoParaEmailInvalido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
    }

    @Test
    @DisplayName("Alunos com o mesmo registro acadêmico devem ser iguais")
    void deveConsiderarAlunosIguaisPeloRegistroAcademico() {
        var aluno1 = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var aluno2 = new Aluno("RA001", "Outro Nome", "outro@email.com");
        var aluno3 = new Aluno("RA002", "Ana Souza", "ana@email.com");

        assertEquals(aluno1, aluno1);
        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
        assertNotEquals(aluno1, aluno3);
        assertNotEquals(aluno1, null);
        assertNotEquals(aluno1, "RA001");
        assertFalse(aluno1.equals(new Object()));
    }

    @Test
    @DisplayName("O toString deve conter o registro acadêmico e o nome")
    void toStringDeveConterRegistroENome() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertEquals("RA001 - Ana Souza", aluno.toString());
    }
}
