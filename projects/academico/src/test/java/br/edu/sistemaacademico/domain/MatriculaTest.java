package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    @Test
    @DisplayName("Deve trancar e depois reativar uma matrícula")
    void deveTrancarEReativarMatricula() {
        // Arrange
        var matricula = novaMatricula("MAT-001");

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertTrue(matricula.isEmCurso());

        // Act
        matricula.reativar();

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertTrue(matricula.isEmCurso());
    }

    @Test
    @DisplayName("Deve impedir trancar matrícula que não está ativa")
    void deveImpedirNovoTrancamento() {
        var matricula = novaMatricula("MAT-002");
        matricula.trancar();

        assertThrows(IllegalStateException.class, matricula::trancar);
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar matrícula e impedir cancelamento repetido")
    void deveCancelarMatricula() {
        var matricula = novaMatricula("MAT-003");

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
        assertFalse(matricula.isEmCurso());
        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertThrows(IllegalStateException.class, matricula::reativar);
    }

    @Test
    @DisplayName("Deve concluir matrícula com aprovação")
    void deveConcluirComAprovacao() {
        // Arrange
        var matricula = novaMatricula("MAT-004");

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertTrue(matricula.isAprovada());
        assertFalse(matricula.isEmCurso());
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    @DisplayName("Deve concluir matrícula com reprovação sem marcar aluno como aprovado")
    void deveConcluirComReprovacao() {
        var matricula = novaMatricula("MAT-005");

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
        assertFalse(matricula.isAprovada());
    }

    @Test
    @DisplayName("Deve permitir conclusão de matrícula trancada")
    void deveConcluirMatriculaTrancada() {
        var matricula = novaMatricula("MAT-006");
        matricula.trancar();

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertTrue(matricula.isAprovada());
    }

    @Test
    @DisplayName("Deve impedir conclusão de matrícula cancelada")
    void deveImpedirConclusaoDeMatriculaCancelada() {
        var matricula = novaMatricula("MAT-007");
        matricula.cancelar();

        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve exigir resultado acadêmico para concluir")
    void deveExigirResultadoParaConclusao() {
        var matricula = novaMatricula("MAT-008");

        assertThrows(IllegalArgumentException.class,
                () -> matricula.concluir(null));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Matrícula deve manter as referências acadêmicas da oferta")
    void deveExporRelacionamentosDaMatricula() {
        var aluno = new Aluno("RA100", "Ana", "ana@email.com");
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("ESOFT4S", periodo);
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var oferta = turma.ofertarDisciplina(disciplina);

        var matricula = oferta.matricular("MAT-100", aluno);

        assertEquals("MAT-100", matricula.getCodigo());
        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOferta());
        assertSame(disciplina, matricula.getDisciplina());
        assertSame(turma, matricula.getTurma());
        assertSame(periodo, matricula.getPeriodoLetivo());
        assertNotNull(matricula.toString());
        assertFalse(matricula.isAprovada());
    }

    @Test
    @DisplayName("Deve permitir cancelar uma matrícula trancada")
    void deveCancelarMatriculaTrancada() {
        var matricula = novaMatricula("MAT-009");
        matricula.trancar();

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
        assertFalse(matricula.isEmCurso());
    }

    @Test
    @DisplayName("Deve impedir reativar matrícula ativa")
    void deveImpedirReativacaoDeMatriculaAtiva() {
        var matricula = novaMatricula("MAT-010");

        assertThrows(IllegalStateException.class, matricula::reativar);
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir concluir novamente uma matrícula já concluída")
    void deveImpedirNovaConclusao() {
        var matricula = novaMatricula("MAT-011");
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO));
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Resultado acadêmico deve distinguir aprovação de reprovação")
    void deveDistinguirResultadoAcademico() {
        assertTrue(ResultadoAcademico.APROVADO.isAprovado());
        assertFalse(ResultadoAcademico.REPROVADO.isAprovado());
    }

    private Matricula novaMatricula(String codigoMatricula) {
        var aluno = new Aluno("RA-" + codigoMatricula, "Aluno Teste", "aluno@email.com");
        var turma = new Turma("T-" + codigoMatricula,
                new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("D-" + codigoMatricula, "Disciplina Teste", 80)
        );
        return oferta.matricular(codigoMatricula, aluno);
    }
}
