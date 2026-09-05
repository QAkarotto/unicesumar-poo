package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo periodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }

    private Disciplina disciplina(String codigo) {
        return new Disciplina(
                codigo,
                "Disciplina " + codigo,
                80
        );
    }

    @Test
    void deveCriarTurmaValida() {
        Turma turma = new Turma("T01", periodo());

        assertEquals("T01", turma.getCodigo());
        assertNotNull(turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveRejeitarCodigoNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(null, periodo())
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("", periodo())
        );
    }

    @Test
    void deveRejeitarPeriodoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("T01", null)
        );
    }

    @Test
    void deveOfertarDisciplina() {
        Turma turma = new Turma("T01", periodo());
        Disciplina disciplina = disciplina("POO");

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertSame(disciplina, oferta.getDisciplina());
        assertSame(turma, oferta.getTurma());
        assertEquals(1, turma.getOfertas().size());
        assertTrue(turma.getOfertas().contains(oferta));
    }

    @Test
    void devePermitirVariasDisciplinasNaMesmaTurma() {
        Turma turma = new Turma("T01", periodo());

        turma.ofertarDisciplina(disciplina("POO"));
        turma.ofertarDisciplina(disciplina("BD"));

        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void naoDevePermitirDisciplinaDuplicadaNaMesmaTurma() {
        Turma turma = new Turma("T01", periodo());

        turma.ofertarDisciplina(disciplina("POO"));

        Disciplina outraInstanciaMesmoCodigo =
                disciplina("POO");

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(
                        outraInstanciaMesmoCodigo
                )
        );
    }

    @Test
    void deveRejeitarDisciplinaNula() {
        Turma turma = new Turma("T01", periodo());

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void listaDeOfertasNaoDevePermitirAlteracaoExterna() {
        Turma turma = new Turma("T01", periodo());

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
    }
}