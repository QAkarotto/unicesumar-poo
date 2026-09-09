package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfertaDisciplinaTest {

    @Test
    @DisplayName("Deve registrar a mesma matrícula no aluno e na oferta")
    void deveRegistrarMatriculaNoAlunoENaOferta() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertSame(matricula, oferta.getMatriculas().get(0));
        assertSame(matricula, aluno.getMatriculas().get(0));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        var aluno = new Aluno("RA002", "Alexandre Gaia", "alexandre@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 40)
        );
        oferta.matricular("MATRICULA-001", aluno);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MATRICULA-002", aluno)
        );
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve matricular alunos diferentes na mesma oferta")
    void deveMatricularAlunosDiferentes() {
        // Arrange
        var oferta = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO))
                .ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act
        oferta.matricular(new Aluno("RA003", "Caio Bertoldi", "caio@email.com"));
        oferta.matricular(new Aluno("RA004", "Larissa Prado", "larissa@email.com"));

        // Assert
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve devolver a lista de matrículas como cópia protegida")
    void deveProtegerAListaDeMatriculas() {
        var oferta = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO))
                .ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(new Aluno("RA005", "Tainá Moraes", "taina@email.com"));

        var matriculas = oferta.getMatriculas();

        assertThrows(UnsupportedOperationException.class, () -> matriculas.remove(matricula));
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve identificar a oferta pela disciplina e pela turma")
    void toStringDescreveAOferta() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        assertEquals("POO - ESOFT4S-NA - 2026/2", oferta.toString());
    }
}
