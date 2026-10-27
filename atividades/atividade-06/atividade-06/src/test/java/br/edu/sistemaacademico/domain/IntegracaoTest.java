package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegracaoTest {

    @Test
    
    void testFluxoCompletoMatricula() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-2026-1", periodo);

        Disciplina matematica = new Disciplina("MAT101", "Matemática Básica", 80);
        Disciplina programacao = new Disciplina("PROG101", "Programação", 100);

        Aluno aluno1 = new Aluno("RA001", "João Silva", "joao@gmail.com");
        Aluno aluno2 = new Aluno("RA002", "Maria Santos", "maria@gmail.com");

        OfertaDisciplina ofertaMat = turma.ofertarDisciplina(matematica);
        OfertaDisciplina ofertaProg = turma.ofertarDisciplina(programacao);

        Matricula m1 = ofertaMat.matricular("M001", aluno1);
        Matricula m2 = ofertaMat.matricular("M002", aluno2);
        Matricula m3 = ofertaProg.matricular("M003", aluno1);

        m1.registrarResultado(ResultadoAcademico.APROVADO);
        m2.registrarResultado(ResultadoAcademico.REPROVADO);

        assertEquals(3, turma.getPeriodoLetivo().getAno());
        assertTrue(aluno1.jaAprovadoEm(matematica));
        assertFalse(aluno2.jaAprovadoEm(matematica));
        assertEquals(2, aluno1.getHistorico().size());
        assertEquals(1, aluno2.getHistorico().size());
    }

    @Test
    
    void testBloqueioMatriculaAposAprovacao() {
        PeriodoLetivo periodo1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo periodo2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma1 = new Turma("T1-2026-1", periodo1);
        Turma turma2 = new Turma("T1-2026-2", periodo2);

        Disciplina disciplina = new Disciplina("CS101", "Estruturas de Dados", 75);

        Aluno aluno = new Aluno("RA100", "Carlos", "carlos@gmail.com");

        OfertaDisciplina oferta1 = turma1.ofertarDisciplina(disciplina);
        Matricula matricula1 = oferta1.matricular("M100", aluno);
        matricula1.registrarResultado(ResultadoAcademico.APROVADO);

        OfertaDisciplina oferta2 = turma2.ofertarDisciplina(disciplina);

        assertTrue(aluno.jaAprovadoEm(disciplina));
        assertThrows(IllegalStateException.class, () -> oferta2.matricular("M101", aluno));
    }

    @Test
    
    void testPermissaoMatriculaAposReprovacao() {
        PeriodoLetivo p1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo p2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma1 = new Turma("TURM-2026-S1", p1);
        Turma turma2 = new Turma("TURM-2026-S2", p2);

        Disciplina disciplina = new Disciplina("PHYS101", "Física Geral", 60);

        Aluno aluno = new Aluno("RA200", "Daniela", "daniela@gmail.com");

        OfertaDisciplina oferta1 = turma1.ofertarDisciplina(disciplina);
        Matricula m1 = oferta1.matricular("M200", aluno);
        m1.registrarResultado(ResultadoAcademico.REPROVADO);

        OfertaDisciplina oferta2 = turma2.ofertarDisciplina(disciplina);
        Matricula m2 = oferta2.matricular("M201", aluno);

        assertFalse(aluno.jaAprovadoEm(disciplina));
        assertEquals(2, aluno.getHistorico().size());
        assertEquals(ResultadoAcademico.REPROVADO, m1.getResultado());
    }

    @Test
    
    void testHistoricoMultiplosDisciplinas() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-GERAL", periodo);

        Disciplina d1 = new Disciplina("DB101", "Banco de Dados", 85);
        Disciplina d2 = new Disciplina("WEB101", "Web Development", 80);
        Disciplina d3 = new Disciplina("SEC101", "Segurança", 70);

        Aluno aluno = new Aluno("RA300", "Eduardo", "eduardo@gmail.com");

        OfertaDisciplina oferta1 = turma.ofertarDisciplina(d1);
        OfertaDisciplina oferta2 = turma.ofertarDisciplina(d2);
        OfertaDisciplina oferta3 = turma.ofertarDisciplina(d3);

        Matricula m1 = oferta1.matricular("M300", aluno);
        Matricula m2 = oferta2.matricular("M301", aluno);
        Matricula m3 = oferta3.matricular("M302", aluno);

        m1.registrarResultado(ResultadoAcademico.APROVADO);
        m2.registrarResultado(ResultadoAcademico.APROVADO);
        m3.registrarResultado(ResultadoAcademico.REPROVADO);

        assertEquals(3, aluno.getHistorico().size());
        assertTrue(aluno.jaAprovadoEm(d1));
        assertTrue(aluno.jaAprovadoEm(d2));
        assertFalse(aluno.jaAprovadoEm(d3));
    }

    @Test
    
    void testValidacaoMatriculaDuplicada() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-DUPLICA", periodo);
        Disciplina disciplina = new Disciplina("DUP101", "Disciplina Teste", 50);
        Aluno aluno = new Aluno("RA400", "Fernanda", "fernanda@gmail.com");

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        oferta.matricular("M400", aluno);

        assertThrows(IllegalStateException.class, () -> oferta.matricular("M401", aluno));
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    
    void testTurmaComMultiplasOfertas() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        Turma turma = new Turma("TURMA-COMPLETA", periodo);

        Disciplina d1 = new Disciplina("ART201", "Artes Visuais", 65);
        Disciplina d2 = new Disciplina("MUS201", "Teoria da Música", 55);
        Disciplina d3 = new Disciplina("DANCE201", "Dança Contemporânea", 60);
        Disciplina d4 = new Disciplina("THEATER201", "Teatro", 70);

        turma.ofertarDisciplina(d1);
        turma.ofertarDisciplina(d2);
        turma.ofertarDisciplina(d3);
        turma.ofertarDisciplina(d4);

        assertEquals(4, turma.getOfertas().size());
        assertEquals(periodo, turma.getPeriodoLetivo());
    }

    @Test
    
    void testAtualizacaoEmailAluno() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-EMAIL", periodo);
        Disciplina disciplina = new Disciplina("EMAIL101", "Testes de Email", 40);

        Aluno aluno = new Aluno("RA500", "Gabriela", "gabri@gmail.com");

        aluno.setEmail("gabriela.nova@gmail.com");
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Matricula matricula = oferta.matricular("M500", aluno);

        assertEquals("gabriela.nova@gmail.com", aluno.getEmail());
        assertEquals(aluno, matricula.getAluno());
        assertTrue(matricula.toString().contains("gabriela@gmail.com"));
    }

    @Test
    
    void testPeriodoCompartilhadoEntreturmas() {
        PeriodoLetivo periodo = new PeriodoLetivo(2027, Semestre.PRIMEIRO);
        Turma turma1 = new Turma("TURMA-A", periodo);
        Turma turma2 = new Turma("TURMA-B", periodo);

        assertEquals(turma1.getPeriodoLetivo(), turma2.getPeriodoLetivo());
        assertEquals(2027, turma1.getPeriodoLetivo().getAno());
        assertEquals(Semestre.PRIMEIRO, turma2.getPeriodoLetivo().getSemestre());
    }

    @Test
    
    void testValidacaoCascata() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-CASCADE", periodo);
        Disciplina disciplina = new Disciplina("CASCADE101", "Cascata", 45);

        Aluno aluno = new Aluno("RA600", "Henrique", "henrique@gmail.com");

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Matricula matricula = oferta.matricular("M600", aluno);

        assertEquals(periodo.getAno(), turma.getPeriodoLetivo().getAno());
        assertEquals(turma, oferta.getTurma());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(aluno, matricula.getAluno());
    }

    @Test
    
    void testRepresentacaoTextualEmCadeia() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-REPR", periodo);
        Disciplina disciplina = new Disciplina("REPR101", "Representação", 50);
        Aluno aluno = new Aluno("RA700", "Íris", "iris@gmail.com");

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Matricula matricula = oferta.matricular("M700", aluno);

        String alunoStr = aluno.toString();
        String disciplinaStr = disciplina.toString();
        String periodoStr = periodo.toString();
        String turmaStr = turma.toString();
        String matriculaStr = matricula.toString();

        assertTrue(alunoStr.contains("RA700"));
        assertTrue(disciplinaStr.contains("REPR101"));
        assertTrue(periodoStr.contains("2026"));
        assertTrue(turmaStr.contains("TURMA-REPR"));
        assertTrue(matriculaStr.contains("M700"));
    }
}
