package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaValida() {

        String codigo = "POO";
        String nome = "Programação Orientada a Objetos";
        int cargaHoraria = 80;

        Aluno.Disciplina disciplina =
                new Aluno.Disciplina(codigo, nome, cargaHoraria);

        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    void deveRejeitarCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno.Disciplina("", "Programação", 80)
        );
    }

    @Test
    void deveRejeitarNomeVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno.Disciplina("POO", "", 80)
        );
    }

    @Test
    void deveRejeitarCargaHorariaInvalida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno.Disciplina("POO", "Programação", 0)
        );
    }

    @Test
    void deveCompararDisciplinasComMesmoCodigo() {
        // Arrange
        Aluno.Disciplina disciplina1 =
                new Aluno.Disciplina("POO", "Programação", 80);

        Aluno.Disciplina disciplina2 =
                new Aluno.Disciplina("POO", "Programação Orientada", 100);

        // Assert
        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
    }


    void deveGerarTextoDaDisciplina() {
        // Arrange
        Aluno.Disciplina disciplina =
                new Aluno.Disciplina("POO", "Programação", 80);

        // Act
        String resultado = disciplina.toString();

        // Assert
        assertTrue(resultado.contains("POO"));
        assertTrue(resultado.contains("Programação"));
        assertTrue(resultado.contains("80"));
    }
}