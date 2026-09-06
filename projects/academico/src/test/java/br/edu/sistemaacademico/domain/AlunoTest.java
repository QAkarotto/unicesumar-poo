package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {

        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        assertEquals("RA2026001", aluno.getIdentificadorAcademico());
        assertEquals("Paola Oliveira", aluno.getNome());
        assertEquals("paola@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void naoDeveCriarAlunoSemIdentificador() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "",
                        "Paola",
                        "paola@email.com"
                )
        );
    }

    @Test
    void naoDeveCriarAlunoSemNome() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "",
                        "paola@email.com"
                )
        );
    }

    @Test
    void naoDeveCriarAlunoSemEmail() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "Paola",
                        ""
                )
        );
    }

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "Paola",
                        "email-invalido"
                )
        );
    }

    @Test
    void devePermitirAlterarEmailValido() {

        Aluno aluno = new Aluno(
                "RA001",
                "Paola",
                "paola@email.com"
        );

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarEmailParaValorInvalido() {

        Aluno aluno = new Aluno(
                "RA001",
                "Paola",
                "paola@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );

        assertEquals(
                "paola@email.com",
                aluno.getEmail()
        );
    }

    @Test
    void naoDevePermitirAlterarListaDeMatriculasDiretamente() {

        Aluno aluno = new Aluno(
                "RA001",
                "Paola",
                "paola@email.com"
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().clear()
        );
    }

    @Test
    void deveAdicionarMatriculaAoHistorico() {

        Aluno aluno = new Aluno(
                "RA001",
                "Paola",
                "paola@email.com"
        );

        Disciplina disciplina =
                new Disciplina("POO", "Programação Orientada a Objetos", 80);

        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma =
                new Turma("TURMA-A", periodo);

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta.matricular(aluno);

        assertEquals(1, aluno.getMatriculas().size());
        assertTrue(aluno.getMatriculas().contains(matricula));
    }
}//deu certo