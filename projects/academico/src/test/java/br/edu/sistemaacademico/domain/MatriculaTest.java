package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    }

    private Disciplina criarDisciplina() {
        return new Disciplina("POO", "Programação Orientada a Objetos", 80);
    }

    private Aluno criarAluno(String ra) {
        return new Aluno(ra, "Aluno Teste", ra + "@email.com");
    }

    private OfertaDisciplina criarOferta(Disciplina disciplina) {
        Turma turma = new Turma("ESOFT4S-NB", criarPeriodo());
        return turma.ofertarDisciplina(disciplina);
    }

    @Test
    void deveCriarMatriculaAtiva() {
        Disciplina disciplina = criarDisciplina();
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta(disciplina);

        Matricula matricula = new Matricula(" MAT-001 ", aluno, oferta);

        assertEquals("MAT-001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(oferta.getTurma(), matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertTrue(aluno.getMatriculas().contains(matricula));
        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    void naoDeveAceitarDadosObrigatoriosInvalidos() {
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta(criarDisciplina());

        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(null, aluno, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(" ", aluno, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-002", null, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-003", aluno, (OfertaDisciplina) null));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-004", aluno, (Turma) null));
    }

    @Test
    void deveConcluirMatriculaComResultado() {
        Matricula matricula = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void naoDeveConcluirSemResultado() {
        Matricula matricula = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));

        assertThrows(IllegalArgumentException.class,
                () -> matricula.concluir(null));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    void deveTrancarMatriculaAtiva() {
        Matricula matricula = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    void deveCancelarMatriculaAtiva() {
        Matricula matricula = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    void naoDeveAlterarMatriculaQueJaFoiFinalizada() {
        Matricula concluida = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));
        concluida.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, concluida::trancar);
        assertThrows(IllegalStateException.class, concluida::cancelar);
        assertThrows(IllegalStateException.class,
                () -> concluida.concluir(ResultadoAcademico.REPROVADO));

        Matricula trancada = new Matricula(
                "MAT-002", criarAluno("RA002"), criarOferta(criarDisciplina()));
        trancada.trancar();

        assertThrows(IllegalStateException.class, trancada::cancelar);

        Matricula cancelada = new Matricula(
                "MAT-003", criarAluno("RA003"), criarOferta(criarDisciplina()));
        cancelada.cancelar();

        assertThrows(IllegalStateException.class, cancelada::trancar);
    }

    @Test
    void toStringDeveConterInformacoesDaMatricula() {
        Matricula matricula = new Matricula(
                "MAT-001", criarAluno("RA001"), criarOferta(criarDisciplina()));

        String texto = matricula.toString();

        assertTrue(texto.contains("MAT-001"));
        assertTrue(texto.contains("RA001"));
        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("ATIVA"));
    }
}
