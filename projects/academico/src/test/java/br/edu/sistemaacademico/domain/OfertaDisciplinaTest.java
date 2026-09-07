package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private Turma turma;
    private Disciplina disciplina;
    private OfertaDisciplina oferta;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        turma = new Turma("T-GOKU", periodo);
        disciplina = new Disciplina("DBZ101", "Ki Control", 60);
        oferta = new OfertaDisciplina(turma, disciplina);
        aluno = new Aluno("RA8000", "Son Goku", "goku@capsulecorp.com");
    }

    @Test
    @DisplayName("Deve criar oferta de disciplina com sucesso")
    void deveCriarOfertaComSucesso() {
        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Deve registrar nova matricula na oferta")
    void deveRegistrarMatricula() {
        Matricula matricula = new Matricula("MAT-1", aluno, oferta);

        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    @DisplayName("Deve validar nova matricula para evitar duplicacao de aluno")
    void deveValidarNovaMatricula() {
        new Matricula("MAT-1", aluno, oferta);

        assertThrows(RuntimeException.class, () -> oferta.validarNovaMatricula(aluno));
    }

    @Test
    @DisplayName("Deve validar equals e hashCode para oferta de disciplina")
    void deveValidarEqualsEHashCode() {
        OfertaDisciplina ofertaMesmaInstancia = oferta;
        Turma outraTurma = new Turma("T-VEGETA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina ofertaDiferente = new OfertaDisciplina(outraTurma, disciplina);

        assertEquals(oferta, ofertaMesmaInstancia);
        assertEquals(oferta.hashCode(), ofertaMesmaInstancia.hashCode());
        assertNotEquals(oferta, ofertaDiferente);
        assertNotEquals(oferta, "TextoQualquer");
    }

    @Test
    @DisplayName("Deve retornar toString formatado")
    void deveRetornarToString() {
        assertNotNull(oferta.toString());
    }
}