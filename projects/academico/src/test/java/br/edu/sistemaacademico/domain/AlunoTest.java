package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    @DisplayName("Deve permitir nova matrícula após reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        var aluno = new Aluno("RA001", "Danilo Machado", "danilo@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var turma1 = new Turma("ESOFT4S-NA",disciplina,new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2 = new Turma("ESOFT4S-NB", disciplina, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = turma1.getOfertas().get(0).matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        var novaMatricula = turma2.getOfertas().get(0).matricular(aluno);

        // Assert
        assertEquals(2, aluno.getMatriculas().size());
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir nova matrícula após aprovação")
    void deveImpedirNovaMatriculaAposAprovacao() {
        // Arrange
        var aluno = new Aluno("RA002", "Doris Daniella", "doris@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var turma1 = new Turma("ESOFT4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2 = new Turma("ESOFT4S-NB", disciplina, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var matricula = turma1.getOfertas().get(0).matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act
        var excecao = assertThrows(
                IllegalStateException.class,
                () -> turma2.getOfertas().get(0).matricular(aluno)
        );

        // Assert
        assertEquals("O aluno já foi aprovado nesta disciplina.", excecao.getMessage());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve registrar a mesma matrícula criada")
    void deveRegistrarMesmaMatriculaCriada() {
        // Arrange
        var aluno = new Aluno("RA001", "Danilo Machado", "danilo@email.com");
        var turma = new Turma("ESOFT4S-NA", new Disciplina("POO", "Programação Orientada a Objetos", 80), new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.getOfertas().get(0);

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertSame(matricula, aluno.getMatriculas().get(0));
    }

    @Test
    @DisplayName("Deve permitir alterar o e-mail") void devePermitirAlterarEmail() {
        // Arrange
        var aluno = new Aluno( "RA002", "Doris Daniella", "doris@email.com" );

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve considerar alunos com registros diferentes como diferentes")
    void deveConsiderarAlunosComRegistrosDiferentesComoDiferentes() {
        // Arrange
        var aluno1 = new Aluno( "RA006", "Danilo Machado", "danilo@email.com" );
        var aluno2 = new Aluno( "RA007", "Danilo Machado", "danilo@email.com" );

        // Act
        var diferentes = aluno1.equals(aluno2);

        // Assert
        assertFalse(diferentes);
    }
}
