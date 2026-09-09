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
    @DisplayName("Deve criar aluno válido e iniciar sem matrículas")
    void deveCriarAlunoValido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Não deve aceitar registro acadêmico vazio")
    void naoDeveAceitarRegistroInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("  ", "Ana Souza", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Não deve aceitar nome vazio")
    void naoDeveAceitarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "ana@email.com")
        );
    }

    @Test
    @DisplayName("Não deve aceitar e-mail em formato inválido")
    void naoDeveAceitarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana.email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana@email")
        );
    }

    @Test
    @DisplayName("Deve permitir alterar o e-mail por um valor válido")
    void deveAlterarEmailValido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        aluno.setEmail("ana.souza@empresa.com.br");

        assertEquals("ana.souza@empresa.com.br", aluno.getEmail());
    }

    @Test
    @DisplayName("Não deve alterar o e-mail para um valor inválido")
    void naoDeveAlterarEmailParaValorInvalido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("invalido")
        );
        assertEquals("ana@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("A lista de matrículas retornada deve ser imutável")
    void listaDeMatriculasDeveSerImutavel() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().add(null)
        );
    }

    @Test
    @DisplayName("Alunos com o mesmo registro acadêmico devem ser iguais")
    void deveCompararAlunosPeloRegistro() {
        var ana = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var mesmaAna = new Aluno("RA001", "Ana Maria", "ana.maria@email.com");
        var joao = new Aluno("RA002", "João Lima", "joao@email.com");

        assertEquals(ana, mesmaAna);
        assertEquals(ana.hashCode(), mesmaAna.hashCode());
        assertNotEquals(ana, joao);
        assertTrue(ana.equals(ana));
        assertFalse(ana.equals("RA001"));
    }

    @Test
    @DisplayName("toString deve conter registro e nome")
    void toStringDeveConterRegistroENome() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertEquals("RA001 - Ana Souza", aluno.toString());
    }
}
