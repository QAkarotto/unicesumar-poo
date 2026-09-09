package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina válida e expor os dados informados")
    void deveCriarDisciplinaValida() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve remover espaços em branco do código e do nome")
    void deveRemoverEspacosDoCodigoENome() {
        var disciplina = new Disciplina("  POO  ", "  Programação  ", 40);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação", disciplina.getNome());
    }

    @Test
    @DisplayName("Não deve aceitar código nulo ou vazio")
    void naoDeveAceitarCodigoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "Programação", 40)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("   ", "Programação", 40)
        );
    }

    @Test
    @DisplayName("Não deve aceitar nome vazio")
    void naoDeveAceitarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "  ", 40)
        );
    }

    @Test
    @DisplayName("Não deve aceitar carga horária menor ou igual a zero")
    void naoDeveAceitarCargaHorariaNaoPositiva() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", -10)
        );
    }

    @Test
    @DisplayName("Duas disciplinas com o mesmo código devem ser iguais")
    void deveCompararDisciplinasPeloCodigo() {
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var pooRepetida = new Disciplina("POO", "Outra descrição", 40);
        var bd = new Disciplina("BD", "Banco de Dados", 80);

        assertEquals(poo, pooRepetida);
        assertEquals(poo.hashCode(), pooRepetida.hashCode());
        assertNotEquals(poo, bd);
        assertTrue(poo.equals(poo));
        assertFalse(poo.equals("POO"));
    }

    @Test
    @DisplayName("toString deve trazer código, nome e carga horária")
    void toStringDeveConterDadosPrincipais() {
        var disciplina = new Disciplina("POO", "Programação", 80);

        assertEquals("POO - Programação (80h)", disciplina.toString());
    }
}
