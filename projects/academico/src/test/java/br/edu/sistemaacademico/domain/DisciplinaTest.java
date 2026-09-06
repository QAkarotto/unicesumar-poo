package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaValida() {
        // Arrange + Act
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Assert
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoVazio() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "",
                        "Programação Orientada a Objetos",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComNomeVazio() {
        // Act + Assert
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

    @Test
    void disciplinasComMesmoCodigoDevemSerIguais() {
        // Arrange
        Disciplina disciplina1 = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina disciplina2 = new Disciplina(
                "POO",
                "Outra descrição",
                40
        );

        // Act + Assert
        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());

        // Vegeta também perceberia que o código identifica a disciplina.
    }

    @Test
    void disciplinasComCodigosDiferentesNaoDevemSerIguais() {
        // Arrange
        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina bd = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        // Act + Assert
        assertNotEquals(poo, bd);
    }

    @Test
    void disciplinaNaoDeveSerIgualAOutroTipoDeObjeto() {
        // Arrange
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Act + Assert
        assertNotEquals(disciplina, "POO");
    }
}