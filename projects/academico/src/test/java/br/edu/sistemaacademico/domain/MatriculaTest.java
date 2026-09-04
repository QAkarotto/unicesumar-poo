package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno criarAluno() {
        return new Aluno(
                "A001",
                "Douglas",
                "douglas@email.com"
        );
    }

    private OfertaDisciplina criarOferta() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        Turma turma =
                new Turma(
                        "ESOFT4S-NB",
                        periodo
                );

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        return turma.ofertarDisciplina(disciplina);
    }

    @Test
    void deveCriarMatriculaValida() {
        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula =
                new Matricula(
                        "M001",
                        aluno,
                        oferta
                );

        assertEquals("M001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(oferta.getTurma(), matricula.getTurma());
        assertEquals(
                SituacaoMatricula.ATIVA,
                matricula.getSituacao()
        );
        assertNull(matricula.getResultado());
    }

    @Test
    void naoDeveCriarMatriculaSemCodigo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "",
                        criarAluno(),
                        criarOferta()
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaComCodigoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        null,
                        criarAluno(),
                        criarOferta()
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaSemAluno() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "M001",
                        null,
                        criarOferta()
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaSemOferta() {
        Aluno aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "M001",
                        aluno,
                        (OfertaDisciplina) null
                )
        );
    }

    @Test
    void deveRemoverEspacosDoCodigo() {
        Matricula matricula =
                new Matricula(
                        "  M001  ",
                        criarAluno(),
                        criarOferta()
                );

        assertEquals("M001", matricula.getCodigo());
    }

    @Test
    void deveConcluirMatriculaComoAprovado() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );

        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );
    }

    @Test
    void deveConcluirMatriculaComoReprovado() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );

        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    void naoDeveConcluirMatriculaDuasVezes() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(
                        ResultadoAcademico.REPROVADO
                )
        );
    }

    @Test
    void deveTrancarMatricula() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.trancar();

        assertEquals(
                SituacaoMatricula.TRANCADA,
                matricula.getSituacao()
        );
    }

    @Test
    void deveCancelarMatricula() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.cancelar();

        assertEquals(
                SituacaoMatricula.CANCELADA,
                matricula.getSituacao()
        );
    }

    @Test
    void naoDeveTrancarMatriculaDepoisDeConcluida() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void naoDeveCancelarMatriculaDepoisDeConcluida() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void naoDeveCancelarMatriculaDepoisDeTrancada() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.trancar();

        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void naoDeveTrancarMatriculaDepoisDeCancelada() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        matricula.cancelar();

        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void deveRetornarToStringCorreto() {
        Matricula matricula =
                new Matricula(
                        "M001",
                        criarAluno(),
                        criarOferta()
                );

        assertEquals(
                "M001 - A001 - POO - ATIVA",
                matricula.toString()
        );
    }
}