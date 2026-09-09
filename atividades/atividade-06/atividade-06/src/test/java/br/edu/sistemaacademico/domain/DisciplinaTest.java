package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
class DisciplinaTest {

    @Test
    void testCriarDisciplinaComDadosValidos() {
        Disciplina disciplina = new Disciplina("CS101", "Estruturas de Dados", 60);

        assertEquals("CS101", disciplina.getCodigo());
        assertEquals("Estruturas de Dados", disciplina.getNome());
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    
    void testNaoCriarComCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "Matemática", 45));
    }

    @Test
    
    void testNaoCriarComCodigoBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("   ", "Matemática", 45));
    }

    @Test
    
    void testNaoCriarComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("MATH101", null, 50));
    }

    @Test
    
    void testNaoCriarComNomeBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("MATH102", "   ", 50));
    }

    @Test
    
    void testNaoCriarComCargaZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("PHYS101", "Física", 0));
    }

    @Test
    
    void testNaoCriarComCargaNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("ENG101", "Engenharia", -30));
    }

    @Test
    
    void testAceitarCargaMinimaValida() {
        Disciplina disciplina = new Disciplina("HIST101", "História", 1);

        assertEquals(1, disciplina.getCargaHoraria());
    }

    @Test
    
    void testAceitarCargaGrande() {
        Disciplina disciplina = new Disciplina("PROG101", "Programação", 200);

        assertEquals(200, disciplina.getCargaHoraria());
    }

    @Test
    
    void testEqualsComMesmoCodigo() {
        Disciplina disciplina1 = new Disciplina("BIO101", "Biologia Molecular", 80);
        Disciplina disciplina2 = new Disciplina("BIO101", "Biologia Básica", 40);

        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1, disciplina1);
    }

    @Test
    
    void testNotEqualsComCodigosDiferentes() {
        Disciplina disciplina1 = new Disciplina("CHEM101", "Química", 75);
        Disciplina disciplina2 = new Disciplina("CHEM102", "Química Orgânica", 75);

        assertNotEquals(disciplina1, disciplina2);
    }

    @Test
    
    void testNotEqualsComOutroTipo() {
        Disciplina disciplina = new Disciplina("ART101", "Artes", 40);

        assertNotEquals(disciplina, "ART101");
        assertNotEquals(disciplina, 101);
        assertNotEquals(disciplina, null);
    }

    @Test
    
    void testHashCodeComMesmoCodigo() {
        Disciplina disciplina1 = new Disciplina("MUS101", "Música", 30);
        Disciplina disciplina2 = new Disciplina("MUS101", "Fundamentos da Música", 60);

        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
    }

    @Test
    
    void testToStringContemDados() {
        Disciplina disciplina = new Disciplina("GEO101", "Geografia", 55);

        String texto = disciplina.toString();

        assertTrue(texto.contains("GEO101"));
        assertTrue(texto.contains("Geografia"));
        assertTrue(texto.contains("55"));
    }

    @Test
    
    void testGetCodigo() {
        Disciplina disciplina = new Disciplina("LANG101", "Inglês", 72);

        assertEquals("LANG101", disciplina.getCodigo());
    }

    @Test
    
    void testGetNome() {
        Disciplina disciplina = new Disciplina("LIT101", "Literatura", 68);

        assertEquals("Literatura", disciplina.getNome());
    }
}
