package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaValida() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void deveLancarExcecaoQuandoCodigoForNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina(null, "Nome", 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("  ", "Nome", 80));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("COD", null, 80));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("COD", "  ", 80));
    }

    @Test
    void deveLancarExcecaoQuandoCargaHorariaNaoForPositiva() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("COD", "Nome", 0));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("COD", "Nome", -10));
    }

    @Test
    void toStringDeveConterNomeCodigoECargaHoraria() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var texto = disciplina.toString();

        assertTrue(texto.contains("Programação Orientada a Objetos"));
        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("80"));
    }
}