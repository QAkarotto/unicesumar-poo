package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina com os atributos corretos")
    void deveCriarDisciplinaCorretamente() {
        var disciplina = new Disciplina("POO001", "Programação Orientada a Objetos", 80);

        assertEquals("POO001", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }
}