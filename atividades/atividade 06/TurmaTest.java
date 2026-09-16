package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private Disciplina criarDisciplina() {
        return new Disciplina("ALG1", "Algoritmos I", 60);
    }

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    }

    private Aluno criarAluno() {
        return new Aluno("2026001", "Maria Silva", "maria@exemplo.com");
    }

    // --- Oferta de disciplinas (criação da turma) ---

    @Test
    void deveCriarTurmaComoOfertaValidaDeUmaDisciplina() {
        // Arrange
        Disciplina disciplina = criarDisciplina();
        PeriodoLetivo periodo = criarPeriodo();

        // Act
        Turma turma = new Turma("T01", disciplina, periodo);

        // Assert
        assertEquals(disciplina, turma.getDisciplina());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getMatriculas().isEmpty());
    }

    @Test
    void naoDeveCriarTurmaComCodigoNuloOuVazio() {
        // Arrange
        Disciplina disciplina = criarDisciplina();
        PeriodoLetivo periodo = criarPeriodo();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Turma(null, disciplina, periodo));
        assertThrows(IllegalArgumentException.class,
                () -> new Turma(" ", disciplina, periodo));
    }

    @Test
    void naoDeveCriarTurmaComDisciplinaNula() {
        // Arrange
        PeriodoLetivo periodo = criarPeriodo();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("T01", null, periodo));
    }

    @Test
    void naoDeveCriarTurmaComPeriodoLetivoNulo() {
        // Arrange
        Disciplina disciplina = criarDisciplina();

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("T01", disciplina, null));
    }

    @Test
    void mesmaDisciplinaPodeSerOfertadaEmTurmasDeSemestresDiferentes() {
        // Arrange
        Disciplina disciplina = criarDisciplina();
        PeriodoLetivo periodo2026_1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo periodo2026_2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        // Act
        Turma turmaPrimeiroSemestre = new Turma("T01", disciplina, periodo2026_1);
        Turma turmaSegundoSemestre = new Turma("T02", disciplina, periodo2026_2);

        // Assert
        assertEquals(disciplina, turmaPrimeiroSemestre.getDisciplina());
        assertEquals(disciplina, turmaSegundoSemestre.getDisciplina());
        assertEquals(Semestre.PRIMEIRO, turmaPrimeiroSemestre.getPeriodoLetivo().getSemestre());
        assertEquals(Semestre.SEGUNDO, turmaSegundoSemestre.getPeriodoLetivo().getSemestre());
    }

    // --- Matrícula ---

    @Test
    void deveMatricularAlunoNaTurma() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno = criarAluno();

        // Act
        Matricula matricula = turma.matricular("M001", aluno);

        // Assert
        assertEquals(1, turma.getMatriculas().size());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.EM_CURSO, matricula.getSituacao());
    }

    @Test
    void naoDeveMatricularAlunoNuloNaTurma() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> turma.matricular("M001", null));
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaEnquantoEmCurso() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno = criarAluno();
        turma.matricular("M001", aluno);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> turma.matricular("M002", aluno));
        assertEquals(1, turma.getMatriculas().size());
    }

    // --- Aprovação / reprovação e seus efeitos sobre novas matrículas ---

    @Test
    void deveBloquearNovaMatriculaAposAprovacao() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno = criarAluno();
        Matricula primeiraMatricula = turma.matricular("M001", aluno);
        primeiraMatricula.aprovar();

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> turma.matricular("M002", aluno));
        assertEquals(1, turma.getMatriculas().size());
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno = criarAluno();
        Matricula primeiraMatricula = turma.matricular("M001", aluno);
        primeiraMatricula.reprovar();

        // Act
        Matricula segundaMatricula = turma.matricular("M002", aluno);

        // Assert
        assertEquals(SituacaoMatricula.REPROVADO, primeiraMatricula.getSituacao());
        assertEquals(SituacaoMatricula.EM_CURSO, segundaMatricula.getSituacao());
        assertEquals(2, turma.getMatriculas().size());
    }

    @Test
    void naoDevePermitirDuasMatriculasEmCursoMesmoAposReprovacaoAntiga() {
        // Arrange: aluno reprova na primeira, matricula de novo (permitido),
        // mas não pode se matricular uma terceira vez enquanto a segunda
        // ainda está em curso.
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno = criarAluno();
        Matricula primeiraMatricula = turma.matricular("M001", aluno);
        primeiraMatricula.reprovar();
        turma.matricular("M002", aluno);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> turma.matricular("M003", aluno));
        assertEquals(2, turma.getMatriculas().size());
    }

    @Test
    void diferentesAlunosPodemSeMatricularNaMesmaTurma() {
        // Arrange
        Turma turma = new Turma("T01", criarDisciplina(), criarPeriodo());
        Aluno aluno1 = criarAluno();
        Aluno aluno2 = new Aluno("2026002", "João Souza", "joao@exemplo.com");

        // Act
        turma.matricular("M001", aluno1);
        turma.matricular("M002", aluno2);

        // Assert
        assertEquals(2, turma.getMatriculas().size());
    }
}
