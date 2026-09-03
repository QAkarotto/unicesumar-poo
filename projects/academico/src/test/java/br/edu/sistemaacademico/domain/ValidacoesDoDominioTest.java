package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacoesDoDominioTest {

    @Test
    void naoDeveCriarAlunoOuDisciplinaComDadosInvalidos() {

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "Paola Oliveira", "paola@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "", "paola@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA2026001",
                        "Paola Oliveira",
                        "email-invalido"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("", "Programação Orientada a Objetos", 80)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "", 80)
        );

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
    void naoDeveCriarPeriodoOuTurmaComDadosInvalidos() {

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "",
                        new PeriodoLetivo(2026, Semestre.PRIMEIRO)
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    void naoDeveCriarMatriculaComDadosObrigatoriosInvalidos() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("", aluno, oferta)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, oferta)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "MAT-001",
                        aluno,
                        (OfertaDisciplina) null
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaPorTurmaSemUmaOfertaUnica() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turmaSemOferta = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-001", aluno, turmaSemOferta)
        );
    }
}