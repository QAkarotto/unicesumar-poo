package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        // Arrange
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        // Act
        String codigo = disciplina.getCodigo();

        // Assert
        assertEquals("POO", codigo);
        assertEquals("Programacao Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaSemCodigo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "",
                        "Programacao Orientada a Objetos",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaSemNome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programacao Orientada a Objetos",
                        0
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaNegativa() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programacao Orientada a Objetos",
                        -10
                )
        );
    }
}