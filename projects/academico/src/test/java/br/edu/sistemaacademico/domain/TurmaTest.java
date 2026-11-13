package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    @Test
<<<<<<< HEAD
    @DisplayName("Deve criar uma turma válida sem ofertas")
    void deveCriarTurmaValida() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(2026, turma.getPeriodoLetivo().getAno());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    @DisplayName("Deve criar uma turma já ofertando uma disciplina")
    void deveCriarTurmaComDisciplinaOfertada() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var turma = new Turma(
                "ESOFT4S-NA",
                disciplina,
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().get(0).getDisciplina());
    }

    @Test
    @DisplayName("Deve rejeitar código nulo ou em branco")
    void deveRejeitarCodigoInvalido() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma(" ", periodo));
    }

    @Test
    @DisplayName("Deve rejeitar período letivo nulo")
    void deveRejeitarPeriodoLetivoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("ESOFT4S-NA", null));
    }

    @Test
    @DisplayName("Deve rejeitar oferta de disciplina nula")
    void deveRejeitarDisciplinaNulaAoOfertar() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    @DisplayName("Deve impedir ofertar a mesma disciplina duas vezes na mesma turma")
    void deveImpedirDisciplinaOfertadaDuasVezes() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve obter a única oferta da turma")
    void deveObterUnicaOferta() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        assertEquals(oferta, turma.obterUnicaOferta());
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter única oferta sem nenhuma disciplina ofertada")
    void deveLancarExcecaoAoObterUnicaOfertaSemOfertas() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertThrows(IllegalStateException.class, turma::obterUnicaOferta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter única oferta com múltiplas disciplinas ofertadas")
    void deveLancarExcecaoAoObterUnicaOfertaComMultiplasOfertas() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 60));

        assertThrows(IllegalStateException.class, turma::obterUnicaOferta);
    }

    @Test
    @DisplayName("O toString deve conter o código e o período letivo")
    void toStringDeveConterCodigoEPeriodoLetivo() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        assertEquals("ESOFT4S-NA - 2026/2", turma.toString());
=======
    @DisplayName("Deve permitir ofertar disciplinas diferentes")
    void devePermitirOfertarDisciplinasDiferentes() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDeDados = new Disciplina("BD", "Banco de Dados", 80);

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDeDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertTrue(turma.getOfertas().stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(poo)));
        assertTrue(turma.getOfertas().stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(bancoDeDados)));
    }

    @Test
    @DisplayName("Deve impedir disciplina duplicada na turma")
    void deveImpedirDisciplinaDuplicadaNaTurma() {
        // Arrange
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(poo);

        // Act / Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(poo)
        );
    }

    @Test
    @DisplayName("Deve proteger a coleção de ofertas")
    void deveProtegerColecaoDeOfertas() {
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
        assertEquals(1, turma.getOfertas().size());
>>>>>>> origin/master
    }
}
