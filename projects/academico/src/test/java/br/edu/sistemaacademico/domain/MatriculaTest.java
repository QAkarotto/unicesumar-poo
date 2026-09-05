package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private static Turma novaTurmaComOferta(String codigoDisciplina) {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(
                new Disciplina(codigoDisciplina, "Programação Orientada a Objetos", 80)
        );
        return turma;
    }

    @Test
    @DisplayName("Deve matricular um aluno a partir da turma quando há uma única oferta")
    void deveMatricularAlunoAPartirDaTurma() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");

        var matricula = new Matricula("MAT-001", aluno, turma);

        assertEquals("MAT-001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve rejeitar código nulo ou em branco")
    void deveRejeitarCodigoInvalido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");

        assertThrows(IllegalArgumentException.class, () -> new Matricula(null, aluno, turma));
        assertThrows(IllegalArgumentException.class, () -> new Matricula(" ", aluno, turma));
    }

    @Test
    @DisplayName("Deve rejeitar aluno nulo")
    void deveRejeitarAlunoNulo() {
        var turma = novaTurmaComOferta("POO");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", null, turma)
        );
    }

    @Test
    @DisplayName("Deve rejeitar turma nula")
    void deveRejeitarTurmaNula() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", aluno, (Turma) null)
        );
    }

    @Test
    @DisplayName("Deve rejeitar oferta de disciplina nula")
    void deveRejeitarOfertaNula() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-001", aluno, (OfertaDisciplina) null)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao matricular por turma sem nenhuma oferta")
    void deveLancarExcecaoAoMatricularPorTurmaSemOferta() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-001", aluno, turma)
        );
    }

    @Test
    @DisplayName("Deve impedir nova matrícula quando o aluno já foi aprovado na disciplina")
    void deveImpedirMatriculaQuandoJaAprovadoNaDisciplina() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var primeiraTurma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var primeiraOferta = primeiraTurma.ofertarDisciplina(disciplina);
        var primeiraMatricula = primeiraOferta.matricular("MAT-001", aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        var segundaTurma = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var segundaOferta = segundaTurma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalStateException.class,
                () -> segundaOferta.matricular("MAT-002", aluno)
        );
    }

    @Test
    @DisplayName("Deve concluir uma matrícula ativa com o resultado informado")
    void deveConcluirMatriculaAtiva() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve rejeitar conclusão sem resultado acadêmico")
    void deveRejeitarConclusaoSemResultado() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);

        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    @DisplayName("Deve trancar uma matrícula ativa")
    void deveTrancarMatriculaAtiva() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar uma matrícula ativa")
    void deveCancelarMatriculaAtiva() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Não deve permitir concluir, trancar ou cancelar uma matrícula que não está ativa")
    void naoDevePermitirOperacoesEmMatriculaNaoAtiva() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);
        matricula.cancelar();

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
        assertThrows(IllegalStateException.class, matricula::trancar);
        assertThrows(IllegalStateException.class, matricula::cancelar);
    }

    @Test
    @DisplayName("foiAprovadoEm deve retornar falso para matrícula ainda ativa ou reprovada")
    void foiAprovadoEmDeveRetornarFalsoQuandoNaoAprovado() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular("MAT-001", aluno);

        assertFalse(matricula.foiAprovadoEm(disciplina));

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertFalse(matricula.foiAprovadoEm(disciplina));
    }

    @Test
    @DisplayName("O toString deve conter código, aluno, disciplina e situação")
    void toStringDeveConterCodigoAlunoDisciplinaESituacao() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = novaTurmaComOferta("POO");
        var matricula = new Matricula("MAT-001", aluno, turma);

        assertTrue(matricula.toString().contains("MAT-001"));
        assertTrue(matricula.toString().contains("RA001"));
        assertTrue(matricula.toString().contains("POO"));
        assertTrue(matricula.toString().contains("ATIVA"));
    }
}
