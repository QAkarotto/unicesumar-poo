package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

    @Test
    void construtorComDadosValidos_deveCriarTurmaCorretamente() {
        // Arrange / Act
        Turma turma = new Turma("ESOFT4S-NA", periodo);

        // Assert
        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void construtorComCodigoNuloOuVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("  ", periodo));
    }

    @Test
    void construtorComPeriodoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("ESOFT4S-NA", null));
    }

    @Test
    void ofertarDisciplina_comDisciplinaValida_deveAdicionarNaListaDeOfertas() {
        // Arrange
        Turma turma = new Turma("ESOFT4S-NA", periodo);
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(poo, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void ofertarDisciplina_duasDisciplinasDiferentes_devePermitirAmbas() {
        // Arrange
        Turma turma = new Turma("ESOFT4S-NA", periodo);
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        Disciplina bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void ofertarDisciplina_comDisciplinaNula_deveLancarExcecao() {
        // Arrange
        Turma turma = new Turma("ESOFT4S-NA", periodo);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    void ofertarDisciplina_jaOfertadaNaMesmaTurma_deveLancarExcecao() {
        // Arrange
        Turma turma = new Turma("ESOFT4S-NA", periodo);
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(poo);

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> turma.ofertarDisciplina(poo));
    }

    @Test
    void getOfertas_deveRetornarListaImutavel() {
        // Arrange
        Turma turma = new Turma("ESOFT4S-NA", periodo);
        List<OfertaDisciplina> ofertas = turma.getOfertas();

        // Act / Assert
        assertThrows(UnsupportedOperationException.class, () -> ofertas.add(null));
    }
}