package br.edu.sistemaacademico.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DisciplinaTest {

    @Test
    void deveCriarDisciplina() {
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        assertEquals("POO", d.getCodigo());
        assertEquals("Programação OO", d.getNome());
        assertEquals(80, d.getCargaHoraria());
    }

    @Test
    void deveIgualarPorCodigo() {
        Disciplina d1 = new Disciplina("POO", "Programação OO", 80);
        Disciplina d2 = new Disciplina("POO", "Outro Nome", 100);
        assertEquals(d1, d2);
    }

    @Test
    void deveSerDiferenteSeCodigoDiferente() {
        Disciplina d1 = new Disciplina("POO", "Programação OO", 80);
        Disciplina d2 = new Disciplina("BD", "Banco de Dados", 80);
        assertNotEquals(d1, d2);
    }
}
