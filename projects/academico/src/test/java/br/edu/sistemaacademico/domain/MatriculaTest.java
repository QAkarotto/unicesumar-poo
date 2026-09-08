package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Matrícula")
class MatriculaTest {

    private Aluno paola;
    private Turma turma;
    private OfertaDisciplina ofertaPoo;

    @BeforeEach
    void preparar() {
        paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        ofertaPoo = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );
    }

    @Test
    @DisplayName("Deve nascer ativa e sem resultado acadêmico")
    void deveNascerAtivaESemResultado() {
        // Act
        var matricula = new Matricula("  MAT-1  ", paola, ofertaPoo);

        // Assert
        assertEquals("MAT-1", matricula.getCodigo());
        assertSame(paola, matricula.getAluno());
        assertSame(ofertaPoo, matricula.getOfertaDisciplina());
        assertSame(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve recusar código, aluno ou oferta inválidos")
    void deveRecusarDadosObrigatorios() {
        OfertaDisciplina ofertaNula = null;
        Turma turmaNula = null;

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, paola, ofertaPoo)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("   ", paola, ofertaPoo)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", null, ofertaPoo)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", paola, ofertaNula)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula("MAT-1", paola, turmaNula)
        );
    }

    @Test
    @DisplayName("Deve permitir matricular a partir de uma turma com oferta única")
    void devePermitirMatricularPelaTurma() {
        // Act
        var matricula = new Matricula("MAT-1", paola, turma);

        // Assert
        assertSame(ofertaPoo, matricula.getOfertaDisciplina());
        assertEquals(1, ofertaPoo.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve concluir a matrícula registrando o resultado acadêmico")
    void deveConcluirComResultado() {
        // Arrange
        var matricula = ofertaPoo.matricular(paola);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve recusar a conclusão sem resultado acadêmico e manter a matrícula ativa")
    void deveRecusarConclusaoSemResultado() {
        // Arrange
        var matricula = ofertaPoo.matricular(paola);

        // Act
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve trancar uma matrícula ativa")
    void deveTrancarMatriculaAtiva() {
        // Arrange
        var matricula = ofertaPoo.matricular(paola);

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar uma matrícula ativa")
    void deveCancelarMatriculaAtiva() {
        // Arrange
        var matricula = ofertaPoo.matricular(paola);

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Não deve concluir duas vezes a mesma matrícula")
    void naoDeveConcluirDuasVezes() {
        // Arrange
        var matricula = ofertaPoo.matricular(paola);
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Act
        var erro = assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );

        // Assert
        assertEquals(
                "Não é possível concluir uma matrícula CONCLUIDA.",
                erro.getMessage()
        );
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Não deve trancar ou cancelar uma matrícula que não está ativa")
    void naoDeveAlterarMatriculaInativa() {
        // Arrange
        var trancada = ofertaPoo.matricular(paola);
        trancada.trancar();

        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        var cancelada = ofertaPoo.matricular(bruno);
        cancelada.cancelar();

        // Act / Assert
        assertThrows(IllegalStateException.class, trancada::trancar);
        assertThrows(IllegalStateException.class, trancada::cancelar);
        assertThrows(
                IllegalStateException.class,
                () -> trancada.concluir(ResultadoAcademico.APROVADO)
        );
        assertThrows(IllegalStateException.class, cancelada::cancelar);
        assertThrows(IllegalStateException.class, cancelada::trancar);

        assertEquals(SituacaoMatricula.TRANCADA, trancada.getSituacao());
        assertEquals(SituacaoMatricula.CANCELADA, cancelada.getSituacao());
    }

    @Test
    @DisplayName("Deve apresentar código, aluno, disciplina e situação no toString")
    void deveApresentarRepresentacaoTextual() {
        // Arrange
        var matricula = ofertaPoo.matricular("MAT-1", paola);

        // Assert
        assertEquals("MAT-1 - RA2026001 - POO - ATIVA", matricula.toString());
    }

    @Test
    @DisplayName("Deve prever apenas as situações e resultados definidos no domínio")
    void devePreverApenasEstadosDoDominio() {
        assertEquals(4, SituacaoMatricula.values().length);
        assertEquals(2, ResultadoAcademico.values().length);
        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                SituacaoMatricula.valueOf("CONCLUIDA")
        );
        assertEquals(
                ResultadoAcademico.REPROVADO,
                ResultadoAcademico.valueOf("REPROVADO")
        );
    }
}
