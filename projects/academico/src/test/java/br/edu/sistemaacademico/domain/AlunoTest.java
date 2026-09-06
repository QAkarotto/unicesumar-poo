
package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void construtorComDadosValidos_deveCriarAlunoCorretamente() {
        // Arrange
        String identificador = "RA2026001";
        String nome = "Paola Oliveira";
        String email = "paola.oliveira@email.com";

        // Act
        Aluno aluno = new Aluno(identificador, nome, email);

        // Assert
        assertEquals(identificador, aluno.getIdentificadorAcademico());
        assertEquals(nome, aluno.getNome());
        assertEquals(email, aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void construtorComIdentificadorNuloOuVazio_deveLancarExcecao() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Nome Valido", "a@a.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("   ", "Nome Valido", "a@a.com"));
    }

    @Test
    void construtorComNomeNuloOuVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "a@a.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "  ", "a@a.com"));
    }

    @Test
    void construtorComEmailInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Nome Valido", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Nome Valido", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Nome Valido", "emailsemarroba.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Nome Valido", "email@semponto"));
    }

    @Test
    void alterarNome_comValorValido_deveAtualizarNome() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome Antigo", "a@a.com");

        // Act
        aluno.alterarNome("Nome Novo");

        // Assert
        assertEquals("Nome Novo", aluno.getNome());
    }

    @Test
    void alterarNome_comValorInvalido_deveLancarExcecao() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome Antigo", "a@a.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> aluno.alterarNome(null));
        assertThrows(IllegalArgumentException.class, () -> aluno.alterarNome(""));
    }

    @Test
    void alterarEmail_comValorValido_deveAtualizarEmail() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome", "antigo@email.com");

        // Act
        aluno.alterarEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void alterarEmail_comValorInvalido_deveLancarExcecao() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome", "antigo@email.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> aluno.alterarEmail("invalido"));
    }

    @Test
    void getMatriculas_deveRetornarListaImutavel() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome", "a@a.com");
        List<Matricula> matriculas = aluno.getMatriculas();

        // Act / Assert
        assertThrows(UnsupportedOperationException.class,
                () -> matriculas.add(null));
    }

    @Test
    void doisAlunosComMesmoIdentificador_devemSerIguais() {
        // Arrange
        Aluno aluno1 = new Aluno("RA001", "Nome A", "a@a.com");
        Aluno aluno2 = new Aluno("RA001", "Nome B", "b@b.com");

        // Act / Assert
        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void doisAlunosComIdentificadoresDiferentes_naoDevemSerIguais() {
        // Arrange
        Aluno aluno1 = new Aluno("RA001", "Nome A", "a@a.com");
        Aluno aluno2 = new Aluno("RA002", "Nome A", "a@a.com");

        // Act / Assert
        assertNotEquals(aluno1, aluno2);
    }

    @Test
    void possuiAprovacaoEm_semMatriculas_deveRetornarFalse() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Nome", "a@a.com");
        Disciplina disciplina = new Disciplina("POO", "Prog. Orientada a Objetos", 80);

        // Act / Assert
        assertFalse(aluno.possuiAprovacaoEm(disciplina));
    }
}