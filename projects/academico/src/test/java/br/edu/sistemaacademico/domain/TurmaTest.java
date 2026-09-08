package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TurmaTest {

    @Test
    void devePermitirOfertarDuasDisciplinasDiferentesNaMesmaTurma() {

        // Preparação
        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina bancoDeDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        // Ação
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDeDados);

        // Verificação do resultado
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void naoDevePermitirOfertarAMesmaDisciplinaDuasVezesNaTurma() {

        // Preparação
        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        turma.ofertarDisciplina(poo);

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }
}