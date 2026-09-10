package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno novoAluno() {
        return new Aluno("RA001", "Ana Souza", "ana@email.com");
    }

    private Disciplina novaDisciplina() {
        return new Disciplina("POO", "Programação Orientada a Objetos", 80);
    }

    private Turma novaTurma(String codigo, int ano, Semestre semestre) {
        return new Turma(codigo, new PeriodoLetivo(ano, semestre));
    }

    @Test
    void deveConcluirMatriculaComAprovacao() {
        // Arrange
        var aluno = novoAluno();
        var disciplina = novaDisciplina();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular("MAT-001", aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());
        assertSame(turma, matricula.getTurma());
    }

    @Test
    void deveConcluirMatriculaComReprovacao() {
        // Arrange
        var aluno = novoAluno();
        var disciplina = novaDisciplina();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        var aluno = novoAluno();
        var disciplina = novaDisciplina();

        var turma2026 = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta2026 = turma2026.ofertarDisciplina(disciplina);
        var primeiraMatricula = oferta2026.matricular("MAT-001", aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        var turma2027 = novaTurma("ESOFT4S-NB", 2027, Semestre.PRIMEIRO);
        var oferta2027 = turma2027.ofertarDisciplina(disciplina);

        // Act
        var novaMatricula = oferta2027.matricular("MAT-002", aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
        assertNull(novaMatricula.getResultado());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void deveBloquearNovaMatriculaAposAprovacao() {
        // Arrange
        var aluno = novoAluno();
        var disciplina = novaDisciplina();

        var turma2026 = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta2026 = turma2026.ofertarDisciplina(disciplina);
        var primeiraMatricula = oferta2026.matricular("MAT-001", aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        var turma2027 = novaTurma("ESOFT4S-NB", 2027, Semestre.PRIMEIRO);
        var oferta2027 = turma2027.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> oferta2027.matricular("MAT-002", aluno)
        );

        assertEquals(1, aluno.getMatriculas().size());
        assertTrue(oferta2027.getMatriculas().isEmpty());
    }

    @Test
    void deveImpedirMatriculaDuplicadaNaMesmaOferta() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(novaDisciplina());
        oferta.matricular("MAT-001", aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MAT-002", aluno)
        );

        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void deveTrancarMatriculaAtivaEImpedirNovaAlteracao() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var matricula = turma.ofertarDisciplina(novaDisciplina()).matricular(aluno);

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    void deveCancelarMatriculaAtivaEImpedirNovoTrancamento() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var matricula = turma.ofertarDisciplina(novaDisciplina()).matricular(aluno);

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
        assertThrows(IllegalStateException.class, matricula::trancar);
    }

    @Test
    void deveImpedirAlteracaoDepoisDeConcluida() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var matricula = turma.ofertarDisciplina(novaDisciplina()).matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // O Mafuba do Mestre Kame serve de lembrete: depois de concluída, a matrícula não volta ao estado ativo.
        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );
        assertThrows(IllegalStateException.class, matricula::trancar);
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    void deveRejeitarConclusaoSemResultado() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var matricula = turma.ofertarDisciplina(novaDisciplina()).matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    void deveCriarMatriculaUsandoTurmaComUmaUnicaOferta() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(novaDisciplina());

        // Act
        var matricula = new Matricula("MAT-001", aluno, turma);

        // Assert
        assertSame(oferta, matricula.getOfertaDisciplina());
        assertTrue(oferta.getMatriculas().contains(matricula));
        assertTrue(aluno.getMatriculas().contains(matricula));
    }

    @Test
    void deveRejeitarMatriculaViaTurmaSemOfertaOuComVariasOfertas() {
        // Arrange
        var aluno = novoAluno();

        var turmaSemOferta = novaTurma("T1", 2026, Semestre.SEGUNDO);

        var turmaComVariasOfertas = novaTurma("T2", 2026, Semestre.SEGUNDO);
        turmaComVariasOfertas.ofertarDisciplina(novaDisciplina());
        turmaComVariasOfertas.ofertarDisciplina(
                new Disciplina("BD", "Banco de Dados", 80)
        );

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-001", aluno, turmaSemOferta)
        );
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-002", aluno, turmaComVariasOfertas)
        );
    }

    @Test
    void deveRejeitarDadosObrigatoriosDaMatricula() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(novaDisciplina());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(" ", aluno, oferta)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, oferta)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-002", aluno, (OfertaDisciplina) null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-003", aluno, (Turma) null)
        );
    }

    @Test
    void listasDeMatriculasNaoDevemSerAlteradasExternamente() {
        // Arrange
        var aluno = novoAluno();
        var turma = novaTurma("ESOFT4S-NA", 2026, Semestre.SEGUNDO);
        var oferta = turma.ofertarDisciplina(novaDisciplina());
        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear()
        );
    }
}
