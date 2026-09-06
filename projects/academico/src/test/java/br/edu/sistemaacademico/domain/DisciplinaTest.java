package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void construtorComDadosValidos_deveCriarDisciplinaCorretamente() {
        // Arrange / Act
        Disciplina disciplina = new Disciplina("POO", "ProgramaÃ§Ã£o Orientada a Objetos", 80);

        // Assert
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("ProgramaÃ§Ã£o Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void construtorComCodigoNuloOuVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Nome", 40));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("  ", "Nome", 40));
    }

    @Test
    void construtorComNomeNuloOuVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("COD", null, 40));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("COD", "  ", 40));
    }

    @Test
    void construtorComCargaHorariaInvalida_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("COD", "Nome", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("COD", "Nome", -10));
    }

    @Test
    void alterarNome_comValorValido_deveAtualizarNome() {
        // Arrange
        Disciplina disciplina = new Disciplina("COD", "Nome Antigo", 40);

        // Act
        disciplina.alterarNome("Nome Novo");

        // Assert
        assertEquals("Nome Novo", disciplina.getNome());
    }

    @Test
    void alterarNome_comValorInvalido_deveLancarExcecao() {
        // Arrange
        Disciplina disciplina = new Disciplina("COD", "Nome", 40);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> disciplina.alterarNome(null));
    }

    @Test
    void alterarCargaHoraria_comValorValido_deveAtualizarCargaHoraria() {
        // Arrange
        Disciplina disciplina = new Disciplina("COD", "Nome", 40);

        // Act
        disciplina.alterarCargaHoraria(60);

        // Assert
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    void alterarCargaHoraria_comValorInvalido_deveLancarExcecao() {
        // Arrange
        Disciplina disciplina = new Disciplina("COD", "Nome", 40);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> disciplina.alterarCargaHoraria(0));
        assertThrows(IllegalArgumentException.class, () -> disciplina.alterarCargaHoraria(-5));
    }

    @Test
    void duasDisciplinasComMesmoCodigo_devemSerIguais() {
        // Arrange
        Disciplina d1 = new Disciplina("POO", "Nome A", 40);
        Disciplina d2 = new Disciplina("POO", "Nome B", 80);

        // Act / Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void duasDisciplinasComCodigosDiferentes_naoDevemSerIguais() {
        // Arrange
        Disciplina d1 = new Disciplina("POO", "Nome", 40);
        Disciplina d2 = new Disciplina("BD", "Nome", 40);

        // Act / Assert
        assertNotEquals(d1, d2);
    }
}