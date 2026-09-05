package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        assertEquals("POO", disciplina.getCodigo());
        assertEquals(
                "Programação Orientada a Objetos",
                disciplina.getNome()
        );
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void deveRejeitarCodigoNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "POO", 80)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("", "POO", 80)
        );
    }

    @Test
    void deveRejeitarNomeNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 80)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "", 80)
        );
    }

    @Test
    void deveRejeitarCargaHorariaNaoPositiva() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "POO", 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "POO", -1)
        );
    }
}