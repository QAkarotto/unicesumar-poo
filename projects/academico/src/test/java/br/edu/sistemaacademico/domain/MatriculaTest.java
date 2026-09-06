package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    @Test
    void deveMatricularAluno() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertNotNull(matricula);
        assertEquals(aluno, matricula.getAluno());
        assertEquals(disciplina, matricula.getDisciplina());
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void naoDevePermitirMatriculaDuplicada() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );
    }

    @Test
    void deveRegistrarReprovacao() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo1 =
                new PeriodoLetivo(2025, Semestre.SEGUNDO);

        Turma turma1 =
                new Turma("ESOFT4S-NB", periodo1);

        OfertaDisciplina oferta1 =
                turma1.ofertarDisciplina(disciplina);

        Matricula primeiraMatricula =
                oferta1.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma2 =
                new Turma("ESOFT4S-NB", periodo2);

        OfertaDisciplina oferta2 =
                turma2.ofertarDisciplina(disciplina);

        // Act
        Matricula novaMatricula =
                oferta2.matricular(aluno);

        // Assert
        assertNotNull(novaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void naoDevePermitirNovaMatriculaAposAprovacao() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo1 =
                new PeriodoLetivo(2025, Semestre.SEGUNDO);

        Turma turma1 =
                new Turma("ESOFT4S-NB", periodo1);

        OfertaDisciplina oferta1 =
                turma1.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta1.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        PeriodoLetivo periodo2 =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma2 =
                new Turma("ESOFT4S-NB", periodo2);

        OfertaDisciplina oferta2 =
                turma2.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta2.matricular(aluno)
        );

        // Nem as Esferas do Dragão liberam nova matrícula após a aprovação.
    }

    @Test
    void naoDeveConcluirMatriculaDuasVezes() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(
                        ResultadoAcademico.REPROVADO
                )
        );
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        // Arrange
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        // Arrange
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );
    }
}