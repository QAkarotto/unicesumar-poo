package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTest {

    private static final Disciplina POO =
            new Disciplina("POO", "Programação Orientada a Objetos", 80);

    @Test
    @DisplayName("Matrícula nasce ativa e sem resultado acadêmico")
    void matriculaNasceAtiva() {
        // Arrange
        var aluno = new Aluno("RA100", "Carla Menezes", "carla@email.com");
        var oferta = ofertaDe(POO, "ESOFT4S-NA");

        // Act
        var matricula = oferta.matricular(aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertSame(aluno, matricula.getAluno());
        assertSame(oferta, matricula.getOfertaDisciplina());
    }

    @Test
    @DisplayName("Concluir com aprovação encerra a matrícula e guarda o resultado")
    void deveConcluirComAprovacao() {
        var aluno = new Aluno("RA101", "Diego Prado", "diego@email.com");
        var matricula = ofertaDe(POO, "ESOFT4S-NA").matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Aluno aprovado não pode se matricular de novo na mesma disciplina")
    void deveBloquearMatriculaAposAprovacao() {
        // Arrange
        var aluno = new Aluno("RA102", "Bruna Camargo", "bruna@email.com");
        var primeiraOferta = ofertaDe(POO, "ESOFT4S-NA");
        primeiraOferta.matricular(aluno).concluir(ResultadoAcademico.APROVADO);
        var ofertaDoSemestreSeguinte = ofertaDe(POO, "ESOFT5S-NA");

        // Act
        var erro = assertThrows(
                IllegalStateException.class,
                () -> ofertaDoSemestreSeguinte.matricular(aluno)
        );

        // Assert
        assertEquals("O aluno já foi aprovado nesta disciplina.", erro.getMessage());
        assertTrue(ofertaDoSemestreSeguinte.getMatriculas().isEmpty());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Aluno reprovado pode cursar a disciplina novamente")
    void devePermitirNovaMatriculaAposReprovacao() {
        var aluno = new Aluno("RA103", "Felipe Nogueira", "felipe@email.com");
        ofertaDe(POO, "ESOFT4S-NA")
                .matricular(aluno)
                .concluir(ResultadoAcademico.REPROVADO);

        var novaMatricula = ofertaDe(POO, "ESOFT5S-NA").matricular(aluno);

        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Aprovação em uma disciplina não bloqueia a matrícula em outra")
    void aprovacaoNaoBloqueiaOutraDisciplina() {
        var aluno = new Aluno("RA104", "Marina Lopes", "marina@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(POO)
                .matricular(aluno)
                .concluir(ResultadoAcademico.APROVADO);

        var matriculaEmBanco = turma
                .ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 60))
                .matricular(aluno);

        assertEquals(SituacaoMatricula.ATIVA, matriculaEmBanco.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Trancar e cancelar mudam a situação da matrícula ativa")
    void deveTrancarECancelar() {
        var trancada = ofertaDe(POO, "ESOFT4S-NA")
                .matricular(new Aluno("RA105", "Igor Salles", "igor@email.com"));
        var cancelada = ofertaDe(POO, "ESOFT4S-NB")
                .matricular(new Aluno("RA106", "Júlia Terra", "julia@email.com"));

        trancada.trancar();
        cancelada.cancelar();

        assertEquals(SituacaoMatricula.TRANCADA, trancada.getSituacao());
        assertEquals(SituacaoMatricula.CANCELADA, cancelada.getSituacao());
    }

    @Test
    @DisplayName("Matrícula trancada não pode ser concluída nem cancelada")
    void deveRecusarOperacoesEmMatriculaTrancada() {
        // Arrange
        var matricula = ofertaDe(POO, "ESOFT4S-NA")
                .matricular(new Aluno("RA107", "Otávio Reis", "otavio@email.com"));
        matricula.trancar();

        // Act
        var erro = assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );

        // Assert
        assertEquals("Não é possível concluir uma matrícula TRANCADA.", erro.getMessage());
        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Matrícula concluída não pode ser trancada nem reavaliada")
    void deveRecusarOperacoesEmMatriculaConcluida() {
        var matricula = ofertaDe(POO, "ESOFT4S-NA")
                .matricular(new Aluno("RA108", "Sabrina Alves", "sabrina@email.com"));
        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertThrows(IllegalStateException.class, matricula::trancar);
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Matrícula cancelada não volta a ser ativa")
    void deveRecusarOperacoesEmMatriculaCancelada() {
        var matricula = ofertaDe(POO, "ESOFT4S-NA")
                .matricular(new Aluno("RA109", "Vitor Hugo", "vitor@email.com"));
        matricula.cancelar();

        assertThrows(IllegalStateException.class, matricula::cancelar);
        assertThrows(IllegalStateException.class, matricula::trancar);
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Concluir exige um resultado acadêmico")
    void deveExigirResultadoAoConcluir() {
        var matricula = ofertaDe(POO, "ESOFT4S-NA")
                .matricular(new Aluno("RA110", "Renata Muniz", "renata@email.com"));

        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Dados obrigatórios da matrícula são validados na criação")
    void deveValidarDadosObrigatorios() {
        var aluno = new Aluno("RA111", "Tiago Franco", "tiago@email.com");
        var oferta = ofertaDe(POO, "ESOFT4S-NA");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("  ", aluno, oferta)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", null, oferta)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (OfertaDisciplina) null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", aluno, (Turma) null)
        );
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Matricular pela turma só funciona quando existe uma única oferta")
    void deveMatricularPelaTurmaComOfertaUnica() {
        // Arrange
        var aluno = new Aluno("RA112", "Leandro Pires", "leandro@email.com");
        var turma = new Turma("ESOFT4S-NA", POO, new PeriodoLetivo(2026, Semestre.SEGUNDO));

        // Act
        var matricula = new Matricula("MAT-1", aluno, turma);

        // Assert
        assertSame(turma, matricula.getTurma());
        assertEquals(POO, matricula.getOfertaDisciplina().getDisciplina());

        turma.ofertarDisciplina(new Disciplina("ES", "Engenharia de Software", 60));
        var outroAluno = new Aluno("RA113", "Paula Ferrari", "paula@email.com");
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-2", outroAluno, turma)
        );
    }

    @Test
    @DisplayName("Não é possível matricular em uma turma que ainda não ofertou disciplinas")
    void deveRecusarMatriculaEmTurmaSemOferta() {
        var aluno = new Aluno("RA117", "Gustavo Zani", "gustavo@email.com");
        var turmaVazia = new Turma("ESOFT4S-NC", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-1", aluno, turmaVazia)
        );
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Código da matrícula é gerado em sequência quando não informado")
    void deveGerarCodigoSequencial() {
        var oferta = ofertaDe(POO, "ESOFT4S-NA");

        var primeira = oferta.matricular(new Aluno("RA114", "Ana Clara", "anaclara@email.com"));
        var segunda = oferta.matricular(new Aluno("RA115", "Bento Rocha", "bento@email.com"));

        assertEquals("MAT-1", primeira.getCodigo());
        assertEquals("MAT-2", segunda.getCodigo());
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("toString da matrícula identifica aluno, disciplina e situação")
    void toStringDescreveAMatricula() {
        var matricula = ofertaDe(POO, "ESOFT4S-NA")
                .matricular("MAT-9", new Aluno("RA116", "Nina Duarte", "nina@email.com"));

        assertEquals("MAT-9 - RA116 - POO - ATIVA", matricula.toString());
    }

    private static OfertaDisciplina ofertaDe(Disciplina disciplina, String codigoTurma) {
        var turma = new Turma(codigoTurma, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        return turma.ofertarDisciplina(disciplina);
    }
}
