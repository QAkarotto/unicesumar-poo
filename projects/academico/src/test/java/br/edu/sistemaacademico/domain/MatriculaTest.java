package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    @DisplayName("Deve concluir matrícula como aprovada")
    void deveConcluirMatriculaComoAprovada() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
        var matricula = oferta.matricular("MAT-001", aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve concluir matrícula como reprovada")
    void deveConcluirMatriculaComoReprovada() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
        var matricula = oferta.matricular("MAT-001", aluno);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir concluir novamente uma matrícula concluída")
    void deveImpedirConclusaoDuplicada() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
        var matricula = oferta.matricular("MAT-001", aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }
}
