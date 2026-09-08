package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    @DisplayName("Deve criar disciplina com dados válidos")
    void deveCriarDisciplina() {

        // Cria uma disciplina utilizando dados válidos
        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Confirma que os dados foram armazenados corretamente
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
    }

    @Test
    @DisplayName("Não deve permitir código vazio")
    void naoDevePermitirCodigoVazio() {

        // Tenta criar uma disciplina sem informar o código
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "",
                        "Programação Orientada a Objetos",
                        80
                )
        );
    }

    @Test
    @DisplayName("Não deve permitir nome vazio")
    void naoDevePermitirNomeVazio() {

        // Tenta criar uma disciplina sem informar o nome
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
    @DisplayName("Não deve permitir carga horária zero")
    void naoDevePermitirCargaHorariaZero() {

        // Tenta criar uma disciplina com carga horária igual a zero
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        0
                )
        );
    }

    @Test
    @DisplayName("Não deve permitir carga horária negativa")
    void naoDevePermitirCargaHorariaNegativa() {

        // Tenta criar uma disciplina com carga horária negativa
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        -40
                )
        );
    }

    @Test
    @DisplayName("Disciplinas com mesmo código devem ser iguais")
    void deveCompararDisciplinasPeloCodigo() {

        // Cria duas disciplinas com o mesmo código,
        // mas com outros dados diferentes
        var disciplina1 = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var disciplina2 = new Disciplina(
                "POO",
                "Outro Nome",
                40
        );

        // Confirma que o código é utilizado para identificar a disciplina
        assertEquals(disciplina1, disciplina2);

        // Objetos considerados iguais devem possuir o mesmo hashCode
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar texto da disciplina")
    void deveGerarToString() {

        // Cria uma disciplina para testar sua representação em texto
        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Confirma o formato utilizado pelo método toString()
        assertEquals(
                "POO - Programação Orientada a Objetos (80h)",
                disciplina.toString()
        );
    }
}