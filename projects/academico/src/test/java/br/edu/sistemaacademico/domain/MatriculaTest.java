package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno criarAluno() {
        return new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    private Turma criarTurma(String codigo) {
        var periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        return new Turma(codigo, periodo);
    }

    @Test
    void deveCriarMatriculaAtiva() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());

        // Act
        var matricula = oferta.matricular("MAT001", aluno);

        // Assert
        assertEquals("MAT001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());

        assertTrue(aluno.getMatriculas().contains(matricula));
        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    void deveConcluirMatriculaComAprovacao() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void deveConcluirMatriculaComReprovacao() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        var aluno = criarAluno();
        var disciplina = criarDisciplina();

        var turma1 = criarTurma("ESOFT4S-NA");
        var oferta1 = turma1.ofertarDisciplina(disciplina);

        var primeiraMatricula = oferta1.matricular("MAT001", aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        var turma2 = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        // Act
        var segundaMatricula = oferta2.matricular("MAT002", aluno);

        // Assert
        assertNotNull(segundaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
        assertEquals(SituacaoMatricula.ATIVA, segundaMatricula.getSituacao());
    }

    @Test
    void deveBloquearNovaMatriculaAposAprovacao() {
        // Arrange
        var aluno = criarAluno();
        var disciplina = criarDisciplina();

        var turma1 = criarTurma("ESOFT4S-NA");
        var oferta1 = turma1.ofertarDisciplina(disciplina);

        var primeiraMatricula = oferta1.matricular("MAT001", aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        var turma2 = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var oferta2 = turma2.ofertarDisciplina(disciplina);

        // Act / Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta2.matricular("MAT002", aluno)
        );
    }

    @Test
    void deveBloquearMatriculaDuplicadaNaMesmaOferta() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());

        oferta.matricular("MAT001", aluno);

        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MAT002", aluno)
        );
    }

    @Test
    void deveTrancarMatriculaAtiva() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    void deveCancelarMatriculaAtiva() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    void naoDeveAlterarMatriculaDepoisDeConcluida() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act / Assert
        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );
    }

    @Test
    void deveRejeitarResultadoNuloAoConcluir() {
        // Arrange
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());
        var matricula = oferta.matricular("MAT001", aluno);

        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    void deveRejeitarCodigoDeMatriculaVazio() {
        var aluno = criarAluno();
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("", aluno)
        );
    }

    @Test
    void deveRejeitarAlunoNulo() {
        var turma = criarTurma("ESOFT4S-NA");
        var oferta = turma.ofertarDisciplina(criarDisciplina());

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT001", null, oferta)
        );
    }

    @Test
    void deveRejeitarOfertaNula() {
        var aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "MAT001",
                        aluno,
                        (OfertaDisciplina) null
                )
        );
    }
}