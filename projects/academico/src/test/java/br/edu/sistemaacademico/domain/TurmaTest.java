package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TurmaTest {

    @Test
    @DisplayName("Deve ofertar disciplina para a turma")
    void deveOfertarDisciplina() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act
        var oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertSame(oferta, turma.getOfertas().get(0));
        assertSame(disciplina, oferta.getDisciplina());
    }

    @Test
    @DisplayName("Deve impedir oferta de disciplina duplicada")
    void deveImpedirOfertaDuplicada() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var disciplina1 = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var disciplina2 = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        turma.ofertarDisciplina(disciplina1);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina2)
        );

        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve permitir ofertas de disciplinas diferentes para a mesma turma")
    void devePermitirOfertasDeDisciplinasDiferentes() {
        // Arrange
        var turma = new Turma( "ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO) );
        var disciplina1 = new Disciplina( "POO", "Programação Orientada a Objetos", 80 );
        var disciplina2 = new Disciplina( "BD", "Banco de Dados", 60 );

        // Act
        turma.ofertarDisciplina(disciplina1);
        turma.ofertarDisciplina(disciplina2);

        // Assert
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve registrar a mesma oferta criada")
    void deveRegistrarMesmaOfertaCriada() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act
        var oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertSame(oferta, turma.getOfertas().get(0));
    }
}
