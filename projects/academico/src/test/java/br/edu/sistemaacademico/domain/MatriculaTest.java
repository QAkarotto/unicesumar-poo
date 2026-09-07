package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private OfertaDisciplina criarOferta() {
        PeriodoLetivo periodo =
                new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("T01", periodo);

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        return turma.ofertarDisciplina(disciplina);
    }

    private Aluno criarAluno() {
        return new Aluno(
                "2026001",
                "Matheus",
                "matheus@email.com"
        );
    }

    @Test
    void deveCriarMatricula() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertNotNull(matricula);
        assertNotNull(matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertNull(matricula.getResultado());
        assertFalse(matricula.isConcluida());
    }

    @Test
    void deveGerarCodigoAutomaticoParaMatricula() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertTrue(matricula.getCodigo().startsWith("MAT-"));
    }

    @Test
    void deveConcluirMatriculaComoAprovado() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();
        Matricula matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
        assertTrue(matricula.isConcluida());
    }

    @Test
    void deveConcluirMatriculaComoReprovado() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();
        Matricula matricula = oferta.matricular(aluno);

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
        assertTrue(matricula.isConcluida());
    }

    @Test
    void naoDeveConcluirMatriculaComResultadoNulo() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();
        Matricula matricula = oferta.matricular(aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    void naoDeveConcluirMatriculaDuasVezes() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();
        Matricula matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        // Primeira matrícula
        OfertaDisciplina primeiraOferta = criarOferta();
        Aluno aluno = criarAluno();

        Matricula primeiraMatricula =
                primeiraOferta.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        // Nova oferta da mesma disciplina
        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        Turma novaTurma =
                new Turma("T02", novoPeriodo);

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        OfertaDisciplina segundaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        // Act
        Matricula segundaMatricula =
                segundaOferta.matricular(aluno);

        // Assert
        assertNotNull(segundaMatricula);
        assertEquals(aluno, segundaMatricula.getAluno());
        assertEquals(
                segundaOferta,
                segundaMatricula.getOfertaDisciplina()
        );
    }

    @Test
    void alunoAprovadoDeveSerReconhecido() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();

        Matricula matricula = oferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Act
        boolean resultado =
                aluno.jaAprovadoEm(oferta.getDisciplina());

        // Assert
        assertTrue(resultado);
    }

    @Test
    void alunoReprovadoNaoDeveSerConsideradoAprovado() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();

        Matricula matricula = oferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        // Act
        boolean resultado =
                aluno.jaAprovadoEm(oferta.getDisciplina());

        // Assert
        assertFalse(resultado);
    }

    @Test
    void alunoAprovadoNaoPodeSeMatricularNovamente() {
        // Arrange
        OfertaDisciplina primeiraOferta = criarOferta();
        Aluno aluno = criarAluno();

        Matricula matricula =
                primeiraOferta.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        // Nova oferta da mesma disciplina
        PeriodoLetivo novoPeriodo =
                new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        Turma novaTurma =
                new Turma("T02", novoPeriodo);

        Disciplina disciplina =
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                );

        OfertaDisciplina segundaOferta =
                novaTurma.ofertarDisciplina(disciplina);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> segundaOferta.matricular(aluno)
        );
    }

    @Test
    void deveGerarTextoDaMatricula() {
        // Arrange
        OfertaDisciplina oferta = criarOferta();
        Aluno aluno = criarAluno();

        Matricula matricula =
                oferta.matricular(aluno);

        // Act
        String resultado = matricula.toString();

        // Assert
        assertTrue(resultado.contains(matricula.getCodigo()));
        assertTrue(resultado.contains("Matheus"));
        assertTrue(resultado.contains("Programação Orientada a Objetos"));
        assertTrue(resultado.contains("T01"));
    }
}