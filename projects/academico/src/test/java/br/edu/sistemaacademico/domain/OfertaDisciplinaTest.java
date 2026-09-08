package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("Deve gerar códigos sequenciais quando a matrícula é feita sem código")
    void deveGerarCodigoSequencialDeMatricula() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("BD", "Banco de Dados", 80)
        );
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno@email.com");

        // Act
        var primeira = oferta.matricular(paola);
        var segunda = oferta.matricular(bruno);

        // Assert
        assertEquals("MAT-1", primeira.getCodigo());
        assertEquals("MAT-2", segunda.getCodigo());
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve manter alunos diferentes matriculados na mesma oferta")
    void devePermitirVariosAlunosNaMesmaOferta() {
        // Arrange
        var turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("SO", "Sistemas Operacionais", 60)
        );
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno@email.com");

        // Act
        var matriculaPaola = oferta.matricular("MAT-A", paola);
        var matriculaBruno = oferta.matricular("MAT-B", bruno);

        // Assert
        assertTrue(oferta.getMatriculas().contains(matriculaPaola));
        assertTrue(oferta.getMatriculas().contains(matriculaBruno));
        assertSame(oferta, matriculaPaola.getOfertaDisciplina());
        assertSame(turma, matriculaBruno.getTurma());
    }

    @Test
    @DisplayName("Deve expor as matrículas como coleção imutável")
    void deveExporMatriculasImutaveis() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
        var matricula = oferta.matricular(
                new Aluno("RA2026001", "Paola Oliveira", "paola@email.com")
        );

        // Act
        var matriculas = oferta.getMatriculas();

        // Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> matriculas.add(matricula)
        );
    }

    @Test
    @DisplayName("Deve apresentar disciplina e turma no toString")
    void deveApresentarRepresentacaoTextual() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        assertEquals("POO - ESOFT4S-NA - 2026/2", oferta.toString());
    }
}
