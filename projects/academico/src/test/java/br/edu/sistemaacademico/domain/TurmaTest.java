package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo periodo;
    private Disciplina disciplina;
    private Turma turma;

    @BeforeEach
    void setUp() {
        periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        disciplina = new Disciplina("FUSION101", "Tecnica de Fusao", 60);
        turma = new Turma("TURMA-Z", periodo);
    }

    @Test
    @DisplayName("Deve criar turma com codigo e periodo letivo")
    void deveCriarTurmaComSucesso() {
        assertEquals("TURMA-Z", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    @DisplayName("Deve ofertar disciplina em uma turma")
    void deveOfertarDisciplina() {
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertNotNull(oferta);
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar ofertar disciplina duplicada")
    void deveValidarOfertaDuplicada() {
        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    @DisplayName("Deve obter unica oferta com sucesso ou lancar excecao se houver mais/menos")
    void deveObterUnicaOferta() {
        assertThrows(IllegalStateException.class, () -> turma.obterUnicaOferta());

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        assertEquals(oferta, turma.obterUnicaOferta());

        Disciplina disciplina2 = new Disciplina("GENKI102", "Genki Dama", 60);
        turma.ofertarDisciplina(disciplina2);

        assertThrows(IllegalStateException.class, () -> turma.obterUnicaOferta());
    }

    @Test
    @DisplayName("Deve lancar excecao com parametros invalidos no construtor")
    void deveValidarConstrutor() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("", periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("TURMA-Z", null));
        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }
}