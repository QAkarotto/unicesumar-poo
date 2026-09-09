package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaComDadosValidos() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                60
        );
        assertEquals("POO", disciplina.getCodigo());
        assertEquals(
                "Programação Orientada a Objetos",
                disciplina.getNome()
        );
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    void deveRejeitarCodigoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(null, "POO", 60)
        );
    }

    @Test
    void deveRejeitarCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("   ", "POO", 60)
        );
    }

    @Test
    void deveRejeitarNomeNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 60)
        );
    }

    @Test
    void deveRejeitarNomeVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "   ", 60)
        );
    }

    @Test
    void deveRejeitarCargaHorariaZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", 0)
        );
    }

    @Test
    void deveRejeitarCargaHorariaNegativa() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação", -10)
        );
    }

    @Test
    void disciplinasComMesmoCodigoDevemSerIguais() {
        Disciplina primeira = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                60
        );

        Disciplina segunda = new Disciplina(
                "POO",
                "Outro Nome",
                80
        );

        assertEquals(primeira, segunda);
        assertEquals(primeira.hashCode(), segunda.hashCode());
    }

    @Test
    void disciplinasComCodigosDiferentesNaoDevemSerIguais() {
        Disciplina primeira = new Disciplina("POO", "Programação", 60);
        Disciplina segunda = new Disciplina("BD", "Banco de Dados", 60);

        assertNotEquals(primeira, segunda);
    }

    @Test
    void deveRepresentarDisciplinaComoTexto() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação",
                60
        );

        assertEquals(
                "POO - Programação (60h)",
                disciplina.toString()
        );
    }
}