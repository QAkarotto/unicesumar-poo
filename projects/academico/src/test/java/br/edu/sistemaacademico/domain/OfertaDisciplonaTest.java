package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    private final Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

    private Turma novaTurma(String codigo, int ano, Semestre semestre) {
        return new Turma(codigo, new PeriodoLetivo(ano, semestre));
    }

    @Test
    void construtorComTurmaNula_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new OfertaDisciplina(null, poo));
    }

    @Test
    void construtorComDisciplinaNula_deveLancarExcecao() {
        // Arrange
        Turma turma = novaTurma("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);

        // Act / Assert
        assertThrows(IllegalArgumentException.class,
                () -> new OfertaDisciplina(turma, null));
    }

    @Test
    void matricular_comAlunoValido_deveCriarMatriculaEVincularAoAluno() {
        // Arrange
        Turma turma = novaTurma("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
        Aluno aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");

        // Act
        Matricula matricula = oferta.matricular(aluno);

        // Assert
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOferta());
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertNull(matricula.getResultado());
    }

    @Test
    void matricular_comAlunoNulo_deveLancarExcecao() {
        // Arrange
        Turma turma = novaTurma("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> oferta.matricular(null));
    }

    @Test
    void matricular_mesmoAlunoDuasVezesNaMesmaOferta_deveLancarExcecao() {
        // Arrange
        Turma turma = novaTurma("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
        Aluno aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");
        oferta.matricular(aluno);

        // Act / Assert
        IllegalStateException excecao = assertThrows(IllegalStateException.class,
                () -> oferta.matricular(aluno));
        assertTrue(excecao.getMessage().contains("já está matriculado"));
    }

    @Test
    void matricular_alunoJaAprovadoNaDisciplina_deveLancarExcecao() {
        // Arrange: aluno aprovado em POO em uma turma anterior
        Turma turmaAnterior = novaTurma("ESOFT4S-NA", 2025, Semestre.SEGUNDO);
        OfertaDisciplina ofertaAnterior = turmaAnterior.ofertarDisciplina(poo);
        Aluno aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");
        Matricula matriculaAnterior = ofertaAnterior.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.APROVADO);

        // Uma nova oferta da mesma disciplina, em outra turma
        Turma novaTurma = novaTurma("ESOFT4S-NB", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina novaOferta = novaTurma.ofertarDisciplina(poo);

        // Act / Assert
        IllegalStateException excecao = assertThrows(IllegalStateException.class,
                () -> novaOferta.matricular(aluno));
        assertTrue(excecao.getMessage().contains("já foi aprovado"));
    }

    @Test
    void matricular_alunoReprovadoAnteriormente_devePermitirNovaMatricula() {
        // Arrange: aluno reprovado em POO em uma turma anterior
        Turma turmaAnterior = novaTurma("ESOFT4S-NA", 2025, Semestre.SEGUNDO);
        OfertaDisciplina ofertaAnterior = turmaAnterior.ofertarDisciplina(poo);
        Aluno aluno = new Aluno("RA001", "Paola Oliveira", "paola@email.com");
        Matricula matriculaAnterior = ofertaAnterior.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.REPROVADO);

        // Uma nova oferta da mesma disciplina, em outra turma
        Turma novaTurma = novaTurma("ESOFT4S-NB", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina novaOferta = novaTurma.ofertarDisciplina(poo);

        // Act
        Matricula novaMatricula = novaOferta.matricular(aluno);

        // Assert
        assertNotNull(novaMatricula);
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void getMatriculas_deveRetornarListaImutavel() {
        // Arrange
        Turma turma = novaTurma("ESOFT4S-NA", 2026, Semestre.PRIMEIRO);
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
        List<Matricula> matriculas = oferta.getMatriculas();

        // Act / Assert
        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(null));
    }
}