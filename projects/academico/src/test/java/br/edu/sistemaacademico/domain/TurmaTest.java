package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    @Test
    void deveCriarTurmaValida() {
        PeriodoLetivo periodo = criarPeriodo();

        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        periodo
                );

        assertEquals("ESOFT4S-NB", turma.getCodigo());
        assertEquals(
                periodo,
                turma.getPeriodoLetivo()
        );
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void naoDeveCriarTurmaSemCodigo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "",
                        criarPeriodo()
                )
        );
    }

    @Test
    void naoDeveCriarTurmaComCodigoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        null,
                        criarPeriodo()
                )
        );
    }

    @Test
    void naoDeveCriarTurmaSemPeriodo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "ESOFT4S-NB",
                        null
                )
        );
    }

    @Test
    void deveRemoverEspacosDoCodigo() {
        Turma turma =
                new Turma(
                        "  ESOFT4S-NB  ",
                        criarPeriodo()
                );

        assertEquals(
                "ESOFT4S-NB",
                turma.getCodigo()
        );
    }

    @Test
    void deveCriarTurmaComDisciplina() {
        PeriodoLetivo periodo = criarPeriodo();
        Disciplina disciplina = criarDisciplina();

        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        disciplina,
                        periodo
                );

        assertEquals(
                "ESOFT4S-NB",
                turma.getCodigo()
        );

        assertEquals(
                periodo,
                turma.getPeriodoLetivo()
        );

        assertEquals(
                1,
                turma.getOfertas().size()
        );

        assertEquals(
                disciplina,
                turma.getOfertas().getFirst().getDisciplina()
        );
    }

    @Test
    void naoDeveCriarTurmaComDisciplinaNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "ESOFT4S-NB",
                        (Disciplina) null,
                        criarPeriodo()
                )
        );
    }

    @Test
    void deveOfertarDisciplina() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        assertNotNull(oferta);
        assertEquals(
                1,
                turma.getOfertas().size()
        );
        assertEquals(
                disciplina,
                oferta.getDisciplina()
        );
        assertEquals(
                turma,
                oferta.getTurma()
        );
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezes() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        Disciplina disciplina = criarDisciplina();

        turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );
    }

    @Test
    void devePermitirDuasDisciplinasDiferentes() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        Disciplina poo =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina banco =
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                );

        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(banco);

        assertEquals(
                2,
                turma.getOfertas().size()
        );
    }

    @Test
    void deveObterUnicaOfertaQuandoExisteApenasUma() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        assertEquals(
                oferta,
                turma.obterUnicaOferta()
        );
    }

    @Test
    void naoDeveObterUnicaOfertaQuandoNaoExisteOferta() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        assertThrows(
                IllegalStateException.class,
                turma::obterUnicaOferta
        );
    }

    @Test
    void naoDeveObterUnicaOfertaQuandoExistemVariasOfertas() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        turma.ofertarDisciplina(
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                )
        );

        assertThrows(
                IllegalStateException.class,
                turma::obterUnicaOferta
        );
    }

    @Test
    void deveRetornarListaDeOfertasSomenteParaLeitura() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        turma.ofertarDisciplina(
                criarDisciplina()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
    }

    @Test
    void deveRetornarToStringCorreto() {
        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        criarPeriodo()
                );

        assertEquals(
                "ESOFT4S-NB - 2026/1",
                turma.toString()
        );
    }
}