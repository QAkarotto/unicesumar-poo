package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    private final Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

    @Test
    void deveMatricularAlunoNaOferta() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(null));
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaDoMesmoAlunoNaMesmaOferta() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        oferta.matricular(aluno);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> oferta.matricular(aluno));
    }

    @Test
    void devePermitirMatriculaDeAlunosDiferentesNaMesmaOferta() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno1 = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var aluno2 = new Aluno("RA002", "João Souza", "joao@email.com");

        // Act
        oferta.matricular(aluno1);
        oferta.matricular(aluno2);

        // Assert
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    void naoDevePermitirNovaMatriculaDeAlunoJaAprovadoNaDisciplinaMesmoEmOutraTurma() {
        // Arrange
        var turma1 = new Turma("T1", periodo);
        var oferta1 = turma1.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matriculaAnterior = oferta1.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.APROVADO);

        var turma2 = new Turma("T2", periodo);
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> oferta2.matricular(aluno));
    }

    @Test
    void devePermitirNovaMatriculaDeAlunoReprovadoNaDisciplinaMesmoEmOutraTurma() {
        // Arrange
        var turma1 = new Turma("T1", periodo);
        var oferta1 = turma1.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matriculaAnterior = oferta1.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.REPROVADO);

        var turma2 = new Turma("T2", periodo);
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        // Act
        var novaMatricula = oferta2.matricular(aluno);

        // Assert
        assertEquals(aluno, novaMatricula.getAluno());
        assertEquals(oferta2, novaMatricula.getOfertaDisciplina());
    }

    @Test
    void getMatriculasNaoDeveExporColecaoInternaParaModificacao() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var matricula = oferta.matricular(aluno);

        // Act
        var matriculas = oferta.getMatriculas();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(matricula));
    }

    @Test
    void toStringDeveConterDisciplinaECodigoDaTurma() {
        // Arrange
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);

        // Act
        var texto = oferta.toString();

        // Assert
        assertTrue(texto.contains("Programação Orientada a Objetos"));
        assertTrue(texto.contains("T1"));
    }
}
