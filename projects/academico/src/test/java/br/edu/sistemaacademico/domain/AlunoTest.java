package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        assertEquals("RA001", aluno.getRa());
        assertEquals("João", aluno.getNome());
        assertEquals("joao@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveRejeitarRaNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(null, "João", "joao@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "João", "joao@email.com")
        );
    }

    @Test
    void deveRejeitarNomeNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "joao@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "joao@email.com")
        );
    }

    @Test
    void deveRejeitarEmailNuloOuVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "João", null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "João", "")
        );
    }

    @Test
    void deveAlterarEmail() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "antigo@email.com"
        );

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarAlteracaoParaEmailInvalido() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("")
        );

        assertEquals("joao@email.com", aluno.getEmail());
    }

    @Test
    void deveIdentificarAprovacaoEmDisciplina() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma("T01", periodo);
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        Matricula matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertTrue(aluno.possuiAprovacaoEm(disciplina));
    }

    @Test
    void deveRetornarFalseQuandoAlunoNaoFoiAprovado() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        assertFalse(aluno.possuiAprovacaoEm(disciplina));
    }

    @Test
    void deveRejeitarDisciplinaNulaNaConsultaDeAprovacao() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.possuiAprovacaoEm(null)
        );
    }

    @Test
    void deveRejeitarMatriculaNula() {
        Aluno aluno = new Aluno(
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
    void deveRejeitarMatriculaDeOutroAluno() {
        Aluno aluno1 = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        Aluno aluno2 = new Aluno(
                "RA002",
                "Maria",
                "maria@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "POO",
                80
        );

        Turma turma = new Turma(
                "T01",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
        Matricula matricula = oferta.matricular(aluno1);

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno2.adicionarMatricula(matricula)
        );
    }

    @Test
    void listaDeMatriculasNaoDevePermitirAlteracaoExterna() {
        Aluno aluno = new Aluno(
                "RA001",
                "João",
                "joao@email.com"
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().add(null)
        );
    }
}