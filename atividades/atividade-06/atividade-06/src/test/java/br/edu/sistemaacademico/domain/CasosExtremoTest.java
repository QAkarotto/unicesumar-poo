package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CasosExtremoTest {

    @Test
    
    void testAlunoComNomeMuito() {
        String nomeLongo = "A".repeat(500);
        Aluno aluno = new Aluno("RA9000", nomeLongo, "long@gmail.com");

        assertEquals(nomeLongo, aluno.getNome());
    }

    @Test
    
    void testAlunoComEmailMuitoLongo() {
        String emailLongo = "a".repeat(100) + "@gmail.com.br";
        Aluno aluno = new Aluno("RA9001", "Nome", emailLongo);

        assertEquals(emailLongo, aluno.getEmail());
    }

    @Test
    
    void testAlunoComIdentificadorAlfanumerico() {
        Aluno aluno = new Aluno("RA123ABC456", "José", "jose@gmail.com");

        assertEquals("RA123ABC456", aluno.getIdentificadorAcademico());
    }

    @Test
    
    void testDisciplinaComCodigoEspecial() {
        Disciplina disciplina = new Disciplina("CS-101.A", "Computação", 80);

        assertEquals("CS-101.A", disciplina.getCodigo());
    }

    @Test
    
    void testDisciplinaComCargaGrandissima() {
        Disciplina disciplina = new Disciplina("SUPER101", "Super Disciplina", 10000);

        assertEquals(10000, disciplina.getCargaHoraria());
    }

    @Test
    
    void testDisciplinaComNomeNumeros() {
        Disciplina disciplina = new Disciplina("2026", "Disciplina 2026 v2.0", 50);

        assertTrue(disciplina.getNome().contains("2026"));
    }

    @Test
    
    void testEmailComMultiplosPontos() {
        Aluno aluno = new Aluno("RA9002", "Nome", "user.name.domain@email.co.uk");

        assertEquals("user.name.domain@email.co.uk", aluno.getEmail());
    }

    @Test
    
    void testEmailComNumeros() {
        Aluno aluno = new Aluno("RA9003", "Nome", "user123@email456.com");

        assertEquals("user123@email456.com", aluno.getEmail());
    }

    @Test
    
    void testTurmaComCodigoNumerico() {
        Turma turma = new Turma("202601", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertEquals("202601", turma.getCodigo());
    }

    @Test
    
    void testTurmaComCodigoHifen() {
        Turma turma = new Turma("T-2026-1-A", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        assertEquals("T-2026-1-A", turma.getCodigo());
    }

    @Test
    
    void testPeriodoAnoDistante() {
        PeriodoLetivo periodo = new PeriodoLetivo(9999, Semestre.PRIMEIRO);

        assertEquals(9999, periodo.getAno());
    }

    @Test
    
    void testPeriodoAnoMinimo() {
        PeriodoLetivo periodo = new PeriodoLetivo(1, Semestre.SEGUNDO);

        assertEquals(1, periodo.getAno());
    }

    @Test
    
    void testAlunoComAcentoNome() {
        Aluno aluno = new Aluno("RA9004", "José Luís Mártinez", "jose@gmail.com");

        assertEquals("José Luís Mártinez", aluno.getNome());
    }

    @Test
    
    void testDisciplinaComCaracteresEspeciais() {
        Disciplina disciplina = new Disciplina("SPEC101", "Geometria & Álgebra (Avançado)", 95);

        assertTrue(disciplina.getNome().contains("&"));
    }

    @Test
    
    void testAlunoHistoricoAposMultiplasOperacoes() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("T9000", periodo);

        Aluno aluno = new Aluno("RA9005", "Múltiplo", "multiplo@gmail.com");

        // Matricular em várias disciplinas
        for (int i = 0; i < 10; i++) {
            Disciplina disciplina = new Disciplina("DISC" + i, "Disciplina " + i, 40 + i);
            OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
            oferta.matricular("M9000" + i, aluno);
        }

        assertEquals(10, aluno.getHistorico().size());
    }

    @Test
    
    void testEqualsHashCodeCoerencia() {
        Aluno a1 = new Aluno("RA9006", "Nome1", "email1@gmail.com");
        Aluno a2 = new Aluno("RA9006", "Nome2", "email2@gmail.com");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    
    void testDisciplinaHashCodeCoerencia() {
        Disciplina d1 = new Disciplina("MATH999", "Matemática", 100);
        Disciplina d2 = new Disciplina("MATH999", "Outra Matemática", 50);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    
    void testEmailComHifen() {
        Aluno aluno = new Aluno("RA9007", "Nome", "user@my-domain.com");

        assertTrue(aluno.getEmail().contains("-"));
    }

    @Test
    
    void testTurmaMuitasOfertas() {
        Turma turma = new Turma("TURMA-GRANDE", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        // Criar 50 disciplinas e ofertar
        for (int i = 0; i < 50; i++) {
            Disciplina d = new Disciplina("CODE" + String.format("%03d", i), "Disciplina " + i, 60);
            turma.ofertarDisciplina(d);
        }

        assertEquals(50, turma.getOfertas().size());
    }

    @Test
    
    void testOfertaMuitosAlunos() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-LOTADA", periodo);
        Disciplina d = new Disciplina("POPULAR101", "Aula Popular", 120);
        OfertaDisciplina oferta = turma.ofertarDisciplina(d);

        // Matricular 100 alunos
        for (int i = 0; i < 100; i++) {
            Aluno aluno = new Aluno("RA-LOTADO-" + i, "Aluno " + i, "aluno" + i + "@gmail.com");
            oferta.matricular("M-LOT-" + i, aluno);
        }

        assertEquals(100, oferta.getMatriculas().size());
    }

    @Test
    
    void testResultadosDiferentes() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-RES", periodo);
        Disciplina d = new Disciplina("RES101", "Resultado", 40);
        Aluno a = new Aluno("RA9008", "Resultado", "resultado@gmail.com");

        OfertaDisciplina oferta = turma.ofertarDisciplina(d);
        Matricula matricula = oferta.matricular("M-RES", a);

        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        // Confirmar que não é REPROVADO
        assertTrue(matricula.getResultado() != ResultadoAcademico.REPROVADO);
    }

    @Test
    
    void testEmailComEspacoAposArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA9009", "Nome", "user@ domain.com"));
    }

    @Test
    
    void testAtualizacaoEmailPreservaOutrosDados() {
        Aluno aluno = new Aluno("RA9010", "João Preservado", "joao@gmail.com");
        String nomeOriginal = aluno.getNome();
        String idOriginal = aluno.getIdentificadorAcademico();

        aluno.setEmail("novo@gmail.com");

        assertEquals(nomeOriginal, aluno.getNome());
        assertEquals(idOriginal, aluno.getIdentificadorAcademico());
    }

    @Test
    
    void testTurmaOfertasImutavel() {
        Turma turma = new Turma("TURMA-IMUTAVEL", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d = new Disciplina("IMUT101", "Imutável", 50);
        turma.ofertarDisciplina(d);

        var ofertas = turma.getOfertas();
        assertThrows(UnsupportedOperationException.class, () -> ofertas.clear());
    }

    @Test
    
    void testAlunoHistoricoImutavel() {
        Aluno aluno = new Aluno("RA9011", "Histórico", "historico@gmail.com");
        PeriodoLetivo p = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma t = new Turma("T-HIST", p);
        Disciplina d = new Disciplina("HIST101", "História", 55);
        OfertaDisciplina oferta = t.ofertarDisciplina(d);
        oferta.matricular("M-HIST", aluno);

        var historico = aluno.getHistorico();
        assertThrows(UnsupportedOperationException.class, () -> historico.remove(0));
    }

    @Test
    
    void testOfertaMatriculasImutavel() {
        Turma t = new Turma("T-OFFER-IMUT", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d = new Disciplina("OFFER-IMUT", "Oferta Imutável", 45);
        OfertaDisciplina oferta = t.ofertarDisciplina(d);

        var matriculas = oferta.getMatriculas();
        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(null));
    }

    @Test
    
    void testToStringCompletoSistema() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("TURMA-STR", periodo);
        Disciplina disciplina = new Disciplina("STR101", "String Test", 60);
        Aluno aluno = new Aluno("RA9012", "StringTest", "string@gmail.com");

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Matricula matricula = oferta.matricular("M-STR", aluno);

        String periodoStr = periodo.toString();
        String turmaStr = turma.toString();
        String disciplinaStr = disciplina.toString();
        String alunoStr = aluno.toString();
        String matriculaStr = matricula.toString();

        assertNotNull(periodoStr);
        assertNotNull(turmaStr);
        assertNotNull(disciplinaStr);
        assertNotNull(alunoStr);
        assertNotNull(matriculaStr);

        assertTrue(periodoStr.length() > 0);
        assertTrue(turmaStr.length() > 0);
        assertTrue(disciplinaStr.length() > 0);
        assertTrue(alunoStr.length() > 0);
        assertTrue(matriculaStr.length() > 0);
    }
}
