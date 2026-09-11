package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdentidadeDominioTest {

    @Test
    @DisplayName("Aluno deve ser identificado pelo registro acadêmico ignorando maiúsculas e minúsculas")
    void deveCompararAlunoPeloRegistroAcademico() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        var mesmoAluno = new Aluno("ra001", "Outro Nome", "outro@email.com");
        var outroAluno = new Aluno("RA002", "Ana", "ana2@email.com");

        assertEquals(aluno, aluno);
        assertEquals(aluno, mesmoAluno);
        assertEquals(aluno.hashCode(), mesmoAluno.hashCode());
        assertNotEquals(aluno, outroAluno);
        assertNotEquals(aluno, "RA001");
        assertEquals("RA001 - Ana", aluno.toString());
    }

    @Test
    @DisplayName("Disciplina deve ser identificada pelo código ignorando maiúsculas e minúsculas")
    void deveCompararDisciplinaPeloCodigo() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmaDisciplina = new Disciplina("poo", "Outro nome", 40);
        var outraDisciplina = new Disciplina("BD", "Banco de Dados", 80);

        assertEquals(disciplina, disciplina);
        assertEquals(disciplina, mesmaDisciplina);
        assertEquals(disciplina.hashCode(), mesmaDisciplina.hashCode());
        assertNotEquals(disciplina, outraDisciplina);
        assertNotEquals(disciplina, "POO");
        assertEquals("Programação Orientada a Objetos", disciplina.toString());
    }

    @Test
    @DisplayName("Período letivo deve ser identificado pelo ano e semestre")
    void deveCompararPeriodoLetivoPorAnoESemestre() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var mesmoPeriodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroAno = new PeriodoLetivo(2025, Semestre.SEGUNDO);
        var outroSemestre = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(periodo, periodo);
        assertEquals(periodo, mesmoPeriodo);
        assertEquals(periodo.hashCode(), mesmoPeriodo.hashCode());
        assertNotEquals(periodo, outroAno);
        assertNotEquals(periodo, outroSemestre);
        assertNotEquals(periodo, "2026/2");
        assertEquals("2026/2", periodo.toString());
    }

    @Test
    @DisplayName("Turma deve ser identificada pelo código e período letivo")
    void deveCompararTurmaPorCodigoEPeriodo() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("ESOFT4S", periodo);
        var mesmaTurma = new Turma("esoft4s", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var outraTurma = new Turma("ADSIS4S", periodo);
        var turmaOutroPeriodo = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        assertEquals(turma, turma);
        assertEquals(turma, mesmaTurma);
        assertEquals(turma.hashCode(), mesmaTurma.hashCode());
        assertNotEquals(turma, outraTurma);
        assertNotEquals(turma, turmaOutroPeriodo);
        assertNotEquals(turma, "ESOFT4S");
        assertTrue(turma.toString().contains("Programação Orientada a Objetos"));
    }

    @Test
    @DisplayName("Oferta deve ser identificada pela turma e disciplina")
    void deveCompararOfertaPorTurmaEDisciplina() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("ESOFT4S", periodo);
        var mesmaTurma = new Turma("esoft4s", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var outraTurma = new Turma("ADSIS4S", periodo);
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmaDisciplina = new Disciplina("poo", "Outro nome", 40);
        var outraDisciplina = new Disciplina("BD", "Banco de Dados", 80);

        var oferta = new OfertaDisciplina(turma, disciplina);
        var mesmaOferta = new OfertaDisciplina(mesmaTurma, mesmaDisciplina);
        var ofertaOutraTurma = new OfertaDisciplina(outraTurma, disciplina);
        var ofertaOutraDisciplina = new OfertaDisciplina(turma, outraDisciplina);

        assertEquals(oferta, oferta);
        assertEquals(oferta, mesmaOferta);
        assertEquals(oferta.hashCode(), mesmaOferta.hashCode());
        assertNotEquals(oferta, ofertaOutraTurma);
        assertNotEquals(oferta, ofertaOutraDisciplina);
        assertNotEquals(oferta, "POO");
        assertEquals("Programação Orientada a Objetos (ESOFT4S - 2026/2)", oferta.toString());
    }

    @Test
    @DisplayName("Matrícula deve ser identificada pelo código ignorando maiúsculas e minúsculas")
    void deveCompararMatriculaPeloCodigo() {
        var aluno1 = new Aluno("RA001", "Ana", "ana@email.com");
        var aluno2 = new Aluno("RA002", "Bruno", "bruno@email.com");
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));

        var matricula = new Matricula("MAT-001", aluno1, oferta);
        var mesmaMatricula = new Matricula("mat-001", aluno2, oferta);
        var outraMatricula = new Matricula("MAT-002", aluno1, oferta);

        assertEquals(matricula, matricula);
        assertEquals(matricula, mesmaMatricula);
        assertEquals(matricula.hashCode(), mesmaMatricula.hashCode());
        assertNotEquals(matricula, outraMatricula);
        assertNotEquals(matricula, "MAT-001");
        assertTrue(matricula.toString().contains("MAT-001"));
        assertTrue(matricula.toString().contains("Ana"));
        assertTrue(matricula.toString().contains("ATIVA"));
    }
}
