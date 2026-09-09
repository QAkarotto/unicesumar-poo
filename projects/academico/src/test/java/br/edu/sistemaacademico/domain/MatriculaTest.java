package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno criarAluno() {
        return new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );
    }

    private OfertaDisciplina criarOferta() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma = new Turma(
                "T01",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        return turma.ofertarDisciplina(disciplina);
    }

    @Test
    void deveCriarMatriculaComDadosValidos() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = new Matricula(
                "MAT001",
                aluno,
                oferta
        );

        assertEquals("MAT001", matricula.getCodigo());
        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());
        assertSame(oferta.getDisciplina(), matricula.getDisciplina());
        assertSame(oferta.getTurma(), matricula.getTurma());
        assertNull(matricula.getResultado());
    }

    @Test
    void deveRejeitarCodigoNuloOuVazio() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, aluno, oferta)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("", aluno, oferta)
        );
    }

    @Test
    void deveRejeitarAlunoNulo() {
        OfertaDisciplina oferta = criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT001", null, oferta)
        );
    }

    @Test
    void deveRejeitarOfertaNula() {
        Aluno aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT001", aluno, null)
        );
    }

    @Test
    void devePermitirAprovacao() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = new Matricula(
                "MAT001",
                aluno,
                oferta
        );

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void devePermitirReprovacao() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = new Matricula(
                "MAT001",
                aluno,
                oferta
        );

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void deveRejeitarResultadoNulo() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = new Matricula(
                "MAT001",
                aluno,
                oferta
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertNull(matricula.getResultado());
    }

    @Test
    void naoDevePermitirConcluirMatriculaDuasVezes() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula = new Matricula(
                "MAT001",
                aluno,
                oferta
        );

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
}