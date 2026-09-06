package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar uma disciplina valida")
    void deveCriarUmaDisciplinaValida() {
        // Arrange / Act
        Disciplina disciplina = new Disciplina("POO", "Programacao Orientada a Objetos", 80);

        // Assert
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programacao Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Nao deve criar disciplina sem codigo")
    void naoDeveCriarDisciplinaSemCodigo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina("   ", "Programacao Orientada a Objetos", 80);
        });
    }

    @Test
    @DisplayName("Nao deve criar disciplina sem nome")
    void naoDeveCriarDisciplinaSemNome() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina("POO", null, 80);
        });
    }

    @Test
    @DisplayName("Nao deve criar disciplina com carga horaria zerada")
    void naoDeveCriarDisciplinaComCargaHorariaZerada() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina("POO", "Programacao Orientada a Objetos", 0);
        });
    }

    @Test
    @DisplayName("Nao deve criar disciplina com carga horaria negativa")
    void naoDeveCriarDisciplinaComCargaHorariaNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Disciplina("POO", "Programacao Orientada a Objetos", -40);
        });
    }

    @Test
    @DisplayName("Duas disciplinas com o mesmo codigo sao iguais")
    void duasDisciplinasComOMesmoCodigoSaoIguais() {
        // Arrange
        Disciplina poo = new Disciplina("POO", "Programacao Orientada a Objetos", 80);
        Disciplina mesmoCodigo = new Disciplina("POO", "POO Avancada", 40);
        Disciplina bancoDeDados = new Disciplina("BD", "Banco de Dados", 60);

        // Assert
        assertEquals(poo, mesmoCodigo);
        assertEquals(poo.hashCode(), mesmoCodigo.hashCode());
        assertFalse(poo.equals(bancoDeDados));
        assertFalse(poo.equals("POO"));
        assertEquals("POO - Programacao Orientada a Objetos (80h)", poo.toString());
    }
}
