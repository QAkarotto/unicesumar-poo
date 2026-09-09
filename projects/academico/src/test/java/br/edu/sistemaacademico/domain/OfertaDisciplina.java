package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private Aluno aluno(String ra) {
        return new Aluno(
                ra,
                "Aluno " + ra,
                ra + "@email.com"
        );
    }

    private Disciplina disciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    private Turma turma(String codigo) {
        return new Turma(
                codigo,
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                )
        );
    }

    @Test
    void deveCriarOfertaComDadosValidos() {
        Disciplina disciplina = disciplina();
        Turma turma = turma("T01");

        OfertaDisciplina oferta =
                new OfertaDisciplina(disciplina, turma);

        assertSame(disciplina, oferta.getDisciplina());
        assertSame(turma, oferta.getTurma());
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    void deveRejeitarDisciplinaNula() {
        Turma turma = turma("T01");

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(null, turma)
        );
    }

    @Test
    void deveRejeitarTurmaNula() {
        Disciplina disciplina = disciplina();

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(disciplina, null)
        );
    }

    @Test
    void deveRealizarMatricula() {
        Disciplina disciplina = disciplina();
        Turma turma = turma("T01");
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno = aluno("RA001");

        Matricula matricula = oferta.matricular(aluno);

        assertNotNull(matricula);
        assertEquals(
                "MAT-RA001-POO-T01",
                matricula.getCodigo()
        );

        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void deveRejeitarAlunoNuloNaMatricula() {
        OfertaDisciplina oferta =
                turma("T01").ofertarDisciplina(disciplina());

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        OfertaDisciplina oferta =
                turma("T01").ofertarDisciplina(disciplina());

        Aluno aluno = aluno("RA001");

        oferta.matricular(aluno);

        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void alunoReprovadoDevePoderSeMatricularNovamente() {
        Disciplina disciplina = disciplina();

        OfertaDisciplina ofertaAnterior =
                turma("T01").ofertarDisciplina(disciplina);

        Aluno aluno = aluno("RA001");

        Matricula primeira =
                ofertaAnterior.matricular(aluno);

        primeira.concluir(ResultadoAcademico.REPROVADO);

        OfertaDisciplina novaOferta =
                turma("T02").ofertarDisciplina(disciplina);

        Matricula segunda =
                novaOferta.matricular(aluno);

        assertNotNull(segunda);
        assertEquals(
                2,
                aluno.getMatriculas().size()
        );
        assertEquals(
                ResultadoAcademico.REPROVADO,
                primeira.getResultado()
        );
        assertNull(segunda.getResultado());
    }

    @Test
    void alunoAprovadoNaoDevePoderSeMatricularNovamente() {
        Disciplina disciplina = disciplina();

        OfertaDisciplina ofertaAnterior =
                turma("T01").ofertarDisciplina(disciplina);

        Aluno aluno = aluno("RA001");

        Matricula primeira =
                ofertaAnterior.matricular(aluno);

        primeira.concluir(ResultadoAcademico.APROVADO);

        OfertaDisciplina novaOferta =
                turma("T02").ofertarDisciplina(disciplina);

        assertThrows(
                IllegalStateException.class,
                () -> novaOferta.matricular(aluno)
        );

        assertEquals(1, aluno.getMatriculas().size());
        assertTrue(aluno.possuiAprovacaoEm(disciplina));
    }

    @Test
    void listaDeMatriculasNaoDevePermitirAlteracaoExterna() {
        OfertaDisciplina oferta =
                turma("T01").ofertarDisciplina(disciplina());

        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear()
        );
    }
}