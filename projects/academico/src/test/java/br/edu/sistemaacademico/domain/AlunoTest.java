package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlunoTest {

    @Test
    @DisplayName("Deve exigir registro acadêmico e nome no cadastro do aluno")
    void deveExigirRegistroAcademicoENome() {
        // Arrange
        var email = "ana@email.com";

        // Act
        var semRegistro = assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("  ", "Ana Souza", email)
        );
        var semNome = assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, email)
        );

        // Assert
        assertEquals("O registro acadêmico é obrigatório.", semRegistro.getMessage());
        assertEquals("O nome do aluno é obrigatório.", semNome.getMessage());
    }

    @Test
    @DisplayName("Deve recusar e-mail fora do formato")
    void deveRecusarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana.email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana@email")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", null)
        );
    }

    @Test
    @DisplayName("Deve trocar o e-mail do aluno e recusar a troca por um inválido")
    void deveTrocarEmailApenasQuandoValido() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        aluno.setEmail("ana.souza@email.com");

        assertEquals("ana.souza@email.com", aluno.getEmail());
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("ana@@email.com")
        );
        assertEquals("ana.souza@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve devolver o histórico de matrículas como cópia protegida")
    void deveDevolverHistoricoComoCopia() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);

        var historico = aluno.getMatriculas();

        assertEquals(1, historico.size());
        assertEquals(matricula, historico.get(0));
        assertThrows(
                UnsupportedOperationException.class,
                () -> historico.add(matricula)
        );
    }

    @Test
    @DisplayName("Deve identificar o aluno pelo registro acadêmico")
    void deveIdentificarAlunoPeloRegistroAcademico() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var mesmoRegistro = new Aluno("RA001", "Ana Souza Silva", "ana.silva@email.com");
        var outroRegistro = new Aluno("RA002", "Ana Souza", "ana@email.com");

        assertEquals(aluno, mesmoRegistro);
        assertEquals(aluno.hashCode(), mesmoRegistro.hashCode());
        assertNotEquals(aluno, outroRegistro);
        assertNotEquals(aluno, "RA001");
        assertEquals("RA001 - Ana Souza", aluno.toString());
    }
}
