package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(
                        "RA001",
                        "João",
                        "email-invalido"
                )
        );
    }

    @Test
    void deveAlterarEmailValido() {
        // Arrange
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals(
                "novo@email.com",
                aluno.getEmail()
        );
    }

    @Test
    void naoDeveAlterarParaEmailInvalido() {
        // Arrange
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );

        assertEquals(
                "joao@email.com",
                aluno.getEmail()
        );
    }

    @Test
    void naoDeveAdicionarMatriculaNula() {
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.adicionarMatricula(null)
        );
    }

    @Test
    void deveIdentificarAprovacaoNaDisciplina() {
        // Arrange
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        Turma turma =
                new Turma(
                        "ADS4S",
                        periodo
                );

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Matricula matricula =
                oferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Assert
        assertTrue(
                aluno.foiAprovadoNaDisciplina(disciplina)
        );
    }

    @Test
    void deveRetornarFalsoQuandoNaoFoiAprovado() {
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        assertFalse(
                aluno.foiAprovadoNaDisciplina(disciplina)
        );
    }

    @Test
    void deveRetornarFalsoParaDisciplinaNula() {
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        assertFalse(
                aluno.foiAprovadoNaDisciplina(null)
        );
    }

    @Test
    void listaDeMatriculasNaoDevePermitirAlteracaoExterna() {
        // Arrange
        Aluno aluno =
                new Aluno(
                        "RA001",
                        "João",
                        "joao@email.com"
                );

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        PeriodoLetivo periodo =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        Turma turma =
                new Turma(
                        "ADS4S",
                        periodo
                );

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().clear()
        );

        assertEquals(
                1,
                aluno.getMatriculas().size()
        );
    }
}