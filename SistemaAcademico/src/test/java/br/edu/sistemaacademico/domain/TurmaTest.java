package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurmaTest {

    private Turma turma() {
        return new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
    }

    private Disciplina poo() {
        return new Disciplina("POO", "Programação Orientada a Objetos", 80);
    }

    @Test
    @DisplayName("Deve rejeitar código em branco e período nulo")
    void deveRejeitarDadosInvalidos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("   ", new PeriodoLetivo(2026, Semestre.SEGUNDO))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    @DisplayName("Deve ofertar várias disciplinas no mesmo período letivo")
    void deveOfertarVariasDisciplinas() {
        var turma = turma();

        var ofertaPoo = turma.ofertarDisciplina(poo());
        var ofertaBancoDados = turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));

        assertEquals(2, turma.getOfertas().size());
        assertSame(turma, ofertaPoo.getTurma());
        assertEquals("2026/2", ofertaBancoDados.getPeriodoLetivo().toString());
        assertTrue(turma.toString().contains("Banco de Dados"));
    }

    @Test
    @DisplayName("Deve impedir a mesma disciplina ofertada duas vezes")
    void deveImpedirDisciplinaDuplicada() {
        var turma = turma();
        turma.ofertarDisciplina(poo());

        assertThrows(
                IllegalStateException.class,
                () -> turma.ofertarDisciplina(new Disciplina("POO", "Outro Nome", 40))
        );
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve exigir disciplina nas operações de oferta")
    void deveExigirDisciplina() {
        var turma = turma();

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
        assertThrows(IllegalArgumentException.class, () -> turma.possuiOferta(null));
        assertThrows(IllegalArgumentException.class, () -> turma.buscarOferta(null));
    }

    @Test
    @DisplayName("Deve localizar a oferta de uma disciplina da turma")
    void deveLocalizarOferta() {
        var turma = turma();
        var oferta = turma.ofertarDisciplina(poo());

        assertTrue(turma.possuiOferta(poo()));
        assertSame(oferta, turma.buscarOferta(poo()));
        assertFalse(turma.possuiOferta(new Disciplina("BD", "Banco de Dados", 80)));
        assertThrows(
                IllegalArgumentException.class,
                () -> turma.buscarOferta(new Disciplina("BD", "Banco de Dados", 80))
        );
    }

    @Test
    @DisplayName("Deve impedir alteração da lista de ofertas devolvida")
    void deveImpedirAlteracaoDasOfertas() {
        var turma = turma();
        var oferta = turma.ofertarDisciplina(poo());

        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().remove(oferta)
        );
    }

    @Test
    @DisplayName("Deve distinguir turmas de mesmo código em períodos diferentes")
    void deveDistinguirTurmasPorPeriodo() {
        var turma2026 = turma();
        var mesmaTurma = new Turma("esoft4s-na", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var turma2027 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2027, Semestre.SEGUNDO));

        assertTrue(turma2026.equals(mesmaTurma));
        assertEquals(turma2026.hashCode(), mesmaTurma.hashCode());
        assertFalse(turma2026.equals(turma2027));
        assertFalse(turma2026.equals("ESOFT4S-NA"));
    }
}