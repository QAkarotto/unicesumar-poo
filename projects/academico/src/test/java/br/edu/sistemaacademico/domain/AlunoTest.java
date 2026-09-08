package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Aluno")
class AlunoTest {

    @Test
    @DisplayName("Deve criar um aluno válido sem matrículas no histórico")
    void deveCriarAlunoValido() {
        // Arrange / Act
        var aluno = new Aluno("  RA2026001  ", "  Arthur Pacher  ", "arthur@email.com");

        // Assert
        assertEquals("RA2026001", aluno.getRegistroAcademico());
        assertEquals("Arthur Pacher", aluno.getNome());
        assertEquals("arthur@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Deve recusar registro acadêmico nulo ou em branco")
    void deveRecusarRegistroAcademicoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(null, "Arthur Pacher", "arthur@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("  ", "Arthur Pacher", "arthur@email.com")
        );
    }

    @Test
    @DisplayName("Deve recusar nome nulo ou em branco")
    void deveRecusarNomeInvalido() {
        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "   ", "arthur@email.com")
        );

        assertEquals("O nome do aluno é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve recusar e-mail em formato inválido na criação")
    void deveRecusarEmailInvalidoNaCriacao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "Arthur Pacher", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "Arthur Pacher", "")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "Arthur Pacher", "arthur.email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA2026001", "Arthur Pacher", "arthur@email")
        );
    }

    @Test
    @DisplayName("Deve permitir alterar o e-mail para um valor válido")
    void devePermitirAlterarEmailValido() {
        // Arrange
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");

        // Act
        aluno.setEmail("arthur.pacher@unicesumar.edu.br");

        // Assert
        assertEquals("arthur.pacher@unicesumar.edu.br", aluno.getEmail());
    }

    @Test
    @DisplayName("Não deve alterar o estado do aluno quando o novo e-mail é inválido")
    void naoDeveAlterarEstadoComEmailInvalido() {
        // Arrange
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");

        // Act
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));

        // Assert
        assertEquals("arthur@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Deve expor o histórico como coleção imutável")
    void deveExporHistoricoImutavel() {
        // Arrange
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(new Disciplina("POO", "Prog. Orientada a Objetos", 80));
        var matricula = oferta.matricular(aluno);

        // Act
        var historico = aluno.getMatriculas();

        // Assert
        assertEquals(1, historico.size());
        assertThrows(
                UnsupportedOperationException.class,
                () -> historico.add(matricula)
        );
    }

    @Test
    @DisplayName("Deve considerar iguais dois alunos com o mesmo registro acadêmico")
    void deveCompararAlunosPeloRegistroAcademico() {
        // Arrange
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");
        var mesmoRegistro = new Aluno("RA2026001", "Outro Nome", "outro@email.com");
        var outro = new Aluno("RA2026002", "Bruno Santos", "bruno@email.com");

        // Assert
        assertEquals(aluno, mesmoRegistro);
        assertEquals(aluno.hashCode(), mesmoRegistro.hashCode());
        assertNotEquals(aluno, outro);
        assertTrue(aluno.equals(aluno));
        assertFalse(aluno.equals("RA2026001"));
        assertFalse(aluno.equals(null));
    }

    @Test
    @DisplayName("Deve apresentar registro acadêmico e nome no toString")
    void deveApresentarRepresentacaoTextual() {
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");

        assertEquals("RA2026001 - Arthur Pacher", aluno.toString());
    }
}
