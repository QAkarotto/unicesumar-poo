package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );
    }

    private Disciplina criarPoo() {
        return new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
    }

    @Test
    void deveOfertarMaisDeUmaDisciplina() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                criarPeriodo()
        );

        var poo = criarPoo();

        var bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        // Act
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBd = turma.ofertarDisciplina(bancoDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertTrue(turma.getOfertas().contains(ofertaPoo));
        assertTrue(turma.getOfertas().contains(ofertaBd));
    }

    @Test
    void naoDevePermitirDisciplinaDuplicada() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                criarPeriodo()
        );

        var poo = criarPoo();

        turma.ofertarDisciplina(poo);

        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }

    @Test
    void naoDevePermitirDisciplinaNula() {
        var turma = new Turma(
                "ESOFT4S-NA",
                criarPeriodo()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void naoDeveCriarTurmaComCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "",
                        criarPeriodo()
                )
        );
    }

    @Test
    void naoDeveCriarTurmaSemPeriodoLetivo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(
                        "ESOFT4S-NA",
                        (PeriodoLetivo) null
                )
        );
    }

    @Test
    void construtorComDisciplinaDeveCriarOferta() {
        // Arrange
        var disciplina = criarPoo();

        // Act
        var turma = new Turma(
                "ESOFT4S-NA",
                disciplina,
                criarPeriodo()
        );

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(
                disciplina,
                turma.getOfertas().getFirst().getDisciplina()
        );
    }

    @Test
    void devePermitirCriarMatriculaDiretamentePelaTurmaComUmaOferta() {
        // Arrange
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        var turma = new Turma(
                "ESOFT4S-NA",
                criarPoo(),
                criarPeriodo()
        );

        // Act
        var matricula = new Matricula(
                "MAT001",
                aluno,
                turma
        );

        // Assert
        assertEquals(turma, matricula.getTurma());
        assertEquals(
                SituacaoMatricula.ATIVA,
                matricula.getSituacao()
        );
    }

    @Test
    void naoDeveCriarMatriculaPelaTurmaSemOferta() {
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        var turma = new Turma(
                "ESOFT4S-NA",
                criarPeriodo()
        );

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula(
                        "MAT001",
                        aluno,
                        turma
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaPelaTurmaComVariasOfertas() {
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        var turma = new Turma(
                "ESOFT4S-NA",
                criarPeriodo()
        );

        turma.ofertarDisciplina(criarPoo());

        turma.ofertarDisciplina(
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula(
                        "MAT001",
                        aluno,
                        turma
                )
        );
    }

    @Test
    void naoDeveCriarMatriculaComTurmaNula() {
        var aluno = new Aluno(
                "RA001",
                "Vinicios Eduardo",
                "vinicios@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(
                        "MAT001",
                        aluno,
                        (Turma) null
                )
        );
    }
}