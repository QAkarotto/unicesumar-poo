package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private final Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
    private final Turma turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
    private final OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
    private final Aluno aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");

    @Test
    void construtorComAlunoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(null, oferta));
    }

    @Test
    void construtorComOfertaNula_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Matricula(aluno, null));
    }

    @Test
    void construtorComDadosValidos_naoDeveTerResultadoInicial() {
        // Arrange / Act
        Matricula matricula = new Matricula(aluno, oferta);

        // Assert
        assertNull(matricula.getResultado());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOferta());
        assertEquals(poo, matricula.getDisciplina());
    }

    @Test
    void concluir_comResultadoValido_deveAtualizarResultado() {
        // Arrange
        Matricula matricula = new Matricula(aluno, oferta);

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void concluir_comResultadoNulo_deveLancarExcecao() {
        // Arrange
        Matricula matricula = new Matricula(aluno, oferta);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    void concluir_matriculaJaConcluida_deveLancarExcecao() {
        // Arrange
        Matricula matricula = new Matricula(aluno, oferta);
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Act / Assert
        assertThrows(IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO));
    }
}