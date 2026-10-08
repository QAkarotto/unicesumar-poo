package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        assertEquals("POO01", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO02", "Estrutura de Dados", 0));
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina(" ", "Estrutura de Dados", 40));
    }

    @Test
    void naoDeveCriarDisciplinaComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("POO03", " ", 40));
    }

    @Test
    void deveConsiderarDisciplinasComMesmoCodigoComoIguais() {
        Disciplina disciplina1 = new Disciplina("POO04", "Nome A", 40);
        Disciplina disciplina2 = new Disciplina("POO04", "Nome B", 60);

        assertEquals(disciplina1, disciplina2);
    }
}
