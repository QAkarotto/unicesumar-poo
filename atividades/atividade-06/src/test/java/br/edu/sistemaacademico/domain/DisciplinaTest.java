package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void deveConsiderarIguaisDisciplinasComMesmoCodigo() {
        Disciplina disciplina1 = new Disciplina("POO04", "Programação", 40);
        Disciplina disciplina2 = new Disciplina("POO04", "Programação Avançada", 60);

        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
        assertEquals(disciplina1, disciplina1);
    }

    @Test
    void naoDeveConsiderarIguaisDisciplinasComCodigoDiferente() {
        Disciplina disciplina1 = new Disciplina("POO05", "Programação", 40);
        Disciplina disciplina2 = new Disciplina("POO06", "Programação", 40);

        assertNotEquals(disciplina1, disciplina2);
        assertNotEquals(disciplina1, "POO05");
    }

    @Test
    void representacaoTextualContemDadosDaDisciplina() {
        Disciplina disciplina = new Disciplina("POO07", "Programação", 40);

        String texto = disciplina.toString();

        assertTrue(texto.contains("POO07"));
        assertTrue(texto.contains("Programação"));
        assertTrue(texto.contains("40"));
    }
}
