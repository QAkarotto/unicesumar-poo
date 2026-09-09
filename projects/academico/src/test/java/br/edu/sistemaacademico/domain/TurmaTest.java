package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TurmaTest {

    @Test
    @DisplayName("Deve exigir código e período letivo na criação da turma")
    void deveExigirCodigoEPeriodoLetivo() {
        // Arrange
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        // Act
        var semCodigo = assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("   ", periodo)
        );
        var semPeriodo = assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );

        // Assert
        assertEquals("O código da turma é obrigatório.", semCodigo.getMessage());
        assertEquals("O período letivo é obrigatório.", semPeriodo.getMessage());
    }

    @Test
    @DisplayName("Deve ofertar disciplinas diferentes para a mesma turma")
    void deveOfertarDisciplinasDiferentes() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        var poo = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var bancoDados = turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        assertEquals(2, turma.getOfertas().size());
        assertSame(turma, poo.getTurma());
        assertEquals("BD", bancoDados.getDisciplina().getCodigo());
    }

    @Test
    @DisplayName("Deve recusar a mesma disciplina duas vezes na mesma turma")
    void deveRecusarDisciplinaJaOfertada() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(new Disciplina("POO", "POO em outra instância", 40))
        );

        assertEquals("A disciplina já foi ofertada para esta turma.", erro.getMessage());
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve recusar disciplina nula na oferta")
    void deveRecusarDisciplinaNula() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );

        assertEquals("A disciplina é obrigatória.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve já ofertar a disciplina informada no construtor")
    void deveOfertarDisciplinaInformadaNoConstrutor() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var turma = new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().get(0).getDisciplina());
        assertEquals("ESOFT4S-NA - 2026/1", turma.toString());
    }

    @Test
    @DisplayName("Deve devolver as ofertas como cópia protegida")
    void deveDevolverOfertasComoCopia() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        var ofertas = turma.getOfertas();

        assertThrows(
                UnsupportedOperationException.class,
                () -> ofertas.add(oferta)
        );
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve guardar o período letivo informado")
    void deveGuardarPeriodoLetivo() {
        var periodo = new PeriodoLetivo(2025, Semestre.SEGUNDO);

        var turma = new Turma("  ESOFT4S-NA  ", periodo);

        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
    }
}
