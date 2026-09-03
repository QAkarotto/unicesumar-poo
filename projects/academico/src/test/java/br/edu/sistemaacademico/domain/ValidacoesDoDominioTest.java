package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacoesDoDominioTest {

    @Test
    void naoDeveCriarAlunoComDadosObrigatoriosInvalidos() {

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
                () -> new Aluno("RA2026001", "Paola Oliveira", "email-invalido")
        );
    }

    @Test
    void naoDeveCriarDisciplinaComDadosInvalidos() {

        // Ação e verificação do resultado
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
    void naoDeveCriarPeriodoLetivoInvalido() {

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    void naoDevePermitirOperacoesComDadosObrigatoriosNulos() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, oferta)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(aluno, null)
        );
    }
}