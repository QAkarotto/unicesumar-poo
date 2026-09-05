package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    void deveCriarAlunoComDadosValidos() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        assertEquals("RA001", aluno.getIdentificador());
        assertEquals("Maria Silva", aluno.getNome());
        assertEquals("maria@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoComIdentificadorNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Maria Silva", "maria@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComIdentificadorVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("   ", "Maria Silva", "maria@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "maria@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "  ", "maria@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Maria Silva", null));
    }

    @Test
    void naoDeveCriarAlunoComEmailVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Maria Silva", ""));
    }

    @Test
    void naoDeveCriarAlunoComEmailForaDoFormato() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Maria Silva", "email-invalido"));
    }

    @Test
    void deveAtualizarEmailComValorValido() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        aluno.setEmail("maria.silva@nova.com");

        assertEquals("maria.silva@nova.com", aluno.getEmail());
    }

    @Test
    void naoDeveAtualizarEmailParaValorInvalido() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
        assertEquals("maria@email.com", aluno.getEmail());
    }

    @Test
    void alunoSemMatriculasNaoDeveEstarAprovadoEmNenhumaDisciplina() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        assertFalse(aluno.jaFoiAprovadoEm(disciplina));
    }

    @Test
    void deveIndicarAprovacaoAposConclusaoComResultadoAprovado() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertTrue(aluno.jaFoiAprovadoEm(disciplina));
    }

    @Test
    void naoDeveIndicarAprovacaoQuandoResultadoForReprovado() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertFalse(aluno.jaFoiAprovadoEm(disciplina));
    }

    @Test
    void getMatriculasNaoDeveExporColecaoInternaParaModificacao() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");
        var disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turma = new Turma("T1", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        var matriculas = aluno.getMatriculas();

        assertTrue(matriculas.contains(matricula));
        assertThrows(UnsupportedOperationException.class,
                () -> matriculas.add(matricula));
    }

    @Test
    void toStringDeveConterNomeIdentificadorEEmail() {
        var aluno = new Aluno("RA001", "Maria Silva", "maria@email.com");

        var texto = aluno.toString();

        assertTrue(texto.contains("Maria Silva"));
        assertTrue(texto.contains("RA001"));
        assertTrue(texto.contains("maria@email.com"));
    }
}