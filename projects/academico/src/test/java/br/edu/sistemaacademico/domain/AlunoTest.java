package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    @Test
    @DisplayName("Registro acadêmico e nome não podem ficar em branco")
    void deveExigirRegistroENome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("   ", "Helena Braga", "helena@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA200", null, "helena@email.com")
        );
    }

    @Test
    @DisplayName("E-mail fora do formato esperado é recusado")
    void deveRecusarEmailInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA201", "Helena Braga", "helena.email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA201", "Helena Braga", "helena@email")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA201", "Helena Braga", null)
        );
    }

    @Test
    @DisplayName("Troca de e-mail atualiza o cadastro e o nome continua sem espaços sobrando")
    void deveAlterarEmail() {
        // Arrange
        var aluno = new Aluno("  RA202 ", "  Helena Braga  ", "helena@email.com");

        // Act
        aluno.setEmail("helena.braga@unicesumar.edu.br");

        // Assert
        assertEquals("helena.braga@unicesumar.edu.br", aluno.getEmail());
        assertEquals("Helena Braga", aluno.getNome());
        assertEquals("RA202", aluno.getRegistroAcademico());
    }

    @Test
    @DisplayName("E-mail inválido no setter não sobrescreve o e-mail atual")
    void naoDeveSobrescreverEmailComValorInvalido() {
        var aluno = new Aluno("RA203", "Helena Braga", "helena@email.com");

        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("sem-arroba"));

        assertEquals("helena@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Histórico do aluno é exposto como cópia e não aceita alteração externa")
    void historicoNaoDeveSerAlteradoDeFora() {
        // Arrange
        var aluno = new Aluno("RA204", "Rafael Bandeira", "rafael@email.com");
        var oferta = new Turma("ADS3S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO))
                .ofertarDisciplina(new Disciplina("ALG", "Algoritmos", 80));
        var matricula = oferta.matricular(aluno);

        // Act
        var matriculas = aluno.getMatriculas();

        // Assert
        assertTrue(matriculas.contains(matricula));
        assertThrows(UnsupportedOperationException.class, matriculas::clear);
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Dois alunos com o mesmo registro acadêmico são a mesma pessoa")
    void alunoEIdentificadoPeloRegistroAcademico() {
        var aluno = new Aluno("RA205", "Sofia Andrade", "sofia@email.com");
        var mesmoAluno = new Aluno("RA205", "Sofia A. Andrade", "sofia.andrade@email.com");
        var outroAluno = new Aluno("RA206", "Sofia Andrade", "sofia@email.com");

        assertEquals(aluno, mesmoAluno);
        assertEquals(aluno.hashCode(), mesmoAluno.hashCode());
        assertFalse(aluno.equals(outroAluno));
        assertFalse(aluno.equals("RA205"));
    }

    @Test
    @DisplayName("Oferta usa a identidade do aluno para barrar matrícula duplicada")
    void deveBarrarDuplicidadeUsandoIdentidadeDoAluno() {
        var oferta = new Turma("ADS3S-NA", new PeriodoLetivo(2026, Semestre.PRIMEIRO))
                .ofertarDisciplina(new Disciplina("ALG", "Algoritmos", 80));
        oferta.matricular(new Aluno("RA207", "Sofia Andrade", "sofia@email.com"));

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(new Aluno("RA207", "Sofia Andrade", "outro@email.com"))
        );
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("toString do aluno mostra registro e nome")
    void toStringDescreveOAluno() {
        var aluno = new Aluno("RA208", "Sofia Andrade", "sofia@email.com");

        assertEquals("RA208 - Sofia Andrade", aluno.toString());
    }
}
