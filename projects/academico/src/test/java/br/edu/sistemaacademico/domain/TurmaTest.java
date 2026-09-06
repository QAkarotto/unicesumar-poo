package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }

    @Test
    void deveCriarTurmaComDadosValidos() {

        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        assertEquals(
                "ESOFT4S-NB",
                turma.getCodigo()
        );

        assertEquals(
                "2026/1",
                turma.getPeriodoLetivo().toString()
        );
    }

    @Test
    void naoDeveCriarTurmaSemCodigo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "",
                        criarPeriodo()
                )
        );
    }

    @Test
    void naoDeveCriarTurmaSemPeriodo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "TURMA-A",
                        null
                )
        );
    }

    @Test
    void devePermitirVariasDisciplinasNaMesmaTurma() {

        Turma turma =
                new Turma(
                        "TURMA-A",
                        criarPeriodo()
                );

        Disciplina poo =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina banco =
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                );

        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(banco);

        assertEquals(
                2,
                turma.getOfertas().size()
        );
    }

    @Test
    void naoDeveOfertarDisciplinaDuplicada() {

        Turma turma =
                new Turma(
                        "TURMA-A",
                        criarPeriodo()
                );

        Disciplina poo =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        turma.ofertarDisciplina(poo);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {

        Turma turma =
                new Turma(
                        "TURMA-A",
                        criarPeriodo()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDevePermitirAlterarOfertasDiretamente() {

        Turma turma =
                new Turma(
                        "TURMA-A",
                        criarPeriodo()
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
    }
}