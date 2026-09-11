package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    @Test
    @DisplayName("Deve permitir ofertar disciplinas diferentes e localizar uma oferta")
    void deveOfertarDisciplinasDiferentes() {
        // Arrange
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD", "Banco de Dados", 80);

        // Act
        var ofertaPoo = turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertTrue(turma.possuiOferta(poo));
        assertTrue(turma.possuiOferta(bd));
        assertSame(ofertaPoo, turma.buscarOferta(poo));
    }

    @Test
    @DisplayName("Deve impedir a mesma disciplina duas vezes na mesma turma")
    void deveImpedirOfertaDuplicada() {
        // Arrange
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmaDisciplina = new Disciplina("poo", "Outro nome", 40);
        turma.ofertarDisciplina(poo);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> turma.ofertarDisciplina(mesmaDisciplina));
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve informar quando disciplina não é ofertada pela turma")
    void deveFalharAoBuscarDisciplinaNaoOfertada() {
        // Arrange
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act + Assert
        assertFalse(turma.possuiOferta(poo));
        assertThrows(IllegalArgumentException.class,
                () -> turma.buscarOferta(poo));
    }

    @Test
    @DisplayName("Operações de oferta devem exigir uma disciplina")
    void deveExigirDisciplinaNasOperacoes() {
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null));
        assertThrows(IllegalArgumentException.class,
                () -> turma.possuiOferta(null));
        assertThrows(IllegalArgumentException.class,
                () -> turma.buscarOferta(null));
    }
    @Test
    @DisplayName("A mesma disciplina pode ser ofertada em turmas diferentes")
    void devePermitirMesmaDisciplinaEmTurmasDiferentes() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turmaA = new Turma("ESOFT4S-A", periodo);
        var turmaB = new Turma("ESOFT4S-B", periodo);

        var ofertaA = turmaA.ofertarDisciplina(disciplina);
        var ofertaB = turmaB.ofertarDisciplina(disciplina);

        assertTrue(turmaA.possuiOferta(disciplina));
        assertTrue(turmaB.possuiOferta(disciplina));
        assertFalse(ofertaA.equals(ofertaB));
    }

}