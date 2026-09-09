package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private Matricula matricula;
    private Aluno aluno;
    private OfertaDisciplina oferta;

    @BeforeEach
    void setUp() {
        Disciplina disciplina = new Disciplina("CS201", "Algoritmos", 90);
        Turma turma = new Turma("T200", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        oferta = turma.ofertarDisciplina(disciplina);
        aluno = new Aluno("RA200", "Roberto", "roberto@email.com");
        matricula = oferta.matricular("M200", aluno);
    }

    @Test
    
    void testIniciarSemResultado() {
        assertNull(matricula.getResultado());
    }

    @Test
    
    void testRegistrarResultadoAprovado() {
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    
    void testRegistrarResultadoReprovado() {
        matricula.registrarResultado(ResultadoAcademico.REPROVADO);

        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    
    void testNaoRegistrarResultadoNulo() {
        assertThrows(IllegalArgumentException.class, () -> matricula.registrarResultado(null));
        assertNull(matricula.getResultado());
    }

    @Test
    
    void testNaoRegistrarResultadoDuasVezes() {
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class,
                () -> matricula.registrarResultado(ResultadoAcademico.REPROVADO));
    }

    @Test
    
    void testNaoCriarComCodigoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula(null, aluno, oferta));
    }

    @Test
    
    void testNaoCriarComCodigoBranco() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula("   ", aluno, oferta));
    }

    @Test
    
    void testNaoCriarComAlunoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M999", null, oferta));
    }

    @Test
    
    void testNaoCriarComOfertaNula() {
        assertThrows(IllegalArgumentException.class, () -> new Matricula("M999", aluno, null));
    }

    @Test
    
    void testGetCodigo() {
        assertEquals("M200", matricula.getCodigo());
    }

    @Test
    
    void testGetAluno() {
        assertEquals(aluno, matricula.getAluno());
    }

    @Test
    
    void testGetOfertaDisciplina() {
        assertEquals(oferta, matricula.getOfertaDisciplina());
    }

    @Test
    
    void testToStringEmAndamento() {
        String texto = matricula.toString();

        assertTrue(texto.contains("M200"));
        assertTrue(texto.contains("Roberto"));
        assertTrue(texto.contains("EM ANDAMENTO"));
    }

    @Test
    
    void testToStringComAprovado() {
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        String texto = matricula.toString();

        assertTrue(texto.contains("APROVADO"));
        assertTrue(texto.contains("M200"));
    }

    @Test
    
    void testToStringComReprovado() {
        matricula.registrarResultado(ResultadoAcademico.REPROVADO);

        String texto = matricula.toString();

        assertTrue(texto.contains("REPROVADO"));
    }

    @Test
    
    void testGetResultadoInicialmentNull() {
        assertNull(matricula.getResultado());
    }

    @Test
    
    void testGetResultadoAposRegistro() {
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertNotNull(matricula.getResultado());
    }
}
