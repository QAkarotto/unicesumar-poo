package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina válida")
    void deveCriarDisciplinaValida() {
        // Arrange
        var codigo = "POO";
        var nome = "Programação Orientada a Objetos";
        var cargaHoraria = 80;

        // Act
        var disciplina = new Disciplina(codigo, nome, cargaHoraria);

        // Assert
        assertEquals(codigo, disciplina.getCodigo());
        assertEquals(nome, disciplina.getNome());
        assertEquals(cargaHoraria, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve considerar disciplinas com códigos diferentes como diferentes")
    void deveConsiderarDisciplinasComCodigosDiferentesComoDiferentes() {
        // Arrange
        var disciplina1 = new Disciplina( "POO", "Programação Orientada a Objetos", 80 );
        var disciplina2 = new Disciplina( "BD", "Banco de Dados", 60 );

        // Act
        var diferentes = disciplina1.equals(disciplina2);

        // Assert
        assertFalse(diferentes);
    }
}
