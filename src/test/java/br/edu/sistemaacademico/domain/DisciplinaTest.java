package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina válida com os dados normalizados")
    void deveCriarDisciplinaValida() {
        var disciplina = new Disciplina("  POO  ", "  Programação Orientada a Objetos  ", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
        assertEquals("Programação Orientada a Objetos", disciplina.toString());
    }

    @Test
    @DisplayName("Deve rejeitar código em branco")
    void deveRejeitarCodigoEmBranco() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("   ", "Programação Orientada a Objetos", 80)
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo")
    void deveRejeitarNomeNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 80)
        );
    }

    @Test
    @DisplayName("Deve rejeitar carga horária não positiva")
    void deveRejeitarCargaHorariaNaoPositiva() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", -10)
        );
    }

    @Test
    @DisplayName("Deve considerar disciplinas iguais pelo código")
    void deveCompararDisciplinasPeloCodigo() {
        var primeira = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var segunda = new Disciplina("poo", "Outro Nome", 40);
        var terceira = new Disciplina("BD", "Banco de Dados", 80);

        assertTrue(primeira.equals(segunda));
        assertEquals(primeira.hashCode(), segunda.hashCode());
        assertFalse(primeira.equals(terceira));
        assertFalse(primeira.equals("POO"));
    }
}
