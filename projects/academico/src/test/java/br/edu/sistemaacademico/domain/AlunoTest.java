package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    @DisplayName("Deve criar aluno com dados válidos e histórico vazio")
    void deveCriarAlunoComDadosValidos() {
        // Arrange e Act
        var aluno = new Aluno("RA001", "Priscila", "priscila@gmail.com");

        assertEquals("RA001", aluno.getRa());
        assertEquals("Priscila", aluno.getNome());
        assertEquals("priscila@gmail.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar dados obrigatórios inválidos no cadastro")
    void deveRejeitarDadosObrigatoriosInvalidos() {
        // Act e Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("", "Priscila", "priscila@gmail.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "priscila@gmail.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA001", "Priscila", "priscila-com"));
    }

    @Test
    @DisplayName("Deve rejeitar troca de email quando inválido")
    void deveAlterarEmailSomenteQuandoValido() {
        // Arrange
        var aluno = new Aluno("RA001", "Priscila", "priscila@gmail.com");
        // Act e Assert
        aluno.setEmail("novo@example.com");
        assertEquals("novo@example.com", aluno.getEmail());
        assertThrows(IllegalArgumentException.class,
                () -> aluno.setEmail("invalido"));
        assertEquals("novo@example.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve identificar aprovação e reprovação no histórico acadêmico")
    void deveIdentificarAprovacaoNaDisciplina() {
        //Arrange
        var aluno = new Aluno("RA001", "Priscila", "priscila@gmail.com");
        var disciplina = new Disciplina("POO", "Programacao", 80);
        var oferta = turma().ofertarDisciplina(disciplina);
        var matricula = oferta.matricular(aluno);

        //Assert e Act
        assertFalse(aluno.jaFoiAprovadoEm(null));
        assertFalse(aluno.jaFoiAprovadoEm(disciplina));

        matricula.concluir(ResultadoAcademico.REPROVADO);
        assertFalse(aluno.jaFoiAprovadoEm(disciplina));

        var segunda = turma().ofertarDisciplina(disciplina).matricular(aluno);
        segunda.concluir(ResultadoAcademico.APROVADO);
        assertTrue(aluno.jaFoiAprovadoEm(disciplina));
    }

    @Test
    @DisplayName("Deve rejeitar inserção de matrícula nula no histórico")
    void deveRejeitarMatriculaNula() {
        //Arrange
        var aluno = new Aluno("RA001", "Priscila", "priscila@gmail.com");

        //Act Assert
        assertThrows(IllegalArgumentException.class, () -> aluno.adicionarMatricula(null));
    }

    @Test
    @DisplayName("Deve retornar cópia do histórico protegendo o encapsulamento")
    void deveRetornarCopiaDoHistoricoDeMatriculas() {
        //Arrange
        var aluno = new Aluno("RA001", "Priscila", "priscila@gmail.com");
        var matricula = turma().ofertarDisciplina(
                new Disciplina("POO", "Programacao", 80)).matricular(aluno);
        //Act
        var historico = aluno.getMatriculas();
        historico.clear();
        //Assert
        assertEquals(1, aluno.getMatriculas().size());
        assertEquals(matricula, aluno.getMatriculas().get(0));
    }
    private Turma turma() {
        return new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
    }
}
