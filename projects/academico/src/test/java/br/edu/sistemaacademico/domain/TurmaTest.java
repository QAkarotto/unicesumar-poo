package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    @Test
    @DisplayName("Deve permitir ofertar disciplinas diferentes")
    void devePermitirOfertarDisciplinasDiferentes() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDeDados = new Disciplina("BD", "Banco de Dados", 80);

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDeDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertTrue(turma.getOfertas().stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(poo)));
        assertTrue(turma.getOfertas().stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(bancoDeDados)));
    }

    @Test
    @DisplayName("Deve impedir disciplina duplicada na turma")
    void deveImpedirDisciplinaDuplicadaNaTurma() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(poo);

        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }

    @Test
    @DisplayName("Deve proteger a coleção de ofertas")
    void deveProtegerColecaoDeOfertas() {
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
        assertEquals(1, turma.getOfertas().size());
    }
}
