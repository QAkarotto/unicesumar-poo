package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacoesDominioTest {

    // =========================
    // DISCIPLINA
    // =========================

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
    void naoDeveCriarDisciplinaComCargaHorariaInvalida() {
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
    void deveAlterarNomeDaDisciplina() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "POO",
                        80
                );

        disciplina.setNome(
                "Programação Orientada a Objetos"
        );

        assertEquals(
                "Programação Orientada a Objetos",
                disciplina.getNome()
        );
    }

    @Test
    void naoDeveAlterarNomeParaVazio() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> disciplina.setNome("")
        );
    }

    @Test
    void deveAlterarCargaHoraria() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        disciplina.setCargaHoraria(100);

        assertEquals(
                100,
                disciplina.getCargaHoraria()
        );
    }

    @Test
    void naoDeveAlterarCargaHorariaParaValorInvalido() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> disciplina.setCargaHoraria(0)
        );

        assertEquals(
                80,
                disciplina.getCargaHoraria()
        );
    }

    // =========================
    // PERIODO LETIVO
    // =========================

    @Test
    void naoDeveCriarPeriodoComAnoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        0,
                        Semestre.PRIMEIRO
                )
        );
    }

    @Test
    void naoDeveCriarPeriodoSemSemestre() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(
                        2026,
                        null
                )
        );
    }

    @Test
    void deveCriarPeriodoLetivoValido() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        assertEquals(
                2026,
                periodo.getAno()
        );

        assertEquals(
                Semestre.SEGUNDO,
                periodo.getSemestre()
        );
    }

    // =========================
    // TURMA
    // =========================

    @Test
    void naoDeveCriarTurmaSemCodigo() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("", periodo)
        );
    }

    @Test
    void naoDeveCriarTurmaSemPeriodoLetivo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "ADS4S",
                        null
                )
        );
    }

    // =========================
    // ALUNO
    // =========================

    @Test
    void naoDeveCriarAlunoSemIdentificadorAcademico() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "",
                        "João",
                        "joao@email.com"
                )
        );
    }

    @Test
    void naoDeveCriarAlunoSemNome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "",
                        "joao@email.com"
                )
        );
    }

    @Test
    void alunosComMesmoIdentificadorDevemSerIguais() {
        Aluno aluno1 =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        Aluno aluno2 =
                new Aluno(
                        "RA001",
                        "João Silva",
                        "joaosilva@email.com"
                );

        assertEquals(
                aluno1,
                aluno2
        );

        assertEquals(
                aluno1.hashCode(),
                aluno2.hashCode()
        );
    }

    // =========================
    // OFERTA DE DISCIPLINA
    // =========================

    @Test
    void naoDeveCriarOfertaSemTurma() {
        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(
                        null,
                        disciplina
                )
        );
    }

    @Test
    void naoDeveCriarOfertaSemDisciplina() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        Turma turma =
                new Turma(
                        "ADS4S",
                        periodo
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(
                        turma,
                        null
                )
        );
    }
}