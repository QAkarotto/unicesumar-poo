package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TurmaValidacaoTest {

    @Test
    @DisplayName("Deve validar os dados obrigatórios da turma")
    void deveValidarDadosObrigatorios() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("  ", periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma("T1", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("T1", (Disciplina) null, periodo));
    }

    @Test
    @DisplayName("Construtor com disciplina deve criar a oferta")
    void construtorComDisciplinaDeveCriarOferta() {
        var disciplina = new Disciplina("POO", "Programação", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turma = new Turma(" T1 ", disciplina, periodo);

        assertEquals("T1", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().get(0).getDisciplina());
        assertEquals("T1 - 2026/1", turma.toString());
    }

    @Test
    @DisplayName("Não deve ofertar disciplina com mesmo código duas vezes")
    void deveRejeitarDisciplinaEquivalenteJaOfertada() {
        var turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        turma.ofertarDisciplina(new Disciplina("POO", "Programação", 80));

        assertThrows(IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(new Disciplina("POO", "Outro nome", 40)));
    }
}
