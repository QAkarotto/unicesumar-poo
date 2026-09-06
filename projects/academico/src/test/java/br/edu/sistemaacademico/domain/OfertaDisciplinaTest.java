package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private OfertaDisciplina criarOferta(Disciplina disciplina) {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.SEGUNDO);

        Turma turma =
                new Turma("ADS4S", periodo);

        return turma.ofertarDisciplina(disciplina);
    }

    @Test
    void deveMatricularAluno() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta =
                criarOferta(disciplina);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        // Act
        Matricula matricula =
                oferta.matricular(aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertTrue(oferta.getMatriculas().contains(matricula));
        assertTrue(aluno.getMatriculas().contains(matricula));
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertNull(matricula.getResultado());
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta =
                criarOferta(disciplina);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );

        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta =
                criarOferta(disciplina);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );
    }

    @Test
    void devePermitirRematriculaDepoisDeReprovacao() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        OfertaDisciplina primeiraOferta =
                criarOferta(disciplina);

        Matricula primeiraMatricula =
                primeiraOferta.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        Turma novaTurma =
                new Turma("ADS5S", novoPeriodo);

        OfertaDisciplina novaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        // Act
        Matricula novaMatricula =
                novaOferta.matricular(aluno);

        // Assert
        assertNotNull(novaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
        assertTrue(primeiraMatricula.foiReprovada());
        assertNull(novaMatricula.getResultado());
    }

    @Test
    void naoDevePermitirRematriculaDepoisDeAprovacao() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        OfertaDisciplina primeiraOferta =
                criarOferta(disciplina);

        Matricula primeiraMatricula =
                primeiraOferta.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.APROVADO
        );

        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        Turma novaTurma =
                new Turma("ADS5S", novoPeriodo);

        OfertaDisciplina novaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> novaOferta.matricular(aluno)
        );

        assertEquals(1, aluno.getMatriculas().size());
        assertTrue(aluno.foiAprovadoNaDisciplina(disciplina));
    }

    @Test
    void listaDeMatriculasNaoDevePermitirAlteracaoExterna() {
        // Arrange
        Disciplina disciplina =
                new Disciplina("BD", "Banco de Dados", 80);

        OfertaDisciplina oferta =
                criarOferta(disciplina);

        Aluno aluno =
                new Aluno("RA001", "João", "joao@email.com");

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear()
        );

        assertEquals(1, oferta.getMatriculas().size());
    }
}