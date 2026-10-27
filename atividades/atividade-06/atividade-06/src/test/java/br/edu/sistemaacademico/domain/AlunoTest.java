package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    void testCriarAlunoComDadosValidos() {
        Aluno aluno = new Aluno("RA001", "João Silva", "joao@gmail.com");

        assertEquals("RA001", aluno.getIdentificadorAcademico());
        assertEquals("João Silva", aluno.getNome());
        assertEquals("joao@gmail.com", aluno.getEmail());
        assertTrue(aluno.getHistorico().isEmpty());
    }

    @Test
    void testNaoCriarAlunoComIdentificadorNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Maria Silva", "maria@gmail.com"));
    }

    @Test
    void testNaoCriarAlunoComIdentificadorBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("   ", "Maria Silva", "maria@gmail.com"));
    }

    @Test
    void testNaoCriarAlunoComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA002", null, "ana@gmail.com"));
    }

    @Test
    void testNaoCriarAlunoComNomeBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA003", "   ", "ana@gmail.com"));
    }

    @Test
    void testNaoCriarAlunoComEmailNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA004", "Pedro", null));
    }

    @Test
    void testNaoCriarAlunoComEmailBranco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA005", "Pedro", "   "));
    }

    @Test
    void testNaoCriarAlunoComEmailSemArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA006", "Clara", "claraemaill.com"));
    }

    @Test
    void testNaoCriarAlunoComEmailComecandoArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA007", "Clara", "@gmail.com"));
    }

    @Test
    void testNaoCriarAlunoComEmailTerminandoArroba() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA008", "Clara", "clara@"));
    }

    @Test
    void testAtualizarEmailComValorValido() {
        Aluno aluno = new Aluno("RA009", "Lucas", "lucas@gmail.com");

        aluno.setEmail("newemail@gmail.com");

        assertEquals("newemail@gmail.com", aluno.getEmail());
    }

    @Test
    void testNaoAtualizarEmailParaNulo() {
        Aluno aluno = new Aluno("RA010", "Ana", "ana@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail(null));
        assertEquals("ana@gmail.com", aluno.getEmail());
    }

    @Test
    void testNaoAtualizarEmailParaInvalido() {
        Aluno aluno = new Aluno("RA011", "Beatriz", "beatriz@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalid-email"));
        assertEquals("beatriz@gmail.com", aluno.getEmail());
    }

    @Test
    void testEqualsComMesmoIdentificador() {
        Aluno aluno1 = new Aluno("RA012", "João", "joao@gmail.com");
        Aluno aluno2 = new Aluno("RA012", "João Diferente", "outro@gmail.com");

        assertEquals(aluno1, aluno2);
        assertEquals(aluno1, aluno1);
    }

    @Test
    void testNotEqualsComIdentificadoresDiferentes() {
        Aluno aluno1 = new Aluno("RA013", "João", "joao@gmail.com");
        Aluno aluno2 = new Aluno("RA014", "João", "joao@gmail.com");

        assertNotEquals(aluno1, aluno2);
    }

    @Test
    void testNotEqualsComOutroTipo() {
        Aluno aluno = new Aluno("RA015", "João", "joao@gmail.com");

        assertNotEquals(aluno, "RA015");
        assertNotEquals(aluno, 123);
        assertNotEquals(aluno, null);
    }

    @Test
    void testHashCodeComMesmoIdentificador() {
        Aluno aluno1 = new Aluno("RA016", "Ana", "ana@gmail.com");
        Aluno aluno2 = new Aluno("RA016", "Ana Diferente", "diferente@gmail.com");

        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void testToStringContemDados() {
        Aluno aluno = new Aluno("RA017", "Carlos", "carlos@gmail.com");

        String texto = aluno.toString();

        assertTrue(texto.contains("RA017"));
        assertTrue(texto.contains("Carlos"));
        assertTrue(texto.contains("carlos@gmail.com"));
    }

    @Test
    void testNaoAprovadoComHistoricoVazio() {
        Aluno aluno = new Aluno("RA018", "Daniela", "daniela@gmail.com");
        Disciplina disciplina = new Disciplina("OOP01", "Orientação a Objetos", 60);

        assertFalse(aluno.jaAprovadoEm(disciplina));
    }

    @Test
    void testNaoAprovadoComReprovacao() {
        Aluno aluno = new Aluno("RA019", "Eduardo", "eduardo@gmail.com");
        Disciplina disciplina = new Disciplina("OOP02", "POO Avançado", 80);
        Turma turma = new Turma("T100", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        Matricula matricula = oferta.matricular("M100", aluno);
        matricula.registrarResultado(ResultadoAcademico.REPROVADO);

        assertFalse(aluno.jaAprovadoEm(disciplina));
    }

    @Test
    void testAprovadoComAprovacao() {
        Aluno aluno = new Aluno("RA020", "Fernanda", "fernanda@gmail.com");
        Disciplina disciplina = new Disciplina("OOP03", "Estruturas de Dados", 75);
        Turma turma = new Turma("T101", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        Matricula matricula = oferta.matricular("M101", aluno);
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertTrue(aluno.jaAprovadoEm(disciplina));
    }

    @Test
    void testHistoricoImutavel() {
        Aluno aluno = new Aluno("RA021", "Gabriela", "gabriela@gmail.com");

        var historico = aluno.getHistorico();

        assertThrows(UnsupportedOperationException.class, () -> historico.add(null));
    }

    @Test
    void testMultiplasMatriculasNoHistorico() {
        Aluno aluno = new Aluno("RA022", "Henrique", "henrique@gmail.com");
        Disciplina d1 = new Disciplina("MATH01", "Cálculo", 90);
        Disciplina d2 = new Disciplina("PHYS01", "Física", 85);

        Turma turma1 = new Turma("T102", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Turma turma2 = new Turma("T103", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        OfertaDisciplina oferta1 = turma1.ofertarDisciplina(d1);
        OfertaDisciplina oferta2 = turma2.ofertarDisciplina(d2);

        oferta1.matricular("M102", aluno);
        oferta2.matricular("M103", aluno);

        assertEquals(2, aluno.getHistorico().size());
    }

    @Test
    void testTotalDisciplinasAprovadosZero() {
        Aluno aluno = new Aluno("RA023", "Iris", "iris@gmail.com");

        assertEquals(0, aluno.totalDisciplinasAprovadas());
    }

    @Test
    void testTotalDisciplinasAprovados() {
        Aluno aluno = new Aluno("RA024", "Jander", "jander@gmail.com");
        Disciplina d1 = new Disciplina("PROG02", "Programação II", 80);
        Disciplina d2 = new Disciplina("DB02", "Banco II", 75);
        Disciplina d3 = new Disciplina("WEB02", "Web II", 70);

        Turma turma = new Turma("T104", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina o1 = turma.ofertarDisciplina(d1);
        OfertaDisciplina o2 = turma.ofertarDisciplina(d2);
        OfertaDisciplina o3 = turma.ofertarDisciplina(d3);

        Matricula m1 = o1.matricular("M104", aluno);
        Matricula m2 = o2.matricular("M105", aluno);
        Matricula m3 = o3.matricular("M106", aluno);

        m1.registrarResultado(ResultadoAcademico.APROVADO);
        m2.registrarResultado(ResultadoAcademico.APROVADO);
        m3.registrarResultado(ResultadoAcademico.REPROVADO);

        assertEquals(2, aluno.totalDisciplinasAprovadas());
    }
}
