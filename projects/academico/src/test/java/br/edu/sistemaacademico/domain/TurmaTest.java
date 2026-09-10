package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo periodo() {
        return new PeriodoLetivo(2026, Semestre.SEGUNDO);
    }

    @Test
    void deveOfertarMaisDeUmaDisciplinaNaMesmaTurma() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo());
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var banco = new Disciplina("BD", "Banco de Dados", 80);

        // Act
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBanco = turma.ofertarDisciplina(banco);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertSame(turma, ofertaPoo.getTurma());
        assertSame(turma, ofertaBanco.getTurma());
        assertEquals(poo, ofertaPoo.getDisciplina());
        assertEquals(banco, ofertaBanco.getDisciplina());
    }

    @Test
    void deveImpedirOfertaDuplicadaDaMesmaDisciplina() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo());
        var primeira = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var mesmaDisciplina = new Disciplina("POO", "POO", 60);
        turma.ofertarDisciplina(primeira);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(mesmaDisciplina)
        );
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    void deveRejeitarDisciplinaNulaNaOferta() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    void deveCriarTurmaComOfertaPeloConstrutorDeCompatibilidade() {
        // Arrange
        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Act
        var turma = new Turma("ESOFT4S-NA", disciplina, periodo());

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().getFirst().getDisciplina());
    }

    @Test
    void deveRejeitarDadosObrigatoriosDaTurma() {
        // Arrange
        var periodo = periodo();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(" ", periodo)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    void listaDeOfertasNaoDeveSerAlteradaExternamente() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo());
        turma.ofertarDisciplina(
                new Disciplina("POO", "Programação Orientada a Objetos", 80)
        );

        // Act + Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().clear()
        );
    }
}
