package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private PeriodoLetivo criarPeriodo() {
        return new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
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
                "ESOFT4S-NB",
                criarPeriodo()
        );
    }

    @Test
    void deveMatricularAluno() {
        // Arrange
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno =
                new Aluno(
                        "A001",
                        "Douglas",
                        "douglas@email.com"
                );

        // Act
        Matricula matricula =
                oferta.matricular("M001", aluno);

        // Assert
        assertNotNull(matricula);
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
    }

    @Test
    void deveMatricularAlunoGerandoCodigoAutomaticamente() {
        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(criarDisciplina());

        Aluno aluno =
                new Aluno(
                        "A001",
                        "Douglas",
                        "douglas@email.com"
                );

        Matricula matricula =
                oferta.matricular(aluno);

        assertNotNull(matricula);
        assertEquals("MAT-1", matricula.getCodigo());
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    void naoDeveMatricularAlunoNulo() {
        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(criarDisciplina());

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("M001", null)
        );
    }

    @Test
    void naoDevePermitirMatriculaDuplicadaNaMesmaOferta() {
        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(criarDisciplina());

        Aluno aluno =
                new Aluno(
                        "A001",
                        "Douglas",
                        "douglas@email.com"
                );

        oferta.matricular("M001", aluno);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("M002", aluno)
        );
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina primeiraOferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno =
                new Aluno(
                        "A001",
                        "Douglas",
                        "douglas@email.com"
                );

        Matricula primeiraMatricula =
                primeiraOferta.matricular("M001", aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        // Após reprovação, o aluno pode se matricular novamente
        // na mesma disciplina em um novo período letivo.

        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(
                        2027,
                        Semestre.PRIMEIRO
                );

        Turma novaTurma =
                new Turma(
                        "ESOFT5S-NB",
                        novoPeriodo
                );

        OfertaDisciplina novaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        Matricula novaMatricula =
                novaOferta.matricular("M002", aluno);

        assertNotNull(novaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void naoDevePermitirNovaMatriculaAposAprovacao() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        Aluno aluno =
                new Aluno(
                        "A001",
                        "Douglas",
                        "douglas@email.com"
                );

        Matricula matricula =
                oferta.matricular("M001", aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(
                        2027,
                        Semestre.PRIMEIRO
                );

        Turma novaTurma =
                new Turma(
                        "ESOFT5S-NB",
                        novoPeriodo
                );

        OfertaDisciplina novaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalStateException.class,
                () -> novaOferta.matricular("M002", aluno)
        );
    }

    @Test
    void deveRetornarDisciplinaDaOferta() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        assertEquals(
                disciplina,
                oferta.getDisciplina()
        );
    }

    @Test
    void deveRetornarTurmaDaOferta() {
        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(criarDisciplina());

        assertEquals(
                turma,
                oferta.getTurma()
        );
    }

    @Test
    void deveComecarComListaDeMatriculasVazia() {
        Turma turma = criarTurma();
        OfertaDisciplina oferta =
                turma.ofertarDisciplina(criarDisciplina());

        assertNotNull(oferta.getMatriculas());
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    void deveRetornarToStringCorreto() {
        Turma turma = criarTurma();
        Disciplina disciplina = criarDisciplina();

        OfertaDisciplina oferta =
                turma.ofertarDisciplina(disciplina);

        assertTrue(
                oferta.toString().contains("POO")
        );
        assertTrue(
                oferta.toString().contains("ESOFT4S-NB")
        );
    }
}