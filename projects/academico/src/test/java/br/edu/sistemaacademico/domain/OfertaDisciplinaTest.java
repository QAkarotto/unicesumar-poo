package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        // Arrange
        var aluno = new Aluno("RA002", "Alexandre Gaia", "alexandre@email.com");
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 40)
        );
        oferta.matricular(aluno);

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao matricular aluno nulo")
    void deveLancarExcecaoQuandoAlunoForNulo() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(null));
    }

    @Test
    @DisplayName("Deve impedir nova matrícula quando aluno já foi aprovado na disciplina")
    void deveImpedirMatriculaQuandoAlunoJaAprovado() {
        // Arrange
        var aluno = new Aluno("RA003", "Bruno Costa", "bruno@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var turmaAntiga = new Turma("TURMA-A", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var ofertaAntiga = turmaAntiga.ofertarDisciplina(disciplina);
        var matriculaAntiga = ofertaAntiga.matricular(aluno);
        matriculaAntiga.concluir(ResultadoAcademico.APROVADO);

        var turmaNova = new Turma("TURMA-B", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaNova = turmaNova.ofertarDisciplina(disciplina);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> ofertaNova.matricular(aluno));
    }

    @Test
    @DisplayName("Deve representar a oferta com disciplina e código da turma")
    void toStringDeveConterDisciplinaETurma() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act
        var texto = oferta.toString();

        // Assert
        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("ESOFT4S-NA"));
    }
}
