package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno aluno;
    private OfertaDisciplina oferta;
    private Disciplina disciplina;
    private Turma turma;

    @BeforeEach
    void setUp() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        disciplina = new Disciplina("DBZ101", "Ki Control", 60);
        turma = new Turma("T-GOKU", periodo);
        oferta = new OfertaDisciplina(turma, disciplina);
        aluno = new Aluno("RA8000", "Son Goku", "goku@capsulecorp.com");
    }

    @Test
    @DisplayName("Deve criar matricula com sucesso e vincular ao aluno e oferta")
    void deveCriarMatriculaComSucesso() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        assertEquals("MAT-8000", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve criar matricula utilizando construtor com Turma de oferta unica")
    void deveCriarMatriculaComTurma() {
        Turma turmaUnica = new Turma("T-VEGETA", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Matricula matricula = new Matricula("MAT-PRINCE", aluno, turmaUnica);

        assertNotNull(matricula);
        assertEquals("MAT-PRINCE", matricula.getCodigo());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar criar matricula com dados nulos ou em branco")
    void deveValidarCamposObrigatoriosNaCriacao() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula("", aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-1", null, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-1", aluno, (OfertaDisciplina) null));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-1", aluno, (Turma) null));
    }

    @Test
    @DisplayName("Deve concluir matricula com resultado academico")
    void deveConcluirMatricula() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertTrue(matricula.foiAprovadoEm(disciplina));
    }

    @Test
    @DisplayName("Deve lancar excecao ao concluir com resultado nulo")
    void deveValidarResultadoNuloNaConclusao() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    @DisplayName("Deve trancar matricula ativa")
    void deveTrancarMatricula() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar matricula ativa")
    void deveCancelarMatricula() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve lancar excecao ao alterar estado de matricula nao ativa")
    void deveLancarExcecaoAoAlterarMatriculaNaoAtiva() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);
        matricula.trancar();

        assertThrows(IllegalStateException.class, () -> matricula.trancar());
        assertThrows(IllegalStateException.class, () -> matricula.cancelar());
        assertThrows(IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.APROVADO));
    }

    @Test
    @DisplayName("Deve retornar toString formatado")
    void deveRetornarToString() {
        Matricula matricula = new Matricula("MAT-8000", aluno, oferta);

        assertNotNull(matricula.toString());
    }
}