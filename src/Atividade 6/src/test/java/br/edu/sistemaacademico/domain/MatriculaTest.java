package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    private Matricula matricula;

    @BeforeEach
    void setUp() {
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Aluno aluno = new Aluno("RA001", "Lucas", "lucas@email.com");
        matricula = oferta.matricular("M001", aluno);
    }

    @Test
    void deveIniciarSemResultado() {
        assertNull(matricula.getResultado());
    }

    @Test
    void deveRegistrarResultadoAprovado() {
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void naoDeveRegistrarResultadoNulo() {
        assertThrows(IllegalArgumentException.class, () -> matricula.registrarResultado(null));
    }

    @Test
    void naoDeveRegistrarResultadoDuasVezes() {
        matricula.registrarResultado(ResultadoAcademico.REPROVADO);

        assertThrows(IllegalStateException.class, () -> matricula.registrarResultado(ResultadoAcademico.APROVADO));
    }
}
