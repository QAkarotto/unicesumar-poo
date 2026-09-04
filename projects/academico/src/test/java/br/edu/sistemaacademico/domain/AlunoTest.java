package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    @DisplayName("Deve considerar que o aluno foi aprovado somente após conclusão com APROVADO")
    void deveIdentificarDisciplinaAprovada() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var oferta2025 = novaOferta("T1", 2025, Semestre.SEGUNDO, disciplina);
        var oferta2026 = novaOferta("T2", 2026, Semestre.PRIMEIRO, disciplina);

        // Act + Assert: antes de qualquer aprovação
        var primeiraMatricula = oferta2025.matricular("MAT-001", aluno);
        assertFalse(aluno.foiAprovadoEm(disciplina));

        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);
        assertFalse(aluno.foiAprovadoEm(disciplina));

        var segundaMatricula = oferta2026.matricular("MAT-002", aluno);
        segundaMatricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertTrue(aluno.foiAprovadoEm(disciplina));
    }

    @Test
    @DisplayName("Deve retornar somente matrículas que ainda estão em curso")
    void deveRetornarSomenteMatriculasEmCurso() {
        // Arrange
        var aluno = new Aluno("RA002", "Bruno Lima", "bruno@email.com");
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var poo = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        var bd = turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        var matriculaPoo = poo.matricular("MAT-003", aluno);
        var matriculaBd = bd.matricular("MAT-004", aluno);
        matriculaBd.concluir(ResultadoAcademico.REPROVADO);

        // Act
        var emCurso = aluno.getMatriculasEmCurso();

        // Assert
        assertEquals(1, emCurso.size());
        assertTrue(emCurso.contains(matriculaPoo));
        assertFalse(emCurso.contains(matriculaBd));
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve rejeitar dados obrigatórios inválidos ao criar aluno")
    void deveRejeitarAlunoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(" ", "Ana", "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA003", " ", "ana@email.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA003", "Ana", "email-invalido"));
    }

    @Test
    @DisplayName("Deve exigir disciplina para consultar aprovação")
    void deveExigirDisciplinaNaConsultaDeAprovacao() {
        var aluno = new Aluno("RA004", "Carla", "carla@email.com");

        assertThrows(IllegalArgumentException.class,
                () -> aluno.foiAprovadoEm(null));
    }

    @Test
    @DisplayName("Aluno deve recusar registro de matrícula nula ou pertencente a outro aluno")
    void deveProtegerRegistroInternoDeMatricula() {
        var aluno = new Aluno("RA005", "Daniel", "daniel@email.com");
        var outroAluno = new Aluno("RA006", "Eduarda", "eduarda@email.com");
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));
        var matriculaDoOutroAluno = new Matricula("MAT-010", outroAluno, oferta);

        assertThrows(IllegalArgumentException.class,
                () -> aluno.registrarMatricula(null));
        assertThrows(IllegalStateException.class,
                () -> aluno.registrarMatricula(matriculaDoOutroAluno));
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Aluno deve normalizar espaços dos dados recebidos")
    void deveNormalizarDadosDoAluno() {
        var aluno = new Aluno("  RA007  ", "  Fernanda Lima  ", "  fernanda@email.com  ");

        assertEquals("RA007", aluno.getRegistroAcademico());
        assertEquals("Fernanda Lima", aluno.getNome());
        assertEquals("fernanda@email.com", aluno.getEmail());
    }

    private OfertaDisciplina novaOferta(
            String codigoTurma,
            int ano,
            Semestre semestre,
            Disciplina disciplina
    ) {
        var turma = new Turma(codigoTurma, new PeriodoLetivo(ano, semestre));
        return turma.ofertarDisciplina(disciplina);
    }
}
