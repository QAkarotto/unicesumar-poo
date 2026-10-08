package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina com dados válidos")
    void deveCriarDisciplinaValida() {
        //Arrange e Act
        var disciplina = new Disciplina("POO", "Programacao", 80);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programacao", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve rejeitar disciplina com dados inválidos")
    void deveRejeitarDisciplinaInvalida() {
        //Act Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Programacao", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programacao", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programacao", -10));
    }

    @Test
    @DisplayName("Deve comparar disciplinas pelo codigo")
    void deveCompararDisciplinasPeloCodigo() {
        // Arrange
        var disciplinaPrincipal = new Disciplina("POO", "Programacao", 80);
        var disciplinaMesmoCodigo = new Disciplina("POO", "Nome alternativo", 40);
        var disciplinaOutra = new Disciplina("BD", "Banco de Dados", 80);

        // Assert
        assertEquals(disciplinaPrincipal, disciplinaMesmoCodigo);
        assertEquals(disciplinaPrincipal.hashCode(), disciplinaMesmoCodigo.hashCode());
        assertNotEquals(disciplinaPrincipal, disciplinaOutra);
    }
}
