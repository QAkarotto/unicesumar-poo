package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    void deveConcluirMatriculaComAprovacao() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Verificação do resultado
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void naoDevePermitirConcluirMatriculaSemResultado() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    void naoDevePermitirConcluirAMesmaMatriculaDuasVezes() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    private Matricula criarMatriculaEmAndamento() {

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

        return oferta.matricular(aluno);
    }
}