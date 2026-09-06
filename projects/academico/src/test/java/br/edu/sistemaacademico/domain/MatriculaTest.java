package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private Aluno criarAluno() {
        return new Aluno(
                "RA001",
                "Paola Oliveira",
                "paola@email.com"
        );
    }

    private OfertaDisciplina criarOferta() {

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                );

        Turma turma =
                new Turma(
                        "TURMA-A",
                        periodo
                );

        return turma.ofertarDisciplina(
                disciplina
        );
    }

    @Test
    void deveCriarMatriculaValida() {

        Aluno aluno = criarAluno();
        OfertaDisciplina oferta = criarOferta();

        Matricula matricula =
                new Matricula(
                        aluno,
                        oferta
                );

        assertEquals(
                aluno,
                matricula.getAluno()
        );

        assertEquals(
                oferta,
                matricula.getOfertaDisciplina()
        );

        assertNull(
                matricula.getResultado()
        );
    }

    @Test
    void naoDeveCriarMatriculaSemAluno() {

        OfertaDisciplina oferta =
                criarOferta();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        null,
                        oferta
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaSemOferta() {

        Aluno aluno = criarAluno();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        aluno,
                        null
                )
        );
    }

    @Test
    void deveConcluirMatriculaComoAprovado() {

        Matricula matricula =
                new Matricula(
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
    }

    @Test
    void deveConcluirMatriculaComoReprovado() {

        Matricula matricula =
                new Matricula(
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
    }

    @Test
    void naoDeveConcluirComResultadoNulo() {

        Matricula matricula =
                new Matricula(
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

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void devePossuirToString() {

        Matricula matricula =
                new Matricula(
                        criarAluno(),
                        criarOferta()
                );

        String texto =
                matricula.toString();

        assertTrue(
                texto.contains("Paola Oliveira")
        );

        assertTrue(
                texto.contains("Programação Orientada a Objetos")
        );

        assertTrue(
                texto.contains("TURMA-A")
        );
    }
}