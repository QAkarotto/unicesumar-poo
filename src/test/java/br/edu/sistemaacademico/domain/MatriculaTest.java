package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private Turma turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
    private Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
    private OfertaDisciplina oferta = turma.ofertarDisciplina(poo);

    private Matricula matricula(String codigo, String registro) {
        return oferta.matricular(codigo, new Aluno(registro, "Ana Souza", registro.toLowerCase() + "@email.com"));
    }

    @Test
    @DisplayName("Deve nascer ativa, em curso e sem resultado")
    void deveNascerAtivaEEmCurso() {
        var matricula = matricula("MAT-001", "RA001");

        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertTrue(matricula.isEmCurso());
        assertFalse(matricula.isAprovada());
        assertNull(matricula.getResultado());
        assertSame(oferta, matricula.getOferta());
        assertEquals("RA001", matricula.getAluno().getRegistroAcademico());
        assertSame(poo, matricula.getDisciplina());
        assertSame(turma, matricula.getTurma());
        assertEquals("2026/2", matricula.getPeriodoLetivo().toString());
    }

    @Test
    @DisplayName("Deve exigir código, aluno e oferta na criação")
    void deveExigirDadosObrigatorios() {
        var aluno = new Aluno("RA002", "Ana Souza", "ana@email.com");

        assertThrows(IllegalArgumentException.class, () -> new Matricula("   ", aluno, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-002", null, oferta));
        assertThrows(IllegalArgumentException.class, () -> new Matricula("MAT-002", aluno, null));
    }

    @Test
    @DisplayName("Deve trancar apenas matrícula ativa")
    void deveTrancarApenasMatriculaAtiva() {
        var matricula = matricula("MAT-003", "RA003");

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertTrue(matricula.isEmCurso());
        assertThrows(IllegalStateException.class, matricula::trancar);
    }

    @Test
    @DisplayName("Deve reativar apenas matrícula trancada")
    void deveReativarApenasMatriculaTrancada() {
        var matricula = matricula("MAT-004", "RA004");

        assertThrows(IllegalStateException.class, matricula::reativar);

        matricula.trancar();
        matricula.reativar();

        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar matrícula ativa e recusar cancelamento repetido")
    void deveCancelarMatriculaAtiva() {
        var matricula = matricula("MAT-005", "RA005");

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
        assertFalse(matricula.isEmCurso());
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    @DisplayName("Deve recusar cancelamento de matrícula concluída")
    void deveRecusarCancelamentoDeConcluida() {
        var matricula = matricula("MAT-006", "RA006");
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    @DisplayName("Deve concluir com aprovação e encerrar a matrícula")
    void deveConcluirComAprovacao() {
        var matricula = matricula("MAT-007", "RA007");

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertTrue(matricula.isAprovada());
        assertFalse(matricula.isEmCurso());
    }

    @Test
    @DisplayName("Deve concluir com reprovação sem marcar aprovação")
    void deveConcluirComReprovacao() {
        var matricula = matricula("MAT-008", "RA008");

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertFalse(matricula.isAprovada());
        assertFalse(ResultadoAcademico.REPROVADO.isAprovado());
    }

    @Test
    @DisplayName("Deve permitir concluir matrícula trancada")
    void devePermitirConcluirMatriculaTrancada() {
        var matricula = matricula("MAT-009", "RA009");
        matricula.trancar();

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve recusar conclusão inválida")
    void deveRecusarConclusaoInvalida() {
        var semResultado = matricula("MAT-010", "RA010");
        var cancelada = matricula("MAT-011", "RA011");
        var concluida = matricula("MAT-012", "RA012");
        cancelada.cancelar();
        concluida.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalArgumentException.class, () -> semResultado.concluir(null));
        assertThrows(IllegalStateException.class, () -> cancelada.concluir(ResultadoAcademico.APROVADO));
        assertThrows(IllegalStateException.class, () -> concluida.concluir(ResultadoAcademico.REPROVADO));
    }

    @Test
    @DisplayName("Deve comparar matrículas pelo código")
    void deveCompararMatriculasPeloCodigo() {
        var primeira = matricula("MAT-013", "RA013");
        var outraTurma = new Turma("ESOFT5S-NA", new PeriodoLetivo(2027, Semestre.PRIMEIRO));
        var mesmaCodigo = outraTurma
                .ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80))
                .matricular("mat-013", new Aluno("RA014", "Bruno Santos", "bruno@email.com"));
        var diferente = matricula("MAT-015", "RA015");

        assertTrue(primeira.equals(mesmaCodigo));
        assertEquals(primeira.hashCode(), mesmaCodigo.hashCode());
        assertFalse(primeira.equals(diferente));
        assertFalse(primeira.equals("MAT-013"));
        assertTrue(primeira.toString().contains("MAT-013"));
        assertTrue(primeira.toString().contains("ATIVA"));
    }
}