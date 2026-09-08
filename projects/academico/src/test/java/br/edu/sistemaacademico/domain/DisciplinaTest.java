package br.edu.sistemaacademico.domain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        assertEquals("POO", disciplina.getCodigo());
        assertEquals(
                "Programação Orientada a Objetos",
                disciplina.getNome()
        );
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void naoDeveCriarDisciplinaSemCodigo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "",
                        "Programação",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaSemNome() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaZero() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programação",
                        0
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComCargaHorariaNegativa() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programação",
                        -10
                )
        );
    }

    @Test
    void devePossuirToString() {

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação",
                80
        );

        String texto = disciplina.toString();

        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("Programação"));
        assertTrue(texto.contains("80"));
    }
}//deucerto