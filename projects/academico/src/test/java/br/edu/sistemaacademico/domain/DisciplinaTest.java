package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    @Test
    void deveCriarDisciplinaValida() {
        Disciplina disciplina =
                new Disciplina(
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
                        "Programação Orientada a Objetos",
                        80
                )
        );
    }

    @Test
    void naoDeveCriarDisciplinaComCodigoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        null,
                        "Programação Orientada a Objetos",
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
    void naoDeveCriarDisciplinaComNomeNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(
                        "POO",
                        null,
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
                        "Programação Orientada a Objetos",
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
                        "Programação Orientada a Objetos",
                        -10
                )
        );
    }

    @Test
    void deveRemoverEspacosDoCodigo() {
        Disciplina disciplina =
                new Disciplina(
                        "  POO  ",
                        "Programação Orientada a Objetos",
                        80
                );

        assertEquals("POO", disciplina.getCodigo());
    }

    @Test
    void deveRemoverEspacosDoNome() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "  Programação Orientada a Objetos  ",
                        80
                );

        assertEquals(
                "Programação Orientada a Objetos",
                disciplina.getNome()
        );
    }

    @Test
    void disciplinasComMesmoCodigoDevemSerIguais() {
        Disciplina disciplina1 =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina disciplina2 =
                new Disciplina(
                        "POO",
                        "Outro Nome",
                        120
                );

        assertEquals(disciplina1, disciplina2);
    }

    @Test
    void disciplinasComCodigosDiferentesNaoDevemSerIguais() {
        Disciplina disciplina1 =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina disciplina2 =
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                );

        assertNotEquals(disciplina1, disciplina2);
    }

    @Test
    void disciplinaNaoDeveSerIgualANulo() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertNotEquals(disciplina, null);
    }

    @Test
    void disciplinaNaoDeveSerIgualAOutroTipoDeObjeto() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertNotEquals(disciplina, "POO");
    }

    @Test
    void devePossuirMesmoHashCodeParaMesmoCodigo() {
        Disciplina disciplina1 =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina disciplina2 =
                new Disciplina(
                        "POO",
                        "Outro Nome",
                        120
                );

        assertEquals(
                disciplina1.hashCode(),
                disciplina2.hashCode()
        );
    }

    @Test
    void deveRetornarToStringCorreto() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertEquals(
                "POO - Programação Orientada a Objetos (80h)",
                disciplina.toString()
        );
    }
}