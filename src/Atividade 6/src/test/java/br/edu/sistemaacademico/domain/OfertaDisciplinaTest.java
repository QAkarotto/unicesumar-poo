package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfertaDisciplinaTest {

    private OfertaDisciplina oferta;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        oferta = turma.ofertarDisciplina(disciplina);
        aluno = new Aluno("RA001", "Lucas", "lucas@email.com");
    }

    @Test
    void deveMatricularAlunoNaOferta() {
        Matricula matricula = oferta.matricular("M001", aluno);

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(1, aluno.getHistorico().size());
    }

    @Test
    void naoDeveMatricularMesmoAlunoDuasVezesNaMesmaOferta() {
        oferta.matricular("M001", aluno);

        assertThrows(IllegalStateException.class, () -> oferta.matricular("M002", aluno));
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular("M001", null));
    }

    @Test
    void naoDeveMatricularAlunoJaAprovadoNaDisciplina() {
        Matricula matricula = oferta.matricular("M001", aluno);
        matricula.registrarResultado(ResultadoAcademico.APROVADO);

        Disciplina mesmaDisciplina = oferta.getDisciplina();
        Turma outraTurma = new Turma("T2", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina novaOferta = outraTurma.ofertarDisciplina(mesmaDisciplina);

        assertThrows(IllegalStateException.class, () -> novaOferta.matricular("M002", aluno));
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        Matricula primeiraMatricula = oferta.matricular("M001", aluno);
        primeiraMatricula.registrarResultado(ResultadoAcademico.REPROVADO);

        Turma outraTurma = new Turma("T2", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina novaOferta = outraTurma.ofertarDisciplina(oferta.getDisciplina());
        Matricula segundaMatricula = novaOferta.matricular("M002", aluno);

        assertEquals(2, aluno.getHistorico().size());
        assertEquals(aluno, segundaMatricula.getAluno());
    }
}
