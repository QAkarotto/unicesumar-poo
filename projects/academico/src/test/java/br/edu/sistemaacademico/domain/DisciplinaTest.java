package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar uma disciplina válida")
    void deveCriarDisciplinaValida() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve rejeitar código nulo ou em branco")
    void deveRejeitarCodigoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "Nome", 40)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(" ", "Nome", 40)
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo ou em branco")
    void deveRejeitarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 40)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", " ", 40)
        );
    }

    @Test
    @DisplayName("Deve rejeitar carga horária menor ou igual a zero")
    void deveRejeitarCargaHorariaInvalida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Nome", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Nome", -10)
        );
    }

    @Test
    @DisplayName("Disciplinas com o mesmo código devem ser iguais")
    void deveConsiderarDisciplinasIguaisPeloCodigo() {
        var disciplina1 = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var disciplina2 = new Disciplina("POO", "Outro nome", 40);
        var disciplina3 = new Disciplina("BD", "Banco de Dados", 60);

        assertEquals(disciplina1, disciplina1);
        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
        assertNotEquals(disciplina1, disciplina3);
        assertNotEquals(disciplina1, null);
        assertFalse(disciplina1.equals("POO"));
    }

    @Test
    @DisplayName("O toString deve conter código, nome e carga horária")
    void toStringDeveConterCodigoNomeECargaHoraria() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertEquals("POO - Programação Orientada a Objetos (80h)", disciplina.toString());
    }
}
