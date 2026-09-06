package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfertaDisciplinaMatriculaTest {

    @Test
    @DisplayName("Deve gerar o codigo da matricula quando ele nao e informado")
    void deveGerarOCodigoDaMatriculaQuandoEleNaoEInformado() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Aluno ana = new Aluno("RA001", "Ana Souza", "ana@email.com");
        Aluno bia = new Aluno("RA002", "Bia Lima", "bia@email.com");

        // Act
        Matricula primeira = oferta.matricular(ana);
        Matricula segunda = oferta.matricular(bia);

        // Assert
        assertEquals("MAT-1", primeira.getCodigo());
        assertEquals("MAT-2", segunda.getCodigo());
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve matricular alunos diferentes na mesma oferta")
    void deveMatricularAlunosDiferentesNaMesmaOferta() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );

        // Act
        oferta.matricular("MAT-001", new Aluno("RA001", "Ana Souza", "ana@email.com"));
        oferta.matricular("MAT-002", new Aluno("RA002", "Bia Lima", "bia@email.com"));

        // Assert
        assertEquals(2, oferta.getMatriculas().size());
    }
}
