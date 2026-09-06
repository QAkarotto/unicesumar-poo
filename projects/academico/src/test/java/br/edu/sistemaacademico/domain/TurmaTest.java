package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TurmaTest {

    @Test
    @DisplayName("Deve criar uma turma sem ofertas")
    void deveCriarUmaTurmaSemOfertas() {
        // Arrange
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        // Act
        Turma turma = new Turma("ADSIS4S", periodo);

        // Assert
        assertEquals("ADSIS4S", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertEquals(0, turma.getOfertas().size());
        assertEquals("ADSIS4S - 2026/2", turma.toString());
    }

    @Test
    @DisplayName("Nao deve criar turma sem codigo")
    void naoDeveCriarTurmaSemCodigo() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> {
            new Turma("   ", periodo);
        });
    }

    @Test
    @DisplayName("Nao deve criar turma sem periodo letivo")
    void naoDeveCriarTurmaSemPeriodoLetivo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Turma("ADSIS4S", null);
        });
    }

    @Test
    @DisplayName("Deve ofertar uma disciplina para a turma")
    void deveOfertarUmaDisciplinaParaATurma() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        Disciplina disciplina = new Disciplina("POO", "Programacao Orientada a Objetos", 80);

        // Act
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(1, turma.getOfertas().size());
        assertEquals("POO - ADSIS4S - 2026/2", oferta.toString());
    }

    @Test
    @DisplayName("Deve criar a turma ja com a disciplina ofertada")
    void deveCriarATurmaJaComADisciplinaOfertada() {
        // Arrange
        Disciplina disciplina = new Disciplina("BD", "Banco de Dados", 60);

        // Act
        Turma turma = new Turma(
                "ADSIS4S",
                disciplina,
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().get(0).getDisciplina());
    }

    @Test
    @DisplayName("Nao deve ofertar a mesma disciplina duas vezes na turma")
    void naoDeveOfertarAMesmaDisciplinaDuasVezesNaTurma() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(new Disciplina("POO", "Programacao Orientada a Objetos", 80));

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            turma.ofertarDisciplina(new Disciplina("POO", "POO Turma B", 40));
        });
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Nao deve ofertar uma disciplina nula")
    void naoDeveOfertarUmaDisciplinaNula() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            turma.ofertarDisciplina(null);
        });
    }
}
