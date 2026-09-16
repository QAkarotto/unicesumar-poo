package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaValida() {
        // Arrange
        String codigo = "ALG1";
        String nome = "Algoritmos I";
        int cargaHoraria = 60;

        // Act
        Disciplina disciplina = new Disciplina(codigo, nome, cargaHoraria);

        // Assert
        assertEquals(codigo, disciplina.getCodigo());
        assertEquals(nome, disciplina.getNome());
        assertEquals(cargaHoraria, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoNuloOuVazio() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Algoritmos I", 60));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("", "Algoritmos I", 60));
    }

    @Test
    void naoDeveCriarDisciplinaComNomeNuloOuVazio() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("ALG1", null, 60));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("ALG1", "  ", 60));
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaZeroOuNegativa() {
        // Arrange / Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("ALG1", "Algoritmos I", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("ALG1", "Algoritmos I", -10));
    }

    @Test
    void deveAlterarNomeQuandoValido() {
        // Arrange
        Disciplina disciplina = new Disciplina("ALG1", "Algoritmos I", 60);

        // Act
        disciplina.alterarNome("Algoritmos e Estruturas de Dados I");

        // Assert
        assertEquals("Algoritmos e Estruturas de Dados I", disciplina.getNome());
    }

    @Test
    void naoDeveAlterarNomeParaValorInvalido() {
        // Arrange
        Disciplina disciplina = new Disciplina("ALG1", "Algoritmos I", 60);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> disciplina.alterarNome(null));
    }

    @Test
    void deveAlterarCargaHorariaQuandoValida() {
        // Arrange
        Disciplina disciplina = new Disciplina("ALG1", "Algoritmos I", 60);

        // Act
        disciplina.alterarCargaHoraria(80);

        // Assert
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveAlterarCargaHorariaParaValorInvalido() {
        // Arrange
        Disciplina disciplina = new Disciplina("ALG1", "Algoritmos I", 60);

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> disciplina.alterarCargaHoraria(-5));
    }
}
