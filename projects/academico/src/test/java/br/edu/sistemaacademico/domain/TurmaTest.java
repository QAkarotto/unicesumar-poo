package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    @DisplayName("Deve criar turma com dados válidos")
    void deveCriarTurma() {

        // Cria o período letivo que será utilizado pela turma
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        // Cria uma turma utilizando o período criado
        var turma = new Turma(
                "ESOFT4S-NA",
                periodo
        );

        // Confirma que o código da turma foi armazenado corretamente
        assertEquals("ESOFT4S-NA", turma.getCodigo());
    }

    @Test
    @DisplayName("Deve ofertar disciplina para a turma")
    void deveOfertarDisciplina() {

        // Cria uma turma para receber a disciplina
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        // Cria a disciplina que será ofertada
        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Cria a oferta da disciplina dentro da turma
        var oferta = turma.ofertarDisciplina(disciplina);

        // Confirma que a oferta foi criada
        assertNotNull(oferta);

        // Confirma que a disciplina pertence à oferta
        assertEquals(disciplina, oferta.getDisciplina());

        // Confirma que a oferta pertence à turma correta
        assertEquals(turma, oferta.getTurma());
    }
}