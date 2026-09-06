package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private Aluno criarAluno() {
        return new Aluno(
                "RA001",
                "Paola Oliveira",
                "paola@email.com"
        );
    }

    private Disciplina criarDisciplina() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    private Turma criarTurma() {
        return new Turma(
                "TURMA-A",
                new PeriodoLetivo(
                        2026,
                        Semestre.PRIMEIRO
                )
        );
    }

    @Test
    void deveCriarOfertaDeDisciplina() {

        Disciplina disciplina = criarDisciplina();
        Turma turma = criarTurma();

        OfertaDisciplina oferta =
                new OfertaDisciplina(
                        disciplina,
                        turma
                );

        assertEquals(
                disciplina,
                oferta.getDisciplina()
        );

        assertEquals(
                turma,
                oferta.getTurma()
        );

        assertTrue(
                oferta.getMatriculas().isEmpty()
        );
    }

    @Test
    void naoDeveCriarOfertaSemDisciplina() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(
                        null,
                        criarTurma()
                )
        );
    }

    @Test
    void naoDeveCriarOfertaSemTurma() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new OfertaDisciplina(
                        criarDisciplina(),
                        null
                )
        );
    }

    @Test
    void devePermitirMatriculaDeAluno() {

        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno = criarAluno();

        Matricula matricula =
                oferta.matricular(aluno);

        assertEquals(
                1,
                oferta.getMatriculas().size()
        );

        assertEquals(
                aluno,
                matricula.getAluno()
        );
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {

        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(
                        criarDisciplina()
                );

        Aluno aluno = criarAluno();

        oferta.matricular(aluno);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(aluno)
        );
    }

    @Test
    void naoDeveMatricularAlunoNulo() {

        Turma turma = criarTurma();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(
                        criarDisciplina()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(null)
        );
    }

    @Test
    void devePermitirNovaMatriculaDepoisDeReprovacao() {

        Aluno aluno = criarAluno();

        Disciplina disciplina = criarDisciplina();

        Turma turma1 =
                criarTurma();

        OfertaDisciplina oferta1 =
                turma1.ofertarDisciplina(disciplina);

        Matricula primeira =
                oferta1.matricular(aluno);

        primeira.concluir(
                ResultadoAcademico.REPROVADO
        );

        Turma turma2 =
                new Turma(
                        "TURMA-B",
                        new PeriodoLetivo(
                                2026,
                                Semestre.SEGUNDO
                        )
                );

        OfertaDisciplina oferta2 =
                turma2.ofertarDisciplina(disciplina);

        Matricula segunda =
                oferta2.matricular(aluno);

        assertNotNull(segunda);
        assertEquals(
                2,
                aluno.getMatriculas().size()
        );
    }

    @Test
    void naoDevePermitirNovaMatriculaDepoisDeAprovacao() {

        Aluno aluno = criarAluno();

        Disciplina disciplina = criarDisciplina();

        Turma turma1 = criarTurma();

        OfertaDisciplina oferta1 =
                turma1.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta1.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        Turma turma2 =
                new Turma(
                        "TURMA-B",
                        new PeriodoLetivo(
                                2026,
                                Semestre.SEGUNDO
                        )
                );

        OfertaDisciplina oferta2 =
                turma2.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalStateException.class,
                () -> oferta2.matricular(aluno)
        );
    }

    @Test
    void naoDevePermitirAlterarListaDeMatriculasDiretamente() {

        Turma turma = criarTurma();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(
                        criarDisciplina()
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().clear()
        );
    }
}