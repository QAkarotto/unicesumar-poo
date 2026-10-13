package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaTest {

    private OfertaDisciplina oferta;
    private Aluno aluno;
    private Disciplina disciplina;
    private Turma turma;

    @BeforeEach
    void setUp() {
        disciplina = new Disciplina("PROG101", "Programação Avançada", 75);
        turma = new Turma("T300", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        oferta = turma.ofertarDisciplina(disciplina);
        aluno = new Aluno("RA300", "Sabrina", "sabrina@email.com");
    }

    @Test
    
    void testMatricularAlunoNaOferta() {
        Matricula matricula = oferta.matricular("M300", aluno);

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(1, aluno.getHistorico().size());
    }

    @Test
    
    void testNaoMatricularDuasVezesNaMesmaOferta() {
        oferta.matricular("M301", aluno);

        assertThrows(IllegalStateException.class, () -> oferta.matricular("M302", aluno));
    }

    @Test
    
    void testNaoMatricularAlunoNulo() {
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular("M303", null));
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    
    void testNaoMatricularAlunoAprovado() {
        Matricula matricula = oferta.matricular("M304", aluno);
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        Turma outroTurma = new Turma("T301", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina outroOferta = outroTurma.ofertarDisciplina(disciplina);

        assertThrows(IllegalStateException.class, () -> outroOferta.matricular("M305", aluno));
    }

    @Test
    
    void testPermitirMatriculaAposReprovacao() {
        Matricula primeira = oferta.matricular("M306", aluno);
        primeira.registrarResultado(ResultadoAcademico.REPROVADO);

        Turma outroTurma = new Turma("T302", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina outroOferta = outroTurma.ofertarDisciplina(disciplina);

        Matricula segunda = outroOferta.matricular("M307", aluno);

        assertEquals(2, aluno.getHistorico().size());
        assertEquals(aluno, segunda.getAluno());
        assertEquals(ResultadoAcademico.REPROVADO, primeira.getResultado());
    }

    @Test
    
    void testGetTurma() {
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    
    void testGetDisciplina() {
        assertEquals(disciplina, oferta.getDisciplina());
    }

    @Test
    
    void testGetMatriculasVazia() {
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    
    void testGetMatriculasComAlunos() {
        oferta.matricular("M308", aluno);

        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    
    void testGetMatriculasImutavel() {
        oferta.matricular("M309", aluno);
        var matriculas = oferta.getMatriculas();

        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(null));
    }

    @Test
    
    void testToStringContemDados() {
        String texto = oferta.toString();

        assertTrue(texto.contains("T300"));
        assertTrue(texto.contains("Programação Avançada"));
    }

    @Test
    
    void testMultiplasMatriculasAlunosDiferentes() {
        Aluno aluno2 = new Aluno("RA301", "Tania", "tania@email.com");
        Aluno aluno3 = new Aluno("RA302", "Ulisses", "ulisses@email.com");

        oferta.matricular("M310", aluno);
        oferta.matricular("M311", aluno2);
        oferta.matricular("M312", aluno3);

        assertEquals(3, oferta.getMatriculas().size());
    }

    @Test
    
    void testNaoMatricularAposAprovacaoOutraTurma() {
        Turma turma2 = new Turma("T303", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta2 = turma2.ofertarDisciplina(disciplina);

        Matricula matricula = oferta2.matricular("M313", aluno);
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, () -> oferta.matricular("M314", aluno));
    }

    @Test
    
    void testGetTotalMatriculas() {
        Aluno a1 = new Aluno("RA315", "Aluno 1", "aluno1@gmail.com");
        Aluno a2 = new Aluno("RA316", "Aluno 2", "aluno2@gmail.com");
        Aluno a3 = new Aluno("RA317", "Aluno 3", "aluno3@gmail.com");

        assertEquals(0, oferta.getTotalMatriculas());
        oferta.matricular("M315", a1);
        assertEquals(1, oferta.getTotalMatriculas());
        oferta.matricular("M316", a2);
        oferta.matricular("M317", a3);

        assertEquals(3, oferta.getTotalMatriculas());
    }

    @Test
    
    void testToStringComTotalMatriculas() {
        Aluno a1 = new Aluno("RA318", "Teste", "teste@gmail.com");
        oferta.matricular("M318", a1);

        String texto = oferta.toString();

        assertTrue(texto.contains("total_alunos"));
        assertTrue(texto.contains("1"));
    }
}
