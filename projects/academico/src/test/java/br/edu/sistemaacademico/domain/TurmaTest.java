package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void deveCriarTurmaComDadosValidos() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma("TURMA-01", periodo);

        assertEquals("TURMA-01", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveCriarTurmaComDisciplinaOfertada() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                60
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "TURMA-01",
                disciplina,
                periodo
        );

        assertEquals(1, turma.getOfertas().size());
        assertEquals(
                disciplina,
                turma.getOfertas().getFirst().getDisciplina()
        );
    }

    @Test
    void devePermitirOfertarDisciplina() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertNotNull(oferta);
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void deveRejeitarDisciplinaNulaNaOferta() {
        Turma turma = criarTurma();

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDevePermitirOfertarMesmaDisciplinaDuasVezes() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );
    }

    @Test
    void deveRejeitarCodigoDaTurmaNulo() {
        PeriodoLetivo periodo = criarPeriodo();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(null, periodo)
        );
    }

    @Test
    void deveRejeitarCodigoDaTurmaVazio() {
        PeriodoLetivo periodo = criarPeriodo();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("   ", periodo)
        );
    }

    @Test
    void deveRejeitarPeriodoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("TURMA-01", null)
        );
    }

    @Test
    void deveRepresentarTurmaComoTexto() {
        Turma turma = criarTurma();

        assertEquals("TURMA-01 - 2026/1", turma.toString());
    }

    private Turma criarTurma() {
        return new Turma(
                "TURMA-01",
                criarPeriodo()
        );
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                60
        );
    }

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }
}