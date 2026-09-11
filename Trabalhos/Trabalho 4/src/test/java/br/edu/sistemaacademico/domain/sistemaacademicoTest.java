package br.edu.sistemaacademico.domain;

import br.edu.sistemaacademico.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SistemaAcademicoTest {

    private Aluno aluno;
    private Disciplina disciplinaPoo;
    private Disciplina disciplinaBd;
    private PeriodoLetivo periodo2026_1;
    private PeriodoLetivo periodo2026_2;
    private Turma turmaA;
    private Turma turmaB;

    @BeforeEach
    void setUp() {
        // Arrange
        aluno = new Aluno("Ana Silva", "RA12345", "ana.silva@email.com");
        disciplinaPoo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        disciplinaBd = new Disciplina("BD", "Banco de Dados", 80);

        periodo2026_1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        periodo2026_2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        turmaA = new Turma("TURMA-A", periodo2026_1);
        turmaB = new Turma("TURMA-B", periodo2026_2);
    }

    @Test
    @DisplayName("Deve ofertar disciplina com sucesso e listar na turma")
    void deveOfertarDisciplinaComSucesso() {
        // Act
        OfertaDisciplina ofertaPoo = turmaA.ofertarDisciplina(disciplinaPoo);
        OfertaDisciplina ofertaBd = turmaA.ofertarDisciplina(disciplinaBd);

        // Assert
        assertEquals(2, turmaA.getOfertas().size());
        assertTrue(turmaA.getOfertas().contains(ofertaPoo));
        assertTrue(turmaA.getOfertas().contains(ofertaBd));
        assertEquals(disciplinaPoo, ofertaPoo.getDisciplina());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ofertar a mesma disciplina duas vezes na mesma turma")
    void deveLancarExcecaoAoOfertarDisciplinaDuplicadaNaMesmaTurma() {
        // Arrange
        turmaA.ofertarDisciplina(disciplinaPoo);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> turmaA.ofertarDisciplina(disciplinaPoo)
        );

        assertTrue(exception.getMessage().contains("já está ofertada nesta turma"));
    }

    @Test
    @DisplayName("Deve matricular aluno com sucesso e alterar seu estado para Cursando")
    void deveMatricularAlunoComSucesso() {
        // Arrange
        OfertaDisciplina oferta = turmaA.ofertarDisciplina(disciplinaPoo);

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertEquals(ResultadoAcademico.CURSANDO, matricula.getResultado());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOferta());
    }

    @Test
    @DisplayName("Deve lançar exceção ao matricular aluno duas vezes na mesma oferta")
    void deveLancarExcecaoAoMatricularAlunoDuplicadoNaMesmaOferta() {
        // Arrange
        OfertaDisciplina oferta = turmaA.ofertarDisciplina(disciplinaPoo);
        oferta.matricular(aluno);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(aluno)
        );

        assertTrue(exception.getMessage().contains("já está matriculado nesta oferta"));
    }

    @Test
    @DisplayName("Deve permitir nova matrícula na mesma disciplina após reprovação anterior")
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        OfertaDisciplina oferta2026_1 = turmaA.ofertarDisciplina(disciplinaPoo);
        Matricula primeiraMatricula = oferta2026_1.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        OfertaDisciplina oferta2026_2 = turmaB.ofertarDisciplina(disciplinaPoo);

        // Act
        Matricula segundaMatricula = oferta2026_2.matricular(aluno);

        // Assert
        assertEquals(2, aluno.getMatriculas().size());
        assertEquals(ResultadoAcademico.REPROVADO, primeiraMatricula.getResultado());
        assertEquals(ResultadoAcademico.CURSANDO, segundaMatricula.getResultado());
    }

    @Test
    @DisplayName("Deve bloquear nova matrícula em disciplina na qual o aluno já foi aprovado")
    void deveBloquearNovaMatriculaAposAprovacao() {
        // Arrange
        OfertaDisciplina oferta2026_1 = turmaA.ofertarDisciplina(disciplinaPoo);
        Matricula primeiraMatricula = oferta2026_1.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        OfertaDisciplina oferta2026_2 = turmaB.ofertarDisciplina(disciplinaPoo);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> oferta2026_2.matricular(aluno)
        );

        assertTrue(exception.getMessage().contains("já foi aprovado na disciplina"));
    }

    @Test
    @DisplayName("Deve verificar saídas em texto dos métodos toString e getters de suporte")
    void deveValidarComportamentosAuxiliaresEGetters() {
        // Arrange
        OfertaDisciplina oferta = turmaA.ofertarDisciplina(disciplinaPoo);
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertEquals("Ana Silva", aluno.getNome());
        assertEquals("POO", disciplinaPoo.getSigla());
        assertEquals("Programação Orientada a Objetos", disciplinaPoo.getNome());
        assertEquals(80, disciplinaPoo.getCargaHoraria());
        assertEquals("TURMA-A", turmaA.getCodigo());
        assertEquals(2026, periodo2026_1.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo2026_1.getSemestre());

        // Testes do método toString() das classes de domínio

        assertEquals("Programação Orientada a Objetos", disciplinaPoo.toString());
        assertEquals("Programação Orientada a Objetos", oferta.toString());
        assertTrue(matricula.toString().contains("Ana Silva"));
        assertTrue(matricula.toString().contains("Programação Orientada a Objetos"));
    }
}