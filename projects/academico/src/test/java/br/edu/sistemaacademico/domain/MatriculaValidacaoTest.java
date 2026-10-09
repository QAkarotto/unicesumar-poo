package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaValidacaoTest {

    @Test
    @DisplayName("Deve validar os argumentos obrigatórios da matrícula")
    void deveValidarArgumentosObrigatorios() {
        var aluno = novoAluno();
        var oferta = novaOferta("POO");

        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(null, aluno, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("  ", aluno, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, oferta));
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-001", aluno, (OfertaDisciplina) null));
    }

    @Test
    @DisplayName("Deve permitir trancar matrícula ativa")
    void deveTrancarMatriculaAtiva() {
        var matricula = novaMatricula("MAT-001", novoAluno(), novaOferta("POO"));

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertThrows(IllegalStateException.class, matricula::trancar);
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    @DisplayName("Deve permitir cancelar matrícula ativa")
    void deveCancelarMatriculaAtiva() {
        var matricula = novaMatricula("MAT-001", novoAluno(), novaOferta("POO"));

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertThrows(IllegalStateException.class, matricula::trancar);
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO));
    }

    @Test
    @DisplayName("Não deve concluir matrícula sem resultado")
    void deveRejeitarResultadoNulo() {
        var matricula = novaMatricula("MAT-001", novoAluno(), novaOferta("POO"));

        assertNull(matricula.getResultado());
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve expor os dados principais e formatar matrícula")
    void deveExporDadosDaMatricula() {
        var aluno = novoAluno();
        var oferta = novaOferta("POO");
        var matricula = novaMatricula(" MAT-001 ", aluno, oferta);

        assertEquals("MAT-001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(oferta.getTurma(), matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertEquals("MAT-001 - RA001 - POO - ATIVA", matricula.toString());
    }

    @Test
    @DisplayName("Construtor que recebe turma deve exigir uma única oferta")
    void construtorComTurmaDeveExigirOfertaUnica() {
        var aluno = novoAluno();
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turmaSemOferta = new Turma("T1", periodo);
        var turmaComUmaOferta = new Turma("T2", periodo);
        turmaComUmaOferta.ofertarDisciplina(new Disciplina("POO", "Programação", 80));
        var turmaComDuasOfertas = new Turma("T3", periodo);
        turmaComDuasOfertas.ofertarDisciplina(new Disciplina("POO", "Programação", 80));
        turmaComDuasOfertas.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT-001", aluno, (Turma) null));
        assertThrows(IllegalStateException.class,
                () -> new Matricula("MAT-002", aluno, turmaSemOferta));
        assertEquals(SituacaoMatricula.ATIVA,
                new Matricula("MAT-003", aluno, turmaComUmaOferta).getSituacao());
        assertThrows(IllegalStateException.class,
                () -> new Matricula("MAT-004", novoAluno(), turmaComDuasOfertas));
    }

    private static Aluno novoAluno() {
        return new Aluno("RA001", "Ana Souza", "ana@email.com");
    }

    private static OfertaDisciplina novaOferta(String codigoDisciplina) {
        var turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        return turma.ofertarDisciplina(
                new Disciplina(codigoDisciplina, "Disciplina " + codigoDisciplina, 80));
    }

    private static Matricula novaMatricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        return new Matricula(codigo, aluno, oferta);
    }
}
