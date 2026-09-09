package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private static final PeriodoLetivo SEGUNDO_SEMESTRE_2026 =
            new PeriodoLetivo(2026, Semestre.SEGUNDO);

    @Test
    @DisplayName("Turma recém-criada não possui ofertas")
    void turmaNasceSemOfertas() {
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);

        assertTrue(turma.getOfertas().isEmpty());
        assertEquals(SEGUNDO_SEMESTRE_2026, turma.getPeriodoLetivo());
    }

    @Test
    @DisplayName("Ofertar uma disciplina vincula a oferta à turma")
    void deveOfertarDisciplina() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        // Act
        var oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
        assertEquals(1, turma.getOfertas().size());
        assertFalse(turma.getOfertas().isEmpty());
    }

    @Test
    @DisplayName("A mesma disciplina não pode ser ofertada duas vezes na turma")
    void deveImpedirOfertaDuplicada() {
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);
        turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(
                        new Disciplina("POO", "POO - turma da noite", 40)
                )
        );

        assertEquals("A disciplina já foi ofertada para esta turma.", erro.getMessage());
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Uma turma pode ofertar disciplinas diferentes")
    void devePermitirVariasDisciplinas() {
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);

        turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 60));

        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Oferta sem disciplina é recusada")
    void deveRecusarDisciplinaNula() {
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    @DisplayName("Código e período letivo são obrigatórios na criação da turma")
    void deveValidarDadosDaTurma() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(" ", SEGUNDO_SEMESTRE_2026)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    @DisplayName("A lista de ofertas devolvida não pode ser alterada por fora")
    void ofertasNaoDevemSerAlteradasDeFora() {
        var turma = new Turma("ESOFT4S-NA", SEGUNDO_SEMESTRE_2026);
        var oferta = turma.ofertarDisciplina(new Disciplina("ES", "Engenharia de Software", 60));
        var ofertas = turma.getOfertas();

        assertThrows(UnsupportedOperationException.class, () -> ofertas.add(oferta));
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Construtor com disciplina já deixa a turma com a oferta pronta")
    void construtorComDisciplinaJaCriaAOferta() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var turma = new Turma("ESOFT4S-NB", disciplina, SEGUNDO_SEMESTRE_2026);

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().get(0).getDisciplina());
        assertEquals("ESOFT4S-NB - 2026/2", turma.toString());
    }
}
