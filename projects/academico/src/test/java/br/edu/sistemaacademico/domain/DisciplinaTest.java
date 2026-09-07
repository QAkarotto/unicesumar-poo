package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    private Disciplina disciplina;

    @BeforeEach
    void setUp() {
        disciplina = new Disciplina("KAME101", "Kamehameha Mastery", 60);
    }

    @Test
    @DisplayName("Deve criar disciplina com dados validos")
    void deveCriarDisciplinaComSucesso() {
        assertEquals("KAME101", disciplina.getCodigo());
        assertEquals("Kamehameha Mastery", disciplina.getNome());
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Deve lancar excecao com dados invalidos no construtor")
    void deveValidarCamposObrigatoriosECargaHoraria() {
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("", "Kamehameha Mastery", 60));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("KAME101", "  ", 60));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("KAME101", "Kamehameha Mastery", 0));
        assertThrows(IllegalArgumentException.class, () -> new Disciplina("KAME101", "Kamehameha Mastery", -10));
    }

    @Test
    @DisplayName("Deve comparar disciplinas pelo codigo")
    void deveValidarEqualsEHashCode() {
        Disciplina disciplina2 = new Disciplina("KAME101", "Outro Nome", 80);
        Disciplina disciplinaDiferente = new Disciplina("GENKI102", "Genki Dama", 60);

        assertEquals(disciplina, disciplina2);
        assertEquals(disciplina.hashCode(), disciplina2.hashCode());
        assertNotEquals(disciplina, disciplinaDiferente);
        assertNotEquals(disciplina, "TextoQualquer");
    }

    @Test
    @DisplayName("Deve retornar toString formatado com carga horaria")
    void deveRetornarToString() {
        assertEquals("KAME101 - Kamehameha Mastery (60h)", disciplina.toString());
    }
}