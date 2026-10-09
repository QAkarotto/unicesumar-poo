package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaTest {

    @Test
    @DisplayName("Deve normalizar os dados e formatar a disciplina")
    void deveNormalizarDados() {
        var disciplina = new Disciplina(" POO ", " Programação Orientada a Objetos ", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
        assertEquals("POO - Programação Orientada a Objetos (80h)", disciplina.toString());
    }

    @Test
    @DisplayName("Deve rejeitar código ou nome vazio")
    void deveRejeitarCodigoOuNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Programação", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(" ", "Programação", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", " ", 80));
    }

    @Test
    @DisplayName("A carga horária deve ser positiva")
    void deveRejeitarCargaHorariaNaoPositiva() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", -1));
    }

    @Test
    @DisplayName("A igualdade da disciplina deve usar o código")
    void deveCompararDisciplinasPeloCodigo() {
        var disciplina = new Disciplina("POO", "Programação", 80);
        var mesmaDisciplina = new Disciplina("POO", "Nome diferente", 40);
        var outraDisciplina = new Disciplina("BD", "Banco de Dados", 80);

        assertTrue(disciplina.equals(disciplina));
        assertEquals(disciplina, mesmaDisciplina);
        assertEquals(disciplina.hashCode(), mesmaDisciplina.hashCode());
        assertNotEquals(disciplina, outraDisciplina);
        assertNotEquals(disciplina, null);
        assertNotEquals(disciplina, "POO");
    }
}
