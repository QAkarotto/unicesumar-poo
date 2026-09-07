package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private Turma criarTurma() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        return new Turma("T01", periodo);
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    private Aluno criarAluno() {
        return new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );
    }

    @Test
    void deveCriarOfertaDeDisciplina() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        // Act
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Assert
        assertNotNull(oferta);
        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    void deveRejeitarAlunoNulo() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );
    }

    @Test
    void deveMatricularAluno() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno = criarAluno();

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertNotNull(matricula);
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno = criarAluno();

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );
    }

    @Test
    void listaDeMatriculasDeveSerSomenteParaLeitura() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear()
        );
    }

    @Test
    void deveGerarTextoDaOferta() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        // Act
        String resultado = oferta.toString();

        // Assert
        assertTrue(resultado.contains("POO"));
        assertTrue(resultado.contains("Programação"));
    }
}