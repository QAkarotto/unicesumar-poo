package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidacoesDominioTest {

    @Test
    @DisplayName("Disciplina não deve ser criada em estado inválido")
    void deveValidarDadosDaDisciplina() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(" ", "POO", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", " ", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", "POO", 0));
    }

    @Test
    @DisplayName("Período letivo deve possuir ano válido e semestre")
    void deveValidarPeriodoLetivo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(1899, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));

        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        assertEquals("2026/2", periodo.toString());
    }

    @Test
    @DisplayName("Turma deve possuir código e período letivo")
    void deveValidarDadosDaTurma() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma(" ", new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("ESOFT4S", null));
    }

    @Test
    @DisplayName("Oferta deve possuir turma e disciplina")
    void deveValidarDadosDaOferta() {
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var disciplina = new Disciplina("POO", "POO", 80);

        assertThrows(IllegalArgumentException.class,
                () -> new OfertaDisciplina(null, disciplina));
        assertThrows(IllegalArgumentException.class,
                () -> new OfertaDisciplina(turma, null));
    }

    @Test
    @DisplayName("Matrícula deve possuir código, aluno e oferta")
    void deveValidarDadosDaMatricula() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));

        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(" ", aluno, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-001", aluno, null));
    }
    @Test
    @DisplayName("Valores nulos também devem ser rejeitados nos construtores")
    void deveRejeitarValoresNulos() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Ana", "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina(null, "POO", 80));
        assertThrows(IllegalArgumentException.class,
                () -> new Disciplina("POO", null, 80));
    }

    @Test
    @DisplayName("Semestres devem fornecer seus números corretamente")
    void deveInformarNumeroDoSemestre() {
        assertEquals(1, Semestre.PRIMEIRO.getNumero());
        assertEquals(2, Semestre.SEGUNDO.getNumero());
        assertTrue(ResultadoAcademico.APROVADO.isAprovado());
        assertFalse(ResultadoAcademico.REPROVADO.isAprovado());
    }

}
