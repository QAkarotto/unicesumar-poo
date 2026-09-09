package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private Aluno aluno;
    private Disciplina poo;
    private OfertaDisciplina oferta;

    @BeforeEach
    void preparar() {
        aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var turma = new Turma("ESOFT4S-NA", poo, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        oferta = turma.getOfertas().get(0);
    }

    @Test
    @DisplayName("Deve criar matrícula ativa vinculada ao aluno e à oferta")
    void deveCriarMatriculaAtiva() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        assertEquals("MAT-1", matricula.getCodigo());
        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Não deve criar matrícula com código vazio")
    void naoDeveCriarComCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("  ", aluno, oferta)
        );
    }

    @Test
    @DisplayName("Não deve criar matrícula sem aluno")
    void naoDeveCriarSemAluno() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", null, oferta)
        );
    }

    @Test
    @DisplayName("Não deve criar matrícula sem oferta")
    void naoDeveCriarSemOferta() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (OfertaDisciplina) null)
        );
    }

    @Test
    @DisplayName("Não deve criar matrícula por turma quando a turma é nula")
    void naoDeveCriarPorTurmaNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (Turma) null)
        );
    }

    @Test
    @DisplayName("Deve concluir a matrícula com resultado de aprovação")
    void deveConcluirComAprovacao() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve concluir a matrícula com resultado de reprovação")
    void deveConcluirComReprovacao() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Não deve concluir matrícula sem informar o resultado")
    void naoDeveConcluirSemResultado() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    @DisplayName("Não deve concluir uma matrícula que não está ativa")
    void naoDeveConcluirMatriculaNaoAtiva() {
        var matricula = new Matricula("MAT-1", aluno, oferta);
        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    @DisplayName("Deve trancar uma matrícula ativa")
    void deveTrancarMatriculaAtiva() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar uma matrícula ativa")
    void deveCancelarMatriculaAtiva() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Não deve trancar uma matrícula já trancada")
    void naoDeveTrancarMatriculaJaTrancada() {
        var matricula = new Matricula("MAT-1", aluno, oferta);
        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    @DisplayName("Não deve cancelar uma matrícula já concluída")
    void naoDeveCancelarMatriculaConcluida() {
        var matricula = new Matricula("MAT-1", aluno, oferta);
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    @DisplayName("Deve bloquear nova matrícula em disciplina já aprovada")
    void deveBloquearNovaMatriculaAposAprovacao() {
        var primeira = new Matricula("MAT-1", aluno, oferta);
        primeira.concluir(ResultadoAcademico.APROVADO);
        var outraTurma = new Turma("ESOFT4S-NB", poo, new PeriodoLetivo(2027, Semestre.PRIMEIRO));
        var novaOferta = outraTurma.getOfertas().get(0);

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-2", aluno, novaOferta)
        );
    }

    @Test
    @DisplayName("Deve permitir nova matrícula em disciplina reprovada")
    void devePermitirNovaMatriculaAposReprovacao() {
        var primeira = new Matricula("MAT-1", aluno, oferta);
        primeira.concluir(ResultadoAcademico.REPROVADO);
        var outraTurma = new Turma("ESOFT4S-NB", poo, new PeriodoLetivo(2027, Semestre.PRIMEIRO));
        var novaOferta = outraTurma.getOfertas().get(0);

        var segunda = new Matricula("MAT-2", aluno, novaOferta);

        assertEquals(SituacaoMatricula.ATIVA, segunda.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("toString deve conter código, aluno, disciplina e situação")
    void toStringDeveConterDadosPrincipais() {
        var matricula = new Matricula("MAT-1", aluno, oferta);

        var texto = matricula.toString();

        assertTrue(texto.contains("MAT-1"));
        assertTrue(texto.contains("RA001"));
        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("ATIVA"));
    }
}
