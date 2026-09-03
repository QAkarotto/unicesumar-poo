package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IdentidadeDoDominioTest {

    @Test
    void deveConsiderarAlunosComMesmoRegistroAcademicoComoIguais() {

        // Preparação
        Aluno primeiroAluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Aluno segundoAluno = new Aluno(
                "RA2026001",
                "Paola O.",
                "outro@email.com"
        );

        Aluno alunoDiferente = new Aluno(
                "RA2026002",
                "Paola Oliveira",
                "paola@email.com"
        );

        // Verificação do resultado
        assertEquals(primeiroAluno, segundoAluno);
        assertEquals(primeiroAluno.hashCode(), segundoAluno.hashCode());
        assertNotEquals(primeiroAluno, alunoDiferente);
    }

    @Test
    void deveConsiderarDisciplinasComMesmoCodigoComoIguais() {

        // Preparação
        Disciplina primeiraDisciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina segundaDisciplina = new Disciplina(
                "POO",
                "POO Avançada",
                40
        );

        Disciplina disciplinaDiferente = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        // Verificação do resultado
        assertEquals(primeiraDisciplina, segundaDisciplina);
        assertEquals(
                primeiraDisciplina.hashCode(),
                segundaDisciplina.hashCode()
        );
        assertNotEquals(primeiraDisciplina, disciplinaDiferente);
    }

    @Test
    void deveConsiderarPeriodosComMesmoAnoESemestreComoIguais() {

        // Preparação
        PeriodoLetivo primeiroPeriodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo segundoPeriodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        PeriodoLetivo periodoDiferente = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        // Verificação do resultado
        assertEquals(primeiroPeriodo, segundoPeriodo);
        assertEquals(
                primeiroPeriodo.hashCode(),
                segundoPeriodo.hashCode()
        );
        assertNotEquals(primeiroPeriodo, periodoDiferente);
    }
}