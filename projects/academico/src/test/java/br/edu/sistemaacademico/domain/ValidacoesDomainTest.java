package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacoesDomainTest {

    @Test
    void deveAlterarEmailValidoEDescartarEmailInvalido() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act
        aluno.setEmail("novo@email.com");

        // Assert
        assertEquals("novo@email.com", aluno.getEmail());

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarDadosInvalidosDoAluno() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(" ", "Ana Souza", "ana@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", " ", "ana@email.com")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "ana@")
        );
    }

    @Test
    void alunosComMesmoRegistroAcademicoDevemRepresentarOMesmoAluno() {
        // Arrange
        var aluno1 = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var aluno2 = new Aluno("RA001", "Ana Silva", "silva@email.com");
        var outroAluno = new Aluno("RA002", "Bruno Lima", "bruno@email.com");

        // Act + Assert
        assertEquals(aluno1, aluno2);
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
        assertNotEquals(aluno1, outroAluno);
        assertNotEquals(aluno1, "RA001");
    }

    @Test
    void deveRejeitarDadosInvalidosDaDisciplina() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina(" ", "POO", 80)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", " ", 80)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Disciplina("POO", "Programação Orientada a Objetos", -1)
        );
    }

    @Test
    void disciplinasComMesmoCodigoDevemSerConsideradasIguais() {
        // Arrange
        var disciplina1 = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );
        var disciplina2 = new Disciplina("POO", "POO", 60);
        var outra = new Disciplina("BD", "Banco de Dados", 80);

        // Act + Assert
        assertEquals(disciplina1, disciplina2);
        assertEquals(disciplina1.hashCode(), disciplina2.hashCode());
        assertNotEquals(disciplina1, outra);
        assertNotEquals(disciplina1, "POO");
    }

    @Test
    void deveRejeitarPeriodoLetivoInvalido() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-1, Semestre.PRIMEIRO)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    void periodosComMesmoAnoESemestreDevemSerIguais() {
        // Arrange
        var periodo1 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var periodo2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroPeriodo = new PeriodoLetivo(2027, Semestre.PRIMEIRO);

        // Act + Assert
        assertEquals(periodo1, periodo2);
        assertEquals(periodo1.hashCode(), periodo2.hashCode());
        assertNotEquals(periodo1, outroPeriodo);
        assertNotEquals(periodo1, "2026/2");
        assertEquals("2026/2", periodo1.toString());
    }

    @Test
    void devePreservarDadosValidosDosObjetosDeDominio() {
        // Arrange
        var aluno = new Aluno("  RA001  ", "  Ana Souza  ", "ana@email.com");
        var disciplina = new Disciplina(
                "  POO  ",
                "  Programação Orientada a Objetos  ",
                80
        );
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("  ESOFT4S-NA  ", periodo);
        var oferta = turma.ofertarDisciplina(disciplina);
        var matricula = oferta.matricular("  MAT-001  ", aluno);

        // Act + Assert
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("POO", disciplina.getCodigo());
        assertEquals("Programação Orientada a Objetos", disciplina.getNome());
        assertEquals(80, disciplina.getCargaHoraria());
        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
        assertEquals(2, Semestre.SEGUNDO.getNumero());
        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertEquals("MAT-001", matricula.getCodigo());

        assertTrue(aluno.toString().contains("RA001"));
        assertTrue(disciplina.toString().contains("POO"));
        assertTrue(turma.toString().contains("ESOFT4S-NA"));
        assertTrue(oferta.toString().contains("POO"));
        assertTrue(matricula.toString().contains("MAT-001"));
    }
}
