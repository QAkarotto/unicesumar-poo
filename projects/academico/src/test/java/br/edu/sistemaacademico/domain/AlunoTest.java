package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoValido() {
        // Act
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        // Assert
        assertEquals("2026001", aluno.getIdentificadorAcademico());
        assertEquals("Matheus", aluno.getNome());
        assertEquals("matheus@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarIdentificadorVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "Matheus", "matheus@email.com")
        );
    }

    @Test
    void deveRejeitarNomeVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("2026001", "", "matheus@email.com")
        );
    }

    @Test
    void deveRejeitarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("2026001", "Matheus", "email-invalido")
        );
    }

    @Test
    void deveAlterarNome() {
        // Arrange
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        // Act
        aluno.setNome("Matheus Machado");

        // Assert
        assertEquals("Matheus Machado", aluno.getNome());
    }

    @Test
    void deveAlterarEmail() {
        // Arrange
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarAlteracaoParaNomeVazio() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setNome("")
        );
    }

    @Test
    void deveRejeitarAlteracaoParaEmailInvalido() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );
    }

    @Test
    void deveIniciarSemMatriculas() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveRetornarListaDeMatriculasSomenteParaLeitura() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        List<Matricula> matriculas = aluno.getMatriculas();

        assertThrows(
                UnsupportedOperationException.class,
                () -> matriculas.clear()
        );
    }

    @Test
    void alunoSemDisciplinaAprovadaDeveRetornarFalso() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertFalse(aluno.jaAprovadoEm(disciplina));
    }

    @Test
    void disciplinaNulaDeveRetornarFalso() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        assertFalse(aluno.jaAprovadoEm(null));
    }

    @Test
    void deveCompararAlunosPeloIdentificadorAcademico() {
        Aluno aluno1 = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        Aluno aluno2 = new Aluno(
                "2026001",
                "Outro Nome",
                "outro@email.com"
        );

        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void deveGerarTextoDoAluno() {
        Aluno aluno = new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );

        String resultado = aluno.toString();

        assertTrue(resultado.contains("2026001"));
        assertTrue(resultado.contains("Matheus"));
        assertTrue(resultado.contains("matheus@email.com"));
    }
}