package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    @DisplayName("Deve validar e normalizar os dados do aluno")
    void deveValidarENormalizarDados() {
        var aluno = new Aluno(" RA001 ", " Ana Souza ", "ana@email.com");

        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertEquals("RA001 - Ana Souza", aluno.toString());
    }

    @Test
    @DisplayName("Não deve aceitar registro acadêmico vazio")
    void deveRejeitarRegistroAcademicoVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Ana", "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("  ", "Ana", "ana@email.com"));
    }

    @Test
    @DisplayName("Não deve aceitar nome vazio")
    void deveRejeitarNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", " ", "ana@email.com"));
    }

    @Test
    @DisplayName("Não deve aceitar e-mail inválido ao criar ou alterar aluno")
    void deveRejeitarEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana", "email-invalido"));

        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("sem-arroba"));
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail(null));
        assertEquals("ana@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve permitir alterar o e-mail válido")
    void deveAlterarEmailValido() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        aluno.setEmail("novo@email.com");
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("A igualdade do aluno deve usar o registro acadêmico")
    void deveCompararAlunosPeloRegistroAcademico() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        var mesmoAluno = new Aluno("RA001", "Outro nome", "outro@email.com");
        var outroAluno = new Aluno("RA002", "Ana", "ana@email.com");

        assertTrue(aluno.equals(aluno));
        assertEquals(aluno, mesmoAluno);
        assertEquals(aluno.hashCode(), mesmoAluno.hashCode());
        assertNotEquals(aluno, outroAluno);
        assertNotEquals(aluno, null);
        assertNotEquals(aluno, "RA001");
    }

    @Test
    @DisplayName("Não deve permitir nova matrícula em disciplina já aprovada")
    void deveImpedirNovaMatriculaEmDisciplinaAprovada() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var primeiraTurma = new Turma("T1", new PeriodoLetivo(2025, Semestre.PRIMEIRO));
        var primeiraOferta = primeiraTurma.ofertarDisciplina(poo);
        var matricula = primeiraOferta.matricular("MAT-001", aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        var segundaTurma = new Turma("T2", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var segundaOferta = segundaTurma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80));

        assertThrows(IllegalStateException.class,
                () -> segundaOferta.matricular("MAT-002", aluno));
    }
}
