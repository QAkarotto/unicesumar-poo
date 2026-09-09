package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private PeriodoLetivo periodo() {
        return new PeriodoLetivo(2026, Semestre.SEGUNDO);
    }

    @Test
    @DisplayName("Deve criar turma e ofertar uma disciplina")
    void deveCriarTurmaEOfertarDisciplina() {
        var turma = new Turma("ESOFT4S-NA", periodo());
        var disciplina = new Disciplina("POO", "Programação", 80);

        var oferta = turma.ofertarDisciplina(disciplina);

        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(periodo(), turma.getPeriodoLetivo());
        assertEquals(1, turma.getOfertas().size());
        assertSame(disciplina, oferta.getDisciplina());
        assertSame(turma, oferta.getTurma());
    }

    @Test
    @DisplayName("Construtor com disciplina deve já criar a oferta")
    void construtorComDisciplinaDeveCriarOferta() {
        var disciplina = new Disciplina("POO", "Programação", 80);

        var turma = new Turma("ESOFT4S-NA", disciplina, periodo());

        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Não deve criar turma com código vazio")
    void naoDeveCriarTurmaComCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("  ", periodo())
        );
    }

    @Test
    @DisplayName("Não deve criar turma sem período letivo")
    void naoDeveCriarTurmaSemPeriodo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    @DisplayName("Não deve ofertar disciplina nula")
    void naoDeveOfertarDisciplinaNula() {
        var turma = new Turma("ESOFT4S-NA", periodo());

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
    }

    @Test
    @DisplayName("Não deve ofertar a mesma disciplina duas vezes")
    void naoDeveOfertarDisciplinaDuplicada() {
        var turma = new Turma("ESOFT4S-NA", periodo());
        var disciplina = new Disciplina("POO", "Programação", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(new Disciplina("POO", "Programação", 40))
        );
    }

    @Test
    @DisplayName("A lista de ofertas retornada deve ser imutável")
    void listaDeOfertasDeveSerImutavel() {
        var turma = new Turma("ESOFT4S-NA", periodo());

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().add(null)
        );
    }

    @Test
    @DisplayName("Matrícula por turma deve funcionar quando há uma única oferta")
    void matriculaPorTurmaComUnicaOferta() {
        var disciplina = new Disciplina("POO", "Programação", 80);
        var turma = new Turma("ESOFT4S-NA", disciplina, periodo());
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        var matricula = new Matricula("MAT-1", aluno, turma);

        assertSame(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Matrícula por turma deve falhar quando não há oferta")
    void matriculaPorTurmaSemOferta() {
        var turma = new Turma("ESOFT4S-NA", periodo());
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-1", aluno, turma)
        );
    }

    @Test
    @DisplayName("Matrícula por turma deve falhar quando há mais de uma oferta")
    void matriculaPorTurmaComVariasOfertas() {
        var turma = new Turma("ESOFT4S-NA", periodo());
        turma.ofertarDisciplina(new Disciplina("POO", "Programação", 80));
        turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-1", aluno, turma)
        );
    }

    @Test
    @DisplayName("toString deve conter código e período")
    void toStringDeveConterCodigoEPeriodo() {
        var turma = new Turma("ESOFT4S-NA", periodo());

        assertTrue(turma.toString().contains("ESOFT4S-NA"));
        assertTrue(turma.toString().contains("2026/2"));
    }
}
