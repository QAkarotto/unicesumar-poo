package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Matricula criarMatricula() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        return oferta.matricular(aluno);
    }

    @Test
    void novaMatriculaDeveIniciarSemResultado() {
        Matricula matricula = criarMatricula();

        assertNull(matricula.getResultado());
        assertFalse(matricula.foiAprovada());
        assertFalse(matricula.foiReprovada());
    }

    @Test
    void deveConcluirMatriculaComoAprovada() {
        Matricula matricula = criarMatricula();

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );

        assertTrue(matricula.foiAprovada());
        assertFalse(matricula.foiReprovada());
    }

    @Test
    void deveConcluirMatriculaComoReprovada() {
        Matricula matricula = criarMatricula();

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );

        assertTrue(matricula.foiReprovada());
        assertFalse(matricula.foiAprovada());
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        Matricula matricula = criarMatricula();

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertNull(matricula.getResultado());
    }

    @Test
    void naoDeveConcluirMatriculaDuasVezes() {
        Matricula matricula = criarMatricula();

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void naoDeveCriarMatriculaSemAluno() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, oferta)
        );
    }

    @Test
    void naoDeveCriarMatriculaSemOferta() {
        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(aluno, null)
        );
    }
}