package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    // --- Helpers ---

    private Aluno criarAluno(String ra) {
        return new Aluno(ra, "Aluno " + ra, ra + "@email.com");
    }

    private OfertaDisciplina criarOferta(String codTurma) {
        Disciplina d = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        Turma t = new Turma(codTurma, d, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        return t.getOfertas().getFirst();
    }

    // --- Criação ---

    @Test
    void deveCriarMatriculaComSituacaoAtiva() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta("T01");

        // Act
        Matricula matricula = oferta.matricular("MAT001", aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultadoAcademico());
    }

    @Test
    void deveCriarMatriculaViaConstrutorComTurma() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);
        Turma turma = new Turma("T01", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        // Act
        Matricula matricula = new Matricula("MAT001", aluno, turma);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(turma, matricula.getTurma());
    }

    @Test
    void naoDeveCriarMatriculaSemCodigo() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta("T01");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("", aluno, oferta));
    }

    @Test
    void naoDeveCriarMatriculaComCodigoNulo() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta("T01");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(null, aluno, oferta));
    }

    @Test
    void naoDeveCriarMatriculaSemAluno() {
        // Arrange
        OfertaDisciplina oferta = criarOferta("T01");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT001", null, oferta));
    }

    @Test
    void naoDeveCriarMatriculaSemOferta() {
        // Arrange
        Aluno aluno = criarAluno("RA001");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT001", aluno, (OfertaDisciplina) null));
    }

    @Test
    void naoDeveCriarMatriculaSemTurma() {
        // Arrange
        Aluno aluno = criarAluno("RA001");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula("MAT001", aluno, (Turma) null));
    }

    // --- Conclusão ---

    @Test
    void deveConcluirMatriculaComoAprovado() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultadoAcademico());
    }

    @Test
    void deveConcluirMatriculaComoReprovado() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultadoAcademico());
    }

    @Test
    void naoDeveConcluirMatriculaSemResultado() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> matricula.concluir(null));
    }

    @Test
    void naoDeveConcluirMatriculaJaTrancada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.trancar();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
    }

    @Test
    void naoDeveConcluirMatriculaJaCancelada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.cancelar();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
    }

    @Test
    void naoDeveConcluirMatriculaJaConcluida() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
    }

    // --- Trancamento ---

    @Test
    void deveTrancarMatriculaAtiva() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    void naoDeveTrancarMatriculaJaTrancada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.trancar();

        // Act & Assert
        assertThrows(IllegalStateException.class, matricula::trancar);
    }

    @Test
    void naoDeveTrancarMatriculaCancelada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.cancelar();

        // Act & Assert
        assertThrows(IllegalStateException.class, matricula::trancar);
    }

    // --- Cancelamento ---

    @Test
    void deveCancelarMatriculaAtiva() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    void naoDeveCancelarMatriculaJaTrancada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.trancar();

        // Act & Assert
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    void naoDeveCancelarMatriculaJaCancelada() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);
        matricula.cancelar();

        // Act & Assert
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    // --- Regras de negócio: aprovação/reprovação ---

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange – aluno reprovado em POO no 1º semestre
        Aluno aluno = criarAluno("RA001");
        Disciplina poo = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        Turma turma1 = new Turma("T01", poo, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Matricula m1 = turma1.getOfertas().getFirst().matricular("MAT001", aluno);
        m1.concluir(ResultadoAcademico.REPROVADO);

        // Act – re-matrícula no 2º semestre (oferta diferente, mesma disciplina)
        Turma turma2 = new Turma("T02", poo, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        Matricula m2 = turma2.getOfertas().getFirst().matricular("MAT002", aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, m2.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void deveBlocarNovaMatriculaAposAprovacao() {
        // Arrange – aluno aprovado em POO
        Aluno aluno = criarAluno("RA001");
        Disciplina poo = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        Turma turma1 = new Turma("T01", poo, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Matricula m1 = turma1.getOfertas().getFirst().matricular("MAT001", aluno);
        m1.concluir(ResultadoAcademico.APROVADO);

        // Act & Assert – tenta se matricular novamente na mesma disciplina
        Turma turma2 = new Turma("T02", poo, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        assertThrows(IllegalStateException.class,
                () -> turma2.getOfertas().getFirst().matricular("MAT002", aluno));
    }

    @Test
    void deveImpedirMatriculaDuplicadaNaMesmaOferta() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        OfertaDisciplina oferta = criarOferta("T01");
        oferta.matricular("MAT001", aluno);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular("MAT002", aluno));
    }

    // --- toString ---

    @Test
    void deveRetornarToStringComInformacoesRelevantes() {
        // Arrange
        Aluno aluno = criarAluno("RA001");
        Matricula matricula = criarOferta("T01").matricular("MAT001", aluno);

        // Act
        String resultado = matricula.toString();

        // Assert
        assertTrue(resultado.contains("MAT001"));
        assertTrue(resultado.contains("RA001"));
        assertTrue(resultado.contains("ATIVA"));
    }
}
