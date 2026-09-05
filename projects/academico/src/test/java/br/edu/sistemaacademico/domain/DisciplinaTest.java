package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        // Arrange & Act
        Disciplina disciplina = new Disciplina("POO001", "Programação Orientada a Objetos", 80);

        // Assert
        assertEquals("POO001", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaSemCodigo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("", "Programação", 80));
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Programação", 80));
    }

    @Test
    void naoDeveCriarDisciplinaSemNome() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO001", "", 80));
    }

    @Test
    void naoDeveCriarDisciplinaComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO001", null, 80));
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO001", "Programação", 0));
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO001", "Programação", -10));
    }

    @Test
    void deveConsiderarDisciplinasComMesmoCodigoComoIguais() {
        // Arrange
        Disciplina d1 = new Disciplina("POO001", "Programação", 80);
        Disciplina d2 = new Disciplina("POO001", "Outro Nome", 60);

        // Act & Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void deveConsiderarDisciplinasComCodigosDiferentesComoDesiguais() {
        // Arrange
        Disciplina d1 = new Disciplina("POO001", "Programação", 80);
        Disciplina d2 = new Disciplina("BD001", "Programação", 80);

        // Act & Assert
        assertNotEquals(d1, d2);
    }

    @Test
    void deveRetornarToStringComCodigoNomeECargaHoraria() {
        // Arrange
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);

        // Act
        String resultado = disciplina.toString();

        // Assert
        assertTrue(resultado.contains("POO001"));
        assertTrue(resultado.contains("Programação"));
        assertTrue(resultado.contains("80"));
    }
}
