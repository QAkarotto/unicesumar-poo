package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void disciplinasComMesmoCodigoDevemSerConsideradasIguais() {

        // Arrange
        var disciplina1 = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var disciplina2 = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Act + Assert
        assertEquals(disciplina1, disciplina2);
    }

    @Test
    void naoDevePermitirCargaHorariaInvalida() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        0
                )
        );
    }
}