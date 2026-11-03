
package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaSituacaoTest {

    private Matricula criarMatricula() {
        Aluno aluno = new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        return oferta.matricular("MAT-001", aluno);
    }

    @Test
    void deveCriarMatriculaAtiva() {
        Matricula matricula = criarMatricula();

        assertEquals("MAT-001", matricula.getCodigo());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertEquals("RA001", matricula.getAluno().getRegistroAcademico());
        assertEquals("ESOFT4S-NA", matricula.getTurma().getCodigo());
        assertEquals(
                "POO",
                matricula.getOfertaDisciplina().getDisciplina().getCodigo()
        );
    }

    @Test
    void deveTrancarMatriculaAtiva() {
        Matricula matricula = criarMatricula();

        matricula.trancar();

        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
        assertNull(matricula.getResultado());
    }

    @Test
    void deveCancelarMatriculaAtiva() {
        Matricula matricula = criarMatricula();

        matricula.cancelar();

        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    void deveImpedirTrancamentoDuplicado() {
        Matricula matricula = criarMatricula();
        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void deveImpedirCancelamentoDeMatriculaConcluida() {
        Matricula matricula = criarMatricula();
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void deveImpedirConclusaoDeMatriculaCancelada() {
        Matricula matricula = criarMatricula();
        matricula.cancelar();

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    void deveRejeitarConclusaoSemResultado() {
        Matricula matricula = criarMatricula();

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    void deveRejeitarCodigoVazio() {
        Matricula matricula = criarMatricula();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "",
                        matricula.getAluno(),
                        matricula.getOfertaDisciplina()
                )
        );
    }

    @Test
    void deveRejeitarAlunoNulo() {
        Matricula matricula = criarMatricula();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "MAT-002",
                        null,
                        matricula.getOfertaDisciplina()
                )
        );
    }

    @Test
    void deveRejeitarOfertaNula() {
        Aluno aluno = new Aluno(
                "RA002",
                "Bruno Santos",
                "bruno@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "MAT-002",
                        aluno,
                        (OfertaDisciplina) null
                )
        );
    }
}
